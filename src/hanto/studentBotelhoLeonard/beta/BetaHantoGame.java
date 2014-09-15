/**
 * @author Andrew & Andrew || Botelho & Leonard
 * BetaHantoGame version of Hanto.  Each player can only place 1 butterfly and 5 sparrows.
 */

package hanto.studentBotelhoLeonard.beta;

import java.util.HashMap;
import java.util.Map;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.common.Butterfly;
import hanto.studentBotelhoLeonard.common.HantoBoard;
import hanto.studentBotelhoLeonard.common.Sparrow;

public class BetaHantoGame implements HantoGame {
	
	private HantoPlayerColor movesFirst;
	private int turnCount;
	private HantoBoard board;
	
	public BetaHantoGame(HantoPlayerColor movesFirst) {
		this.movesFirst = movesFirst;
		turnCount = 0;
		
		Map<HantoPieceType, Integer> pieceLimits = new HashMap<HantoPieceType, Integer>();
		pieceLimits.put(HantoPieceType.BUTTERFLY, 1);
		pieceLimits.put(HantoPieceType.SPARROW, 5);
		board = new HantoBoard(pieceLimits);
	}

	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {
		HantoPlayerColor color;
		HantoPiece piece;
		MoveResult result;
		
		// Beta can only place new pieces
		if (from != null) throw new HantoException("Can't move pieces in Beta Hanto. Only place new pieces.");
		
		// determine which player is making a move
		if (turnCount % 2 == 0) { // on even turns, the color movesFirst moves
			color = movesFirst;
		}
		else { // on odd turns, the color opposite of movesFirst moves
			if (movesFirst == HantoPlayerColor.BLUE) {
				color = HantoPlayerColor.RED;
			}
			else {
				color = HantoPlayerColor.BLUE;
			}
		}
		
		// switch case for adding different types of game pieces
		switch(pieceType) {
			case BUTTERFLY:
				piece = new Butterfly(color);
				break;
			case SPARROW:
				piece = new Sparrow(color);
				break;
			default:
				throw new HantoException("Unrecognized game piece type!");
		}
		
		board.addPiece(to, piece, turnCount/2); // our makeMove logic requires that we increment turncount when either player moves, correcting for external use.
		
		// check winning conditions
		boolean isBlueWinner = board.checkIfLost(HantoPlayerColor.RED);
		boolean isRedWinner = board.checkIfLost(HantoPlayerColor.BLUE);
		if (isBlueWinner && isRedWinner) {
			result = MoveResult.DRAW;
		}
		else if (isBlueWinner) {
			result = MoveResult.BLUE_WINS;
		}
		else if (isRedWinner) {
			result = MoveResult.RED_WINS;
		}
		// check for draw
		else if (!board.anyPiecesLeftToPlay()) {
			result = MoveResult.DRAW;
		}
		else {
			result = MoveResult.OK;
		}
		turnCount++;
		return result;
	}

	@Override
	public HantoPiece getPieceAt(HantoCoordinate where) {
		return board.getPieceAt(where);
	}

	@Override
	public String getPrintableBoard() {
		return board.toString();
	}

}
