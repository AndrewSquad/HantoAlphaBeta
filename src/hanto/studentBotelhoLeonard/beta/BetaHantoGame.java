/**
 * @author Andrew & Andrew || Botelho & Leonard
 * BetaHantoGame version of Hanto.  Each player can only place 1 butterfly and 5 sparrows.
 */

package hanto.studentBotelhoLeonard.beta;

import java.util.HashMap;

import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentBotelhoLeonard.common.BaseHantoGame;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;

/**
 * An implementation of the HantoGame interface for the Beta version of Hanto.
 * Each player gets one Butterfly and five Sparrows. Pieces can only be placed not moved.
 * It is now possible for there to be a winner and a loser.
 * @author Andy Botelho and Andrew Leonard
 *
 */
public class BetaHantoGame extends BaseHantoGame implements HantoGame {

	/**
	 * Constructor for BetaHanto. Creates a HantoBoard telling it that each player gets 
	 * one Butterfly and five Sparrows.
	 * @param movesFirst a HantoPlayerColor indicating which player goes first.
	 */
	public BetaHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		
		turnLimit = 12; // game ends after 6th full turn
		
		bluePiecesLeft = new HashMap<HantoPieceType, Integer>();
		bluePiecesLeft.put(HantoPieceType.BUTTERFLY, 1);
		bluePiecesLeft.put(HantoPieceType.SPARROW, 5);
		redPiecesLeft = new HashMap<HantoPieceType, Integer>(bluePiecesLeft);
	}

	
	// Validates potential moves
	protected void validateMove(HantoPieceType pieceType, PieceCoordinate from, PieceCoordinate to, HantoPlayerColor color) throws HantoException {
		// Can only place new pieces
		if (from != null) throw new HantoException("Can't move pieces in Beta Hanto. Only place new pieces.");

		// First move must be at (0, 0)
		if (board.getBoard().isEmpty()) {
			if (to.getX() != 0 || to.getY() != 0) throw new HantoException("First move must be at (0, 0)!");
		}
		else {
			if (!board.isAdjacentToAnyPiece(to) || board.isTileAlreadyOccupied(to)) {
				throw new HantoException("Invalid Move");
			}

			if (turnCount > 5 && !hasPlayerPlacedButterfly(color) && pieceType != HantoPieceType.BUTTERFLY) {
				throw new HantoException("A butterfly needs to be on the board by turn 4!");
			}

			int piecesLeft = playerPieceTypeRemaining(color, pieceType);
			if (piecesLeft == 0) {
				throw new HantoException("Player has no more pieces of that type left!");
			}
		}
				
	}

}
