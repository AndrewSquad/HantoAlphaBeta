/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * 
 * Abstract class BaseHantoGame that implements common functionality between Beta, Gamma,
 * Delta, and Epsilon versions.
 */

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
import hanto.common.HantoPrematureResignationException;
import hanto.common.MoveResult;

/**
 * Abstract class BaseHantoGame that implements common functionality between Beta, Gamma,
 * Delta, and Epsilon versions.
 */
public abstract class BaseHantoGame implements HantoGame {

	protected HantoPlayerColor movesFirst;
	protected int turnCount;
	protected int turnLimit;
	protected HantoBoard board;
	protected Map<HantoPieceType, Integer> bluePiecesLeft;
	protected Map<HantoPieceType, Integer> redPiecesLeft;
	protected Map<HantoPieceType, MoveValidator> pieceAbilities;
	protected boolean gameHasEnded;
	protected static MoveValidatorFactory moveValidatorFactory = MoveValidatorFactory.getInstance();
	protected static HantoPieceFactory pieceFactory = HantoPieceFactory.getInstance();

	
	/**
	 * Constructor for creating a new instance of a BaseHantoGame implementation.
	 * @param movesFirst the player who is moving first in this game.
	 */
	protected BaseHantoGame(HantoPlayerColor movesFirst) {
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
			if (playerHasLegalMove(color)) throw new HantoPrematureResignationException();
			gameHasEnded = true;
			result = (color == HantoPlayerColor.RED) ? MoveResult.BLUE_WINS: MoveResult.RED_WINS;
			return result;			
		}
		
		if (pieceType == null) throw new HantoException("Need to specify a piece");
		if (to == null) throw new HantoException("Need Destination");

		piece = pieceFactory.makePiece(pieceType, color);
		
		// use copy constructor on given HantoCoordinates
		PieceCoordinate newFrom = (from == null? null : new PieceCoordinate(from));
		PieceCoordinate newTo = (to == null? null : new PieceCoordinate(to));

		validateMove(pieceType, newFrom, newTo, color);
		
		if (newFrom == null) { // if we're placing a new piece
			decrementPieceTypeForPlayer(color, pieceType);
			board.addPiece(newTo, piece);
		}
		else { // if we're moving an existing piece
			board.moveExistingPiece(newFrom, newTo, board.getPieceAt(newFrom));
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
	
	
	public int getTurnCount() {
		return turnCount;
	}
	
	public HantoBoard getHantoBoard() {
		return board;
	}
	
	
	/**
	 * Given a HantoPieceType, this method will give you the corresponding MoveValidator for this game 
	 * (Walk, Jump, Fly, etc.).
	 * @param pieceType the piece type
	 * @return the MoveValidator for the given piece type
	 */
	public MoveValidator getValidator(HantoPieceType pieceType) {
		return pieceAbilities.get(pieceType);
	}
	
	/**
	 * Given a player color, this method determines whether or not he/she has placed a butterfly yet
	 * @param player the player to check for
	 * @return boolean indicating whether or not the player has placed their butterfly yet.
	 */
	public boolean hasPlayerPlacedButterfly(HantoPlayerColor player) {
		boolean hasPlacedButterfly = true;
		if (player == HantoPlayerColor.BLUE) {
			if (bluePiecesLeft.get(HantoPieceType.BUTTERFLY) > 0) hasPlacedButterfly = false;
		}

		else {
			if (redPiecesLeft.get(HantoPieceType.BUTTERFLY) > 0) hasPlacedButterfly = false;
		}

		return hasPlacedButterfly;
	}

	/**
	 * Highest layer helper method for validating a new move.
	 * This method is called when a new piece is placed as well as when a piece is moved.
	 * @param pieceType - the piece type of the HantoPiece being placed/moved.
	 * @param from - where the piece is moving from (null when a piece is placed on board)
	 * @param to - where the piece is going to be placed/moved to.
	 * @param color - player color of the player moving/placing the piece.
	 * @throws HantoException when the move is invalid for a number of possible reasons.
	 */
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
	
	
	/**
	 * Helper method called only when an existing piece is being moved to a new coordinate.
	 * Method validates if the movement is possible given the current state of the board.
	 * @param pieceType - the piece type of the HantoPiece being moved.
	 * @param from - where the piece is moving from
	 * @param to - where the piece is going to be moved to.
	 * @param color - player color of the player moving the piece.
	 * @throws HantoException when the move is invalid for a number of possible reasons.
	 */
	protected void validateMoveExistingPiece(HantoPieceType pieceType, PieceCoordinate from, PieceCoordinate to, HantoPlayerColor color) throws HantoException {

		// player must place Butterfly before moving an existing piece
		if(!hasPlayerPlacedButterfly(color)) throw new HantoException("Must place Butterfly before moving an existing piece!");

		if(!board.isTileAlreadyOccupied(from)) throw new HantoException("There is no piece at the coordinate you want to move from!");
		
		// piece type at from coordinate must match the given piece type
		if(pieceType != board.getPieceAt(from).getType()) throw new HantoException("Piece type mismatch!");
		
		// piece color at from coordinate must match the given player color
		if(color != board.getPieceAt(from).getColor()) throw new HantoException("Player color mismatch!");
		
		if(!pieceAbilities.get(pieceType).isMoveLegal(from, to)) throw new HantoException("Invalid movement of existing piece!");
	}
	

	/**
	 * Helper method called for validating the action of placing a new piece on the board.
	 * @param pieceType - the piece type of the HantoPiece being placed.
	 * @param from - should be null
	 * @param to - where the piece is going to be placed.
	 * @param color - player color of the player placing the piece.
	 * @throws HantoException when the move is invalid for a number of possible reasons
	 */
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


	/**
	 * Helper method used specifically for validating the action of adding a new piece 
	 * during the first turn of the game.
	 * @param pieceType - the piece type of the HantoPiece being placed.
	 * @param from - should be null
	 * @param to - where the piece is going to be placed.
	 * @param color - player color of the player placing the piece.
	 * @throws HantoException when the move is invalid for a number of possible reasons
	 */
	protected void validateFirstTurn(HantoPieceType pieceType, PieceCoordinate from, PieceCoordinate to, HantoPlayerColor color) throws HantoException {
		// First move must be at (0, 0)
		if (board.getBoardMap().isEmpty()) { // first player move
			if (to.getX() != 0 || to.getY() != 0) throw new HantoException("First move must be at (0, 0)!");
		}

		else { // second player move
			if (!board.isAdjacentToAnyPiece(to)) throw new HantoException("Second move must be adjacent to (0, 0)");
		}
	}
	
	
	/**
	 * Helper method called for validating the state of the board after a move has completed.
	 * Simply checks to see if the board is in a contiguous state or not.
	 * @throws HantoException
	 */
	protected void validatePostMove() throws HantoException {
		if (!board.isBoardContiguous()) {
			throw new HantoException("Invalid Move: The board is no longer contiguous!");
		}
	}



	/**
	 * Determines which player is making the current move based on the turn count and 
	 * who started the game.
	 * @return HantoPlayerColor indicating whose turn it is.
	 */
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



	/**
	 * Decrements how many of a certain piece type the given player has left to use.
	 * @param player - the player to decrement the piece count for.
	 * @param pieceType - the piece type to decrement
	 */
	protected void decrementPieceTypeForPlayer(HantoPlayerColor player, HantoPieceType pieceType) {
		int piecesLeft = playerPieceTypeRemaining(player, pieceType);
		if (player == HantoPlayerColor.BLUE) {
			bluePiecesLeft.put(pieceType, (piecesLeft - 1));
		}
		else {
			redPiecesLeft.put(pieceType, (piecesLeft - 1));
		}
	}


	/**
	 * Determines how many of a particular piece type the given player has left to put on the board
	 * @param player - the player to check for.
	 * @param pieceType - the piece type to check for
	 * @return int indicating how many of a particular piece type the given player has left 
	 * to put on the board
	 */
	public int playerPieceTypeRemaining(HantoPlayerColor player, HantoPieceType pieceType) {
		if (player == HantoPlayerColor.BLUE) {
			return bluePiecesLeft.get(pieceType);
		}
		else {
			return redPiecesLeft.get(pieceType);
		}
	}


	/**
	 * Determines if either player has any pieces left to place on the board
	 * @return boolean indicating if either player has any pieces left to place on the board.
	 */
	protected boolean anyPiecesLeftToPlay() {
		return redHasPiecesToPlay() || blueHasPiecesToPlay();
		
	}
	
	
	/**
	 * Determines whether or not the red player has any more pieces to place on the board.
	 * @return boolean indicating if red can place new pieces.
	 */
	private boolean redHasPiecesToPlay() {
		int totalPiecesLeft = 0;
		Iterator<Entry<HantoPieceType, Integer>> redPieceTotals = redPiecesLeft.entrySet().iterator();

		while(redPieceTotals.hasNext()) {
			Entry<HantoPieceType, Integer> entry = redPieceTotals.next();
			totalPiecesLeft += entry.getValue();
		}
		
		return totalPiecesLeft > 0;
	}
	
	
	/**
	 * Determines whether or not the blue player has any more pieces to place on the board.
	 * @return boolean indicating if blue can place new pieces.
	 */
	private boolean blueHasPiecesToPlay() {
		int totalPiecesLeft = 0;
		Iterator<Entry<HantoPieceType, Integer>> bluePieceTotals = bluePiecesLeft.entrySet().iterator();

		while(bluePieceTotals.hasNext()) {
			Entry<HantoPieceType, Integer> entry = bluePieceTotals.next();
			totalPiecesLeft += entry.getValue();
		}
		
		return totalPiecesLeft > 0;
	}


	// determine the game result after this move.  makes calls to HantoBoard
	/**
	 * Determines the game result after the current move has ended.
	 * @return MoveResult enum indicating the state of the game after the current move has ended.
	 */
	protected MoveResult determineGameResult() {
		MoveResult result;
		boolean isBlueWinner = board.checkIfPlayerLost(HantoPlayerColor.RED);
		boolean isRedWinner = board.checkIfPlayerLost(HantoPlayerColor.BLUE);
		if (isBlueWinner && isRedWinner) {
			result = MoveResult.DRAW;
			gameHasEnded = true;
		}
		else if (isBlueWinner) {
			result = MoveResult.BLUE_WINS;
			gameHasEnded = true;
		}
		else if (isRedWinner) {
			result = MoveResult.RED_WINS;
			gameHasEnded = true;
		}
		else if (!anyPiecesLeftToPlay() || turnCount >= turnLimit) { // check for draw
			result = MoveResult.DRAW;
			gameHasEnded = true;
		}
		else {
			result = MoveResult.OK;
		}

		return result;
	}
	
	
	/**
	 * Given a player color, this method determines if that color has any more legal moves to make in the game.
	 * @param color the player color to determine this for
	 * @return boolean indicating whether or not the player can make a legal move
	 */
	public boolean playerHasLegalMove(HantoPlayerColor color) {
		
		if (turnCount < 2) return true;
		
		boolean playerHasAPiece = (color == HantoPlayerColor.RED) ? redHasPiecesToPlay() : blueHasPiecesToPlay(); 
		// check if a piece can be placed
		if (playerHasAPiece && board.canPlayerPlacePiece(color)) return true;
		
		// check if an existing piece can be moved
		if (canPieceBeMoved(color)) return true;
		
		return false;
	}
	
	private boolean canPieceBeMoved(HantoPlayerColor color) {
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = board.getBoardMap().entrySet().iterator();
		PieceCoordinate next;
		HantoPiece piece;
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			piece = entry.getValue();
			if (piece.getColor() != color) continue;
			next = entry.getKey();
			if (pieceAbilities.get(piece.getType()).existsLegalMove(next)) return true;
		}
		return false;
	}
	

}
