package src;

public class GameResults {

	public Player winner;
	public int winningScore;
	public int losingScore;
	
	public GameResults(Player winner, int winningScore, int losingScore){
		this.winner = winner;
		this.winningScore = winningScore;
		this.losingScore = losingScore;
	}
	
}
