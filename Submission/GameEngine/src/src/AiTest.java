package src;

import src.playerTypes.PlayerHuman;
import src.playerTypes.PlayerRandom;
import src.playerTypes.ai_1.AI_1Player;
import src.playerTypes.ai_2.AI_2Player;

public class AiTest {

	
	
	public static void main(String[] args){
		
		Player player1 = new PlayerRandom("Harry and Josh");

		
		//Player player2 = new AI_1Player("Our Design");
		Player player2 = new AI_2Player("Q learning");

		
		int player1Wins = 0;
		int player2Wins = 0;
		
		int player2Seeds = 0;
		int player1Seeds = 0;
		
		int count = 0;
		
		for(int i = 0; i < 1000; i++){
			
			if(count == 100){
				System.out.println(i);
				count = 0;
			}
			

			Game game = new Game(player1, player2, false);

			GameResults results = game.playGame();

			Player winner = results.winner;
			
			
			if(winner == player2){
				player2Wins++;
				player2Seeds += results.winningScore;
				player1Seeds += results.losingScore;
			} else {
				player1Wins++;
				player1Seeds += results.winningScore;
				player2Seeds += results.losingScore;
			}
			count++;
		}
		
		System.out.println("");
		System.out.println("Player1Wins:  " + player1Wins+"    Total seeds: "+player1Seeds);
		System.out.println("Player2Wins:  " + player2Wins+"    Total seeds: "+player2Seeds);

	}
	
	
	
}
