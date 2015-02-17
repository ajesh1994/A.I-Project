package src;

public abstract class Player {

	public Game currentGame;
	
	public String playerName;
	public abstract int getNextMove();
	
	public Player(String playerName){
		this.playerName = playerName;
	}
	
	public abstract void prepareForNewGame();

	public abstract void deliverResults(int myScore, int opponentsScore);
	
}
