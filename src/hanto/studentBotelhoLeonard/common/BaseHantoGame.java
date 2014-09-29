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
	protected int turnLimit;
	protected HantoBoard board;
	protected Map<HantoPieceType, Integer> bluePiecesLeft;
	protected Map<HantoPieceType, Integer> redPiecesLeft;
	protected Map<HantoPieceType, MoveValidator> pieceAbilities;
	protected boolean gameHasEnded;

	public BaseHantoGame(HantoPlayerColor movesFirst) {
		this.movesFirst = movesFirst;
		turnCount = 0;
		board = new HantoBoard();
		gameHasEnded = false;
	}
	

	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {
		
		if (gameHasEnded) throw new HantoException("The game has already ended!");
		
		if (turnCount >= turnLimit) throw new HantoException("Turn limit exceeded!");

		HantoPlayerColor color;
		HantoPiece piece;
		MoveResult result;

		color = whoseTurnIsIt();
		
		// if the player resigned
		if (pieceType == null && from == null && to == null) {
			gameHasEnded = true;
			result = (color == HantoPlayerColor.RED) ? MoveResult.BLUE_WINS: MoveResult.RED_WINS;
			return result;			
		}
		

		//HantoPieceFactory.getInstance();
		piece = HantoPieceFactory.makePiece(pieceType, color);

		// use copy constructor on given HantoCoordinates
		PieceCoordinate newFrom = (from == null? null : new PieceCoordinate(from));
		PieceCoordinate newTo = (to == null? null : new PieceCoordinate(to));

		validateMove(pieceType, newFrom, newTo, color);
		
		if (newFrom == null) { // if we're placing a new piece
			decrementPieceTypeForPlayer(color, pieceType);
			board.addPiece(newTo, piece);
		}
		else { // if we're moving an existing piece
			board.moveExistingPiece(newFrom, newTo, piece);
		}
		
		validatePostMove();
		
		turnCount++;
		result = determineGameResult();
		
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

		else { // if an existing piece is being moved
			validateMoveExistingPiece(pieceType, from, to, color);
		}
	}
	
	
	protected void validateMoveExistingPiece(HantoPieceType pieceType, PieceCoordinate from, PieceCoordinate to, HantoPlayerColor color) throws HantoException {

		// player must place Butterfly before moving an existing piece
		if(!hasPlayerPlacedButterfly(color)) throw new HantoException("Must place Butterfly before moving an existing piece!");

		if(pieceType != board.getPieceAt(from).getType()) throw new HantoException("Piece type mismatch!");
		
		if(!pieceAbilities.get(pieceType).isMoveLegal(from, to)) throw new HantoException("Invalid movement of existing piece!");
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
		if (board.getBoardMap().isEmpty()) { // first player move
			if (to.getX() != 0 || to.getY() != 0) throw new HantoException("First move must be at (0, 0)!");
		}

		else { // second player move
			if (!board.isAdjacentToAnyPiece(to)) throw new HantoException("Second move must be adjacent to (0, 0)");
		}
	}
	
	
	// determines if the board is in a valid state after a move has been made
	protected void validatePostMove() throws HantoException {
		if (!board.isBoardContiguous()) {
			throw new HantoException("Invalid Move: The board is no longer contiguous!");
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
		else if (!anyPiecesLeftToPlay() || turnCount >= turnLimit) { // check for draw
			result = MoveResult.DRAW;
		}
		else {
			result = MoveResult.OK;
		}

		return result;
	}

}
