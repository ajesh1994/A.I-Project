package src;

import java.util.Random;

import src.playerTypes.PlayerHuman;
import src.playerTypes.PlayerRandom;
import src.playerTypes.ai_1.AI_1Player;

public class Main {

	public static void main(String[] args){
		
		Player harry = new PlayerHuman("Harry");
		
		Player ai1 = new AI_1Player("AI");
		
		Game game = new Game(harry, ai1, true);

		GameResults results = game.playGame();

		Player winner = results.winner;
		
		if(winner != null){
			System.out.println("Winner: "+winner.playerName);
		} else {
			System.out.println("Draw.");
		}
		
	}
	
	
	
	
}
