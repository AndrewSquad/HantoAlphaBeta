package hanto.tournament;

import hanto.common.HantoException;
import hanto.common.HantoGameID;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.epsilon.EpsilonHantoGame;
import hanto.studentBotelhoLeonard.tournament.HantoPlayer;

public class Main {

	/**
	 * @param args
	 * @throws HantoException 
	 */
	public static void main(String[] args) throws HantoException {
		HantoPlayer player1 = new HantoPlayer();
		HantoPlayer player2 = new HantoPlayer();
		
		player1.startGame(HantoGameID.EPSILON_HANTO, HantoPlayerColor.BLUE, true);
		player2.startGame(HantoGameID.EPSILON_HANTO, HantoPlayerColor.RED, false);
		
		HantoMoveRecord previousMove;
		
		EpsilonHantoGame theGame = new EpsilonHantoGame(HantoPlayerColor.BLUE);
		MoveResult mv;
		
		boolean running = true;
		previousMove = player1.makeMove(null);
		theGame.makeMove(previousMove.getPiece(), previousMove.getFrom(), previousMove.getTo());
		while (running) {	
			System.out.println("Blue");
			previousMove = player2.makeMove(previousMove);
			mv = theGame.makeMove(previousMove.getPiece(), previousMove.getFrom(), previousMove.getTo());
			if (mv != MoveResult.OK) {
				System.out.println(mv);
				running = false;
				continue;
			}
			System.out.println("Red");
			previousMove = player1.makeMove(previousMove);
			mv = theGame.makeMove(previousMove.getPiece(), previousMove.getFrom(), previousMove.getTo());
			if (mv != MoveResult.OK) {
				System.out.println(mv);
				running = false;
			}
		}
		

	}

}
