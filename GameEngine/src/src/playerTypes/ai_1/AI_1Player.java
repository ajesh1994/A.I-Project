package src.playerTypes.ai_1;

import java.util.ArrayList;

import src.gameState.GameState;
import src.playerTypes.PlayerAI;

public class AI_1Player extends PlayerAI {

	public ArrayList<State> stateArray = new ArrayList<State>();
	public ArrayList<Path> pathHistory = new ArrayList<Path>();

	
	public State getState(int[] houses, int store_player1, int store_player2){
		for(State stateInstance : stateArray){
			boolean skip = false;
			if(store_player1 != stateInstance.store_player1 || store_player2 != stateInstance.store_player2){
				continue;
			}
			for(int index = 0; index < houses.length; index++){
				if(houses[index] != stateInstance.houses[index]){
					skip = true;
				}
			}
			if(skip){
				continue;
			}
			return stateInstance;
		}
		return null;
	}
	
	public AI_1Player(String playerName){
		super(playerName);
	}
	
	@Override
	public int getNextMove() {
		GameState currentState = currentGame.currentGameState;
		State state = getState(currentState.houses, currentState.store_player1, currentState.store_player2);
		int nextMoveIndex = -1;
		
		/*
		 * We haven't encountered this state yet.
		 */
		if (state == null) {
			state = new State();
			state.store_player1 = currentState.store_player1;
			state.store_player2 = currentState.store_player2;
			state.houses = currentState.houses.clone();
			int[] validMoves = currentGame.getValidMoves(this);
			state.choiceOfMoves = new int[validMoves.length];
			state.moveWeight = new int[validMoves.length];
			for(int index = 0; index < validMoves.length; index++){
				state.choiceOfMoves[index] = validMoves[index];
				state.moveWeight[index] = 10;
			}
			ArrayList<State> similarStates = findSimilarStates(state);
			if(similarStates != null){
				if(similarStates.size() > 0){
					state.choiceOfMoves = similarStates.get(0).choiceOfMoves;
				}
			}
			stateArray.add(state);
		}
		nextMoveIndex = selectMove(state);
		pathHistory.add(new Path(state, nextMoveIndex));
		return state.choiceOfMoves[nextMoveIndex];
	}

	
	public int selectMove(State state) {
		RandomCollection<Integer> choiceOfMoves = new RandomCollection<Integer>();
		for(int index = 0; index < state.choiceOfMoves.length; index++){
			choiceOfMoves.add(state.moveWeight[index], index);
		}
		return choiceOfMoves.next();
	}

	
	public void rewardPath(Path path, int rewardAmount){
		State initialState = path.initialState;
		int move = path.move;
		rewardStateMove(initialState, move, rewardAmount);
	}
	
	public void rewardStateMove(State initialState, int move, int rewardAmount){
		initialState.moveWeight[move] += rewardAmount;
	}
	
	public void punishPath(Path path, int punishAmount){
		State initialState = path.initialState;
		int move = path.move;
		initialState.moveWeight[move] -= punishAmount;
		if(initialState.moveWeight[move] < 1){
			initialState.moveWeight[move] = 1;
		}
	}

	@Override
	public void prepareForNewGame() {
		pathHistory.clear();
	}
	
	public void rewardAllPaths(){
		for(Path path : pathHistory){
			rewardPath(path, 8);
		}
	}
	
	public ArrayList<State> findSimilarStates(State state){
		ArrayList<State> similarStates = new ArrayList<State>();
		if(currentGame.player1 == this){
			for(State stateInstance : stateArray){
				boolean skip = false;
				for(int index = 0; index < 6; index++){
					if(state.houses[index] != stateInstance.houses[index]){
						skip = true;
					}
				}
				if(skip){
					continue;
				}
				similarStates.add(stateInstance);
			}
		}
		if(currentGame.player2 == this){
			for(State stateInstance : stateArray){
				boolean skip = false;
				for(int index = 6; index < 12; index++){
					if(state.houses[index] != stateInstance.houses[index]){
						skip = true;
					}
				}
				if(skip){
					continue;
				}
				similarStates.add(stateInstance);
			}
		}
		return similarStates;
	}

	
	public void punishAllPaths(){
		for(Path path : pathHistory){
			punishPath(path, 8);
		}
	}


	@Override
	public void deliverResults(int myScore, int opponentsScore) {
		if(myScore > opponentsScore){
			rewardAllPaths();
		} else {
			punishAllPaths();
		}
		
	}

}
