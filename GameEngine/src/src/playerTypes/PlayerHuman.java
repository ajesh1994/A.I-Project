package src.playerTypes;

import java.util.Scanner;

import src.Player;

public class PlayerHuman extends Player {

	
	public PlayerHuman(String playerName){
		super(playerName);
	}
	
	@Override
	public int getNextMove() {
		
		Scanner in = new Scanner(System.in);

		System.out.print("Please input a house number: ");

		int input = 0;
			
			
		try {
			input = in.nextInt();in.nextLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return input;
	}

	@Override
	public void prepareForNewGame() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void deliverResults(int player1Score, int player2Score) {
		// TODO Auto-generated method stub
		
	}

	

	
}
