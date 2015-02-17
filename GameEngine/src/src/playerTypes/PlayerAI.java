package src.playerTypes;

import src.Player;

public abstract class PlayerAI extends Player {

	public PlayerAI(String playerName){
		super(playerName);
	}

	public abstract int getNextMove();
}
