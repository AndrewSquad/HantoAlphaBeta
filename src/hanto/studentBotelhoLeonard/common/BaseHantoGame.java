package hanto.studentBotelhoLeonard.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;

public abstract class BaseHantoGame implements HantoGame {

	protected HantoPlayerColor movesFirst;
	protected int turnCount;
	protected HantoBoard board;
	protected Map<HantoPieceType, Integer> bluePiecesLeft;
	protected Map<HantoPieceType, Integer> redPiecesLeft;

	public BaseHantoGame(HantoPlayerColor movesFirst) {
		this.movesFirst = movesFirst;
		turnCount = 0;
		board = new HantoBoard();
	}
	

	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {
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
	

	@Override
	public HantoPiece getPieceAt(HantoCoordinate where) {
		PieceCoordinate coordinate = new PieceCoordinate(where); // copy constructor
		return board.getPieceAt(coordinate);
	}

	@Override
	public String getPrintableBoard() {
		return board.toString();
	}

	// Validates potential moves
	protected void validateMove(HantoPieceType pieceType, PieceCoordinate from, PieceCoordinate to, HantoPlayerColor color) throws HantoException {

		if (turnCount > 5 && !hasPlayerPlacedButterfly(color) && pieceType != HantoPieceType.BUTTERFLY) {
			throw new HantoException("A butterfly needs to be on the board by turn 4!");
		}

		if (from == null) { // if a new piece is being placed
			validateAddPiece(pieceType, from, to, color);
		}

		else { // if a piece is being moved

			//			if (!board.isAdjacentToAnyPiece(to) || board.isTileAlreadyOccupied(to)) {
			//				throw new HantoException("Invalid Move");
			//			}

		}

	}
	

	// Validates adding pieces
	protected void validateAddPiece(HantoPieceType pieceType, PieceCoordinate from, PieceCoordinate to, HantoPlayerColor color) throws HantoException {
		
		if (turnCount < 2) { // If it's either player's first move, rules are slightly different
			validateFirstTurn(pieceType, from, to, color);
		}

		else {
			int piecesLeft = playerPieceTypeRemaining(color, pieceType);
			if (piecesLeft == 0) {
				throw new HantoException("Player has no more pieces of that type left!");
			}

			if (!board.isAdjacentSameColorPiece(to, color) || board.isTileAlreadyOccupied(to) || board.isAdjacentToOtherColorPiece(to, color)) {
				throw new HantoException("Invalid Move");
			}
		}
	}


	// Validate first move
	protected void validateFirstTurn(HantoPieceType pieceType, PieceCoordinate from, PieceCoordinate to, HantoPlayerColor color) throws HantoException {
		// First move must be at (0, 0)
		if (board.getBoard().isEmpty()) { // first player move
			if (to.getX() != 0 || to.getY() != 0) throw new HantoException("First move must be at (0, 0)!");
		}

		else { // second player move
			if (!board.isAdjacentToAnyPiece(to)) throw new HantoException("Second move must be adjacent to (0, 0)");
		}
	}



	// determines whose turn it is based on movesFirst and turnCount
	protected HantoPlayerColor whoseTurnIsIt() {
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


	// given a player color, this method determines whether or not he/she has placed a butterfly yet
	protected boolean hasPlayerPlacedButterfly(HantoPlayerColor player) {
		boolean hasPlacedButterfly = true;
		if (player == HantoPlayerColor.BLUE) {
			if (bluePiecesLeft.get(HantoPieceType.BUTTERFLY) > 0) hasPlacedButterfly = false;
		}

		else {
			if (redPiecesLeft.get(HantoPieceType.BUTTERFLY) > 0) hasPlacedButterfly = false;
		}

		return hasPlacedButterfly;
	}

	// decrements how many of a certain piece type the given player has left to use
	protected void decrementPieceTypeForPlayer(HantoPlayerColor player, HantoPieceType pieceType) {
		int piecesLeft = playerPieceTypeRemaining(player, pieceType);
		if (player == HantoPlayerColor.BLUE) {
			bluePiecesLeft.put(pieceType, (piecesLeft - 1));
		}
		else {
			redPiecesLeft.put(pieceType, (piecesLeft - 1));
		}
	}



	// determines how many of a particular piece type the given player has left to put on the board
	protected int playerPieceTypeRemaining(HantoPlayerColor player, HantoPieceType pieceType) {
		if (player == HantoPlayerColor.BLUE) {
			return bluePiecesLeft.get(pieceType);
		}
		else {
			return redPiecesLeft.get(pieceType);
		}
	}

	// determines if either player has any pieces left to move with
	protected boolean anyPiecesLeftToPlay() {
		int totalPiecesLeft = 0;
		Iterator<Entry<HantoPieceType, Integer>> bluePieceTotals = bluePiecesLeft.entrySet().iterator();
		Iterator<Entry<HantoPieceType, Integer>> redPieceTotals = redPiecesLeft.entrySet().iterator();

		while(bluePieceTotals.hasNext()) {
			Entry<HantoPieceType, Integer> entry = bluePieceTotals.next();
			totalPiecesLeft += entry.getValue();
		}
		while(redPieceTotals.hasNext()) {
			Entry<HantoPieceType, Integer> entry = redPieceTotals.next();
			totalPiecesLeft += entry.getValue();
		}

		return totalPiecesLeft > 0;
	}


	// determine the game result after this move.  makes calls to HantoBoard
	protected MoveResult determineGameResult() {
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
