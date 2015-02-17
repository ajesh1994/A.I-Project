package src.playerTypes.ai_2;

import java.util.ArrayList;

import src.Game;
import src.gameState.GameState;
import src.playerTypes.PlayerAI;


public class AI_2Player extends PlayerAI {

	private double alpha = 0.5;
	private double discount = 0.9;

	private int networkInputSize;
	private NeuralNet NeuralNetwork;

	public ArrayList<Path> pathHistory = new ArrayList<Path>();

	public AI_2Player(String playerName){
		super(playerName);
		networkInputSize = 2+(Game.TOTAL_HOUSES/2)*2+1;
		NeuralNetwork = new NeuralNet(networkInputSize,2*networkInputSize);
	}

	@Override
	public int getNextMove() {

		GameState currentState = currentGame.currentGameState;

		State state = new State();
		state.store_player1 = currentState.store_player1;
		state.store_player2 = currentState.store_player2;
		state.houses = currentState.houses.clone();


		int[] validMoves = currentGame.getValidMoves(this);
		double[] qValues = getQValues(state);

		if(validMoves.length == 0){ 
			return -1;
		}

		double[] validQValues = new double[validMoves.length];
		for(int moveIndex = 0; moveIndex < validMoves.length;moveIndex++) {
			validQValues[moveIndex] = qValues[validMoves[moveIndex]];
		}

		int validMove = maxIndex(validQValues);
		int nextMove = validMoves[validMove];

		pathHistory.add(new Path(state, nextMove));
		return nextMove;
	}




	public double[] getStateInput(State state) {
		int[] mySide = currentGame.getHouses(this, state);
		int[] oppSide = currentGame.getHouses(currentGame.getOpponent(this), state);
		int myScore = currentGame.getStoreAmount(this, state);
		int opponenetScore = currentGame.getStoreAmount(currentGame.getOpponent(this), state);
		//compose state

		double[] stateInput = new double[networkInputSize-1];
		int stateIndex=0;
		stateInput[stateIndex] = (double)myScore;
		stateIndex++;
		for(int j = 0; j < Game.HOUSES_PER_PLAYER; j++, stateIndex++) {
			stateInput[stateIndex] = (double)mySide[j];
		}
		stateInput[stateIndex] = (double)opponenetScore;
		stateIndex++;
		for(int j=0;j<Game.HOUSES_PER_PLAYER;j++,stateIndex++) {
			stateInput[stateIndex] = (double)oppSide[j];
		}
		return stateInput;
	}

	public double[] getQValues(State state) {
		double[] qVals = new double[Game.HOUSES_PER_PLAYER];
		double[] stateInput = getStateInput(state);
		for(int houseIndex = 0; houseIndex < Game.HOUSES_PER_PLAYER; houseIndex++) {
			qVals[houseIndex] = NeuralNetwork.calculate(NeuralNetwork.convertStateInputToNetworkInput(stateInput, houseIndex));
		}
		return qVals;
	}



	//Find the index of largest of and array of doubles
	public static int maxIndex(double[] array) {
		int index = 0;
		for(int i=0;i<array.length;i++) {
			if(array[i]>array[index]) index=i;
		}
		return index;
	}


	public void learnFromGame(double reward){


		Path lastPath = pathHistory.get(pathHistory.size()-1);

		double[] state = getStateInput(lastPath.initialState);

		int action = lastPath.move;

		double[] example = NeuralNetwork.convertStateInputToNetworkInput(state, action);

		double oldQ = NeuralNetwork.calculate(example);
		double newQ = (1-alpha)*oldQ+alpha*reward;
		NeuralNetwork.learnFromExample(example,newQ);


		double[] nextExample = example;

		for(int pathIndex = 0; pathIndex < pathHistory.size(); pathIndex++) {
			Path path = pathHistory.get((pathHistory.size()-1)-pathIndex);
			reward = path.reward;
			state = getStateInput(path.initialState);
			action = path.move;
			example = NeuralNetwork.convertStateInputToNetworkInput(state, action);
			double[] qVals = new double[Game.HOUSES_PER_PLAYER];
			for(int a=0;a<Game.HOUSES_PER_PLAYER;a++) {
				nextExample[0] = (double)a;
				qVals[a] = NeuralNetwork.calculate(nextExample);
			}
			double maxVal = qVals[maxIndex(qVals)];
			oldQ = NeuralNetwork.calculate(example);
			newQ = (1-alpha)*oldQ+alpha*(reward+discount*maxVal);
			NeuralNetwork.learnFromExample(example,newQ);
		}
	}



	@Override
	public void prepareForNewGame() {
		// TODO Auto-generated method stub

	}


	@Override
	public void deliverResults(int myScore, int opponentsScore) {
		double reward = (double)myScore - (double)opponentsScore;
		learnFromGame(reward);
		pathHistory.clear();
	}

}
