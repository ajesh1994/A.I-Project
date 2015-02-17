package src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import src.gameState.GameState;
import src.playerTypes.ai_1.State;

public class Game {

	public static final int TOTAL_HOUSES = 12;
	public static final int HOUSES_PER_PLAYER = TOTAL_HOUSES/2;
	
	public Player player1;
	public Player player2;
	

	
	public GameState currentGameState;
	

	public boolean output = false;
	public boolean playerTwosTurn = true;
	
	public Game(Player player1, Player player2, boolean output){
		this.player1 = player1;
		this.player2 = player2;
		
		this.player1.prepareForNewGame();
		this.player2.prepareForNewGame();
		
		player1.currentGame = this;
		player2.currentGame = this;
		this.output = output;
		
		currentGameState = new GameState();
		
		currentGameState.store_player1 = 0;
		currentGameState.store_player2 = 0;
		currentGameState.houses = new int[12];
		for(int index = 0; index < TOTAL_HOUSES; index++){
			currentGameState.houses[index] = 4;
		}
		
		
		Random random = new Random();
		playerTwosTurn = random.nextInt(2) == 0;
		
	}

	public Player getOpponent(Player player){
		if(player == player1){
			return player2;
		} else {
			return player1;
		}
	}
	

	public int[] getHouses(Player player, GameState state){
		int[] side = new int[6];
		if(player == player1){
			for(int index = 0; index < 6; index++){
				side[index] = currentGameState.houses[player == player1 ? index : index+6];
			}
		}
		return side;
	}
	
	public int getStoreAmount(Player player, GameState state){
		if(player == player1){
			return currentGameState.store_player1;
		} else {
			return currentGameState.store_player2;
		}
	}
	
	public int[] getValidMoves(Player player){
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for(int houseIndex = 0; houseIndex < 6; houseIndex++){
			int globalHouse = convertHouseToGlobalHouse(player, houseIndex);
			if(currentGameState.houses[globalHouse] > 0){
				moves.add(houseIndex);
			}
    	}
		int[] movesArray = new int[moves.size()];
		int count = 0;
		for(Integer i : moves){
			movesArray[count] = i;
			count++;
		}
		return movesArray;
	}
	
	public Player getWinningPlayer(){
		if(currentGameState.store_player1 > currentGameState.store_player2){
			return player1;
		} else if (currentGameState.store_player2 > currentGameState.store_player1){
			return player2;
		}
		return null;
	}
	
	public Player getCurrentPlayersTurn(){
		if(!playerTwosTurn){
			return player1;
		} else {
			return player2;
		}
	}
	
	public int convertHouseToGlobalHouse(Player player, int house){
		if(player == player1){
			return house;
		} else {
			return house + 6;
		}
	}
	
	public boolean doesGlobalHouseBelongToPlayer(Player player, int globalHouse){
		if(globalHouse > 5){
			return player == player2;
		} else {
			return player == player1;
		}
	}
	
	public int getOppositeGlobalHouse(int globalHouse){
			return 11-globalHouse;
	}
	
	public void depositAllSeeds(Player player){
		
		int totalSeeds = 0;
		for(int houseIndex = 0; houseIndex < 6; houseIndex++){
			int globalHouse = convertHouseToGlobalHouse(player, houseIndex);
			if(currentGameState.houses[globalHouse] > 0){
				totalSeeds+=currentGameState.houses[globalHouse];
				currentGameState.houses[globalHouse] = 0;
			}
    	}
		increaseStore(player, totalSeeds);
		
	}
	
	
	
	
	public GameResults playGame(){
		output();
		
		while(true){
			
			if(getValidMoves(player1).length == 0 || getValidMoves(player2).length == 0){
				depositAllSeeds(player1);
				depositAllSeeds(player2);
				break;
			}
			
			if(output)
			System.out.println("[Player "+(playerTwosTurn ? 2 : 1)+"'s turn!]");
			
			Player player = getCurrentPlayersTurn();

			while(true){
				int nextMove = player.getNextMove();
				
				boolean move = sowerSeeds(player, nextMove);
				if(move){
					if(output)
					System.out.println("Move: "+nextMove);
					break;
				} else {
					if(output)
					System.out.println("["+(playerTwosTurn ? 2 : 1)+"] Move was invalid: "+nextMove);
				}
			}
			output();
			
		}
		output();
		
		player1.deliverResults(currentGameState.store_player1, currentGameState.store_player2);
		player2.deliverResults(currentGameState.store_player2, currentGameState.store_player1);
		
		Player winner = getWinningPlayer();
		
		GameResults results = new GameResults(winner, getStoreAmount(winner, currentGameState), getStoreAmount(getOpponent(winner), currentGameState));
		
		return results;
	}
	
	
	
	public boolean sowerSeeds(Player player, int house){
		if(house < 0 || house > 5){
			return false;
		}
		
		int globalHouse = convertHouseToGlobalHouse(player, house);
		return move(player, globalHouse);
	}
	
	public void increaseStore(Player player, int amount){
		if(player == player1){
			currentGameState.store_player1+= amount;
		} else {
			currentGameState.store_player2+= amount;
		}
	}
	
	public boolean move(Player player, int house){

		int totalSeeds = currentGameState.houses[house];

		if(totalSeeds == 0){
			//invalid move
			return false;
		}

		currentGameState.houses[house] = 0;

		boolean giveAnotherGo = false;

		int currentHouse = house+1;

		for(int seedIndex = 0; seedIndex < totalSeeds; seedIndex++){
			if(currentHouse == 6 && player == player1){
				currentGameState.store_player1++;
				seedIndex++;

				if(seedIndex == totalSeeds){
					giveAnotherGo = true;
				}
			}
			if(currentHouse == 12){
				currentHouse = 0;
				if(player == player2){
					currentGameState.store_player2++;
					seedIndex++;
					if(seedIndex == totalSeeds){
						giveAnotherGo = true;
					}
				}
			}
			if(seedIndex < totalSeeds){
				currentGameState.houses[currentHouse] += 1;
				
				if(seedIndex == totalSeeds-1){
					
					if(doesGlobalHouseBelongToPlayer(player, currentHouse)){
						if(currentGameState.houses[currentHouse] == 1){
							
							int oppositeHouse = getOppositeGlobalHouse(currentHouse);
							
							if(currentGameState.houses[oppositeHouse] > 0){
								increaseStore(player, currentGameState.houses[oppositeHouse]);
								currentGameState.houses[oppositeHouse] = 0;
							}
							
							increaseStore(player, 1);
							currentGameState.houses[currentHouse] = 0;
							
						}
					}
				}
				currentHouse++;
			}
		}

		if(!giveAnotherGo){
			playerTwosTurn = !playerTwosTurn;
		}
		return true;
	}
	
	public void output(){

		if(!output){
			return;
		}
		
		System.out.flush();
		
		
    	System.out.println(" ");
    	//move(10);
    	for(int i = 5; i>= 0; i--){
    		System.out.print("  ["+currentGameState.houses[i]+"]  ");
    	}
    	System.out.println("");//new line
    	System.out.println("["+currentGameState.store_player1+"]                                     ["+currentGameState.store_player2+"]");
    	
    	for(int i = 6; i < 12; i++){
    		System.out.print("  ["+currentGameState.houses[i]+"]  ");
    	}
    	System.out.println(" ");
    }

	
	
	
}
