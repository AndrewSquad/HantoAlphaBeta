/**
 * @author Andrew & Andrew || Botelho & Leonard
 * AlphaHantoGame version of Hanto.
 * Each player can only place one butterfly.
 */

package hanto.studentBotelhoLeonard.alpha;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.common.Butterfly;
import hanto.studentBotelhoLeonard.common.HantoBoard;

/**
 * An implementation of the HantoGame interface for the Alpha version of Hanto.
 * Each player can only place 1 butterfly on the board.
 * @author Andy Botelho and Andrew Leonard
 *
 */
public class AlphaHantoGame implements HantoGame {

	private int turnCount;
	private HantoBoard board;

	/**
	 * Constructor for AlphaHantoGame.
	 * Creates a HantoBoard telling it that each player gets 1 butterfly.
	 */
	public AlphaHantoGame() {
		turnCount = 0;

		board = new HantoBoard();
	}

	
	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {

		MoveResult result;
		HantoPlayerColor color;
		
		validateMove(pieceType, from, to);
						
		if (turnCount % 2 ==  0) { // if the turn count is even - blue should go
			color = HantoPlayerColor.BLUE;
		}
		else { // turn count is odd - red should go
			color = HantoPlayerColor.RED;
		}
		
		Butterfly butterfly = new Butterfly(color);
		board.addPiece(to, butterfly);

		turnCount++;
		
		if (turnCount == 2) { // Alpha Hanto ends after turn 2
			result = MoveResult.DRAW;
		}
		else {
			result = MoveResult.OK;
		}
		
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
	
	// Validates potential moves
	private void validateMove(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {
		
		if (from != null) { // Can only place new pieces
			throw new HantoException("Can't move pieces in Alpha Hanto. Only place new pieces.");
		}
		
		// If the given pieceType is not a Butterfly, throw exception
		if (pieceType != HantoPieceType.BUTTERFLY) {
			throw new HantoException("Unrecognized Piece Type");	
		}
		
		// First move must be at (0, 0)
		if (board.getBoard().isEmpty()) {
			if (to.getX() != 0 || to.getY() != 0) throw new HantoException("First move must be at (0, 0)!");
		}
		
		else if (!board.isAdjacentToAnyPiece(to) || board.isTileAlreadyOccupied(to)) throw new HantoException("Invalid Move");
				
	}

}
