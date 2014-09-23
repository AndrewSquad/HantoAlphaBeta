/**
 * @author Andrew & Andrew || Botelho & Leonard
 * BetaHantoGame version of Hanto.  Each player can only place 1 butterfly and 5 sparrows.
 */

package hanto.studentBotelhoLeonard.beta;

import java.util.HashMap;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.common.BaseHantoGame;
import hanto.studentBotelhoLeonard.common.Butterfly;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;
import hanto.studentBotelhoLeonard.common.Sparrow;

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
		
		bluePiecesLeft = new HashMap<HantoPieceType, Integer>();
		bluePiecesLeft.put(HantoPieceType.BUTTERFLY, 1);
		bluePiecesLeft.put(HantoPieceType.SPARROW, 5);
		redPiecesLeft = new HashMap<HantoPieceType, Integer>(bluePiecesLeft);
	}

	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {
		HantoPlayerColor color;
		HantoPiece piece;
		MoveResult result;
		
		color = whoseTurnIsIt();
				
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
		
		// use copy constructor on given HantoCoordinates
		PieceCoordinate newFrom = (from == null? null : new PieceCoordinate(from));
		PieceCoordinate newTo = (to == null? null : new PieceCoordinate(to));
		
		validateMove(pieceType, newFrom, newTo, color);
		decrementPieceTypeForPlayer(color, pieceType);
		board.addPiece(newTo, piece);
		result = determineGameResult();

		turnCount++;
		return result;
	}
	

	// determines whose turn it is based on movesFirst and turnCount
	private HantoPlayerColor whoseTurnIsIt() {
		HantoPlayerColor color;
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
		
		return color;
	}

	
	// Validates potential moves
	private void validateMove(HantoPieceType pieceType, PieceCoordinate from, PieceCoordinate to, HantoPlayerColor color) throws HantoException {
		// Can only place new pieces
		if (from != null) throw new HantoException("Can't move pieces in Alpha Hanto. Only place new pieces.");

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
	
	
	// determine the game result after this move.  makes calls to HantoBoard
	private MoveResult determineGameResult() {
		MoveResult result;
		boolean isBlueWinner = board.checkIfPlayerLost(HantoPlayerColor.RED);
		boolean isRedWinner = board.checkIfPlayerLost(HantoPlayerColor.BLUE);
		if (isBlueWinner && isRedWinner) {
			result = MoveResult.DRAW;
		}
		else if (isBlueWinner) {
			result = MoveResult.BLUE_WINS;
		}
		else if (isRedWinner) {
			result = MoveResult.RED_WINS;
		}
		else if (!anyPiecesLeftToPlay()) { // check for draw
			result = MoveResult.DRAW;
		}
		else {
			result = MoveResult.OK;
		}
		
		return result;
	}

}
