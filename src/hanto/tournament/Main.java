package hanto.tournament;

import hanto.common.HantoGameID;
import hanto.common.HantoPlayerColor;
import hanto.studentBotelhoLeonard.tournament.HantoPlayer;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HantoPlayer player1 = new HantoPlayer();
		HantoPlayer player2 = new HantoPlayer();
		
		player1.startGame(HantoGameID.EPSILON_HANTO, HantoPlayerColor.BLUE, true);
		player2.startGame(HantoGameID.EPSILON_HANTO, HantoPlayerColor.RED, false);
		
		HantoMoveRecord previousMove;
		
		boolean running = true;
		previousMove = player1.makeMove(null);
		while (running) {
			System.out.println("Blue");
			previousMove = player2.makeMove(previousMove);
			System.out.println("Red");
			previousMove = player1.makeMove(previousMove);
		}
		

	}

}
