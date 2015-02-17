package src.gameState;

public class GameState {

	//
	public int store_player1;
	public int store_player2;
	
	public int[] houses;
	
	public boolean equals(GameState state){
		if(store_player1 != state.store_player1 || store_player2 != state.store_player2){
			return false;
		}
		
		for(int index = 0; index < houses.length; index++){
			if(houses[index] != state.houses[index]){
				return false;
			}
		}
		return true;
	}
	
}
