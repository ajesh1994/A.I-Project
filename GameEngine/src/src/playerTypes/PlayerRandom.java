package src.playerTypes;


public class PlayerRandom extends PlayerAI {

	
	public PlayerRandom(String playerName){
		super(playerName);
	}
	
	@Override
	public int getNextMove() {
		
		int[] validMoves = currentGame.getValidMoves(this);
		
		int move = validMoves[random(validMoves.length-1)];
		
		return move;
	}
	
	public static int random(int range) {
		return (int)(java.lang.Math.random() * (range+1));
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
