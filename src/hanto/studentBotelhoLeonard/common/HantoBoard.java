/**
 * @author Andrew & Andrew || Botelho & Leonard
 * This is the HantoBoard class that manages if moves are valid and what is currently on the board.
 */
package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Class in charge of keeping track what pieces are on the board and at which location.
 * This class validates a potential move and keeps track of how many pieces each player can play during the game.
 * @author Andy Botelho and Andrew Leonard
 *
 */
public class HantoBoard {

	private Map<HantoCoordinate, HantoPiece> board;
	private Map<HantoPieceType, Integer> bluePiecesLeft;
	private Map<HantoPieceType, Integer> redPiecesLeft;
	
	
	/**
	 * Constructor for a HantoBoard.  Initializes Maps used to keep track of which pieces are on 
	 * the board and how many pieces each player has placed.
	 * @param pieceLimits a Map that indicates how many of each HantoPieceType 
	 * a player can play throughout the game
	 */
	public HantoBoard(Map<HantoPieceType, Integer> pieceLimits) {
		board = new HashMap<HantoCoordinate, HantoPiece>();
		
		bluePiecesLeft = new HashMap<HantoPieceType, Integer>(pieceLimits);
		redPiecesLeft = new HashMap<HantoPieceType, Integer>(pieceLimits);
	}

	
	/** A getter for the HashMap that maps each HantoCoordinate to which HantoPiece is at that coordinate.
	 * @return the board HashMap
	 */
	public Map<HantoCoordinate, HantoPiece> getBoard() {
		return board;
	}
	
	/**
	 * Looks at the HantoBoard's Hashmap and gets the HantoPiece object at the given HantoCoordinate.
	 * @param coordinate the HantoCoordinate to look at.
	 * @return the HantoPiece found at the given coordinate.
	 */
	public HantoPiece getPieceAt(HantoCoordinate coordinate) {
		return board.get(coordinate);
	}

	
	/**
	 * The method used for adding a piece to the HantoBoard. Checks a few conditions before adding the piece.
	 * For example: new pieces (except the first) must be adjacent to other pieces, a butterfly must be
	 * placed by the 4th turn, and the player must have enough pieces of the given type left.
	 * After checking these conditions, the method adds the coordinate/piece to the HantoBoard's board HashMap.
	 * @param coordinate the HantoCoordinate to place the piece at.
	 * @param piece the HantoPiece being added to the board.
	 * @param turnCount the current turn count
	 * @throws HantoException in cases where the piece cannot be put on the given HantoCoordinate
	 */
	public void addPiece(HantoCoordinate coordinate, HantoPiece piece, int turnCount) throws HantoException{
		if (!validateNewPieceAdd(coordinate)) {
			throw new HantoException("Invalid position!");
		}
		
		if(turnCount > 2 && !playerPlacedButterfly(piece.getColor()) && piece.getType() != HantoPieceType.BUTTERFLY){
			throw new HantoException("A buttefly needs to be on the board by turn 4!");
		}
		
		int piecesLeft = playerPieceTypeRemaining(piece.getColor(), piece.getType());
		if (piecesLeft == 0) {
			throw new HantoException("Player has no more pieces of that type left!");
		}
		if (piece.getColor() == HantoPlayerColor.BLUE) {
			bluePiecesLeft.put(piece.getType(), (piecesLeft - 1));
		}
		else {
			redPiecesLeft.put(piece.getType(), (piecesLeft - 1));
		}
		
		board.put(coordinate, piece);
	}
	

	/**
	 * Determines what state the board is in after a move.
	 * Is the board OK and another move can occur? Is the game a draw? Has a player won?
	 * @return the MoveResult indicating the state of the game: either OK, DRAW, RED_WINS, or BLUE_WINS
	 */
	public MoveResult determineGameResult() {
		MoveResult result;
		boolean isBlueWinner = checkIfLost(HantoPlayerColor.RED);
		boolean isRedWinner = checkIfLost(HantoPlayerColor.BLUE);
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
		else if (!anyPiecesLeftToPlay()) {
			result = MoveResult.DRAW;
		}
		else {
			result = MoveResult.OK;
		}
		
		return result;
	}
	
	
	// Given the destination coordinate and the HantoPiece being placed, this method 
	// determines if the piece can be placed at the given coordinate. Enforces that the first played 
	// piece must be at (0, 0) and pieces after that must be placed adjacent to occupied tiles.
	private boolean validateNewPieceAdd(HantoCoordinate coordinate) {
		// if first turn, piece must be put in center of board
		if (board.isEmpty() && coordinate.getX() == 0 && coordinate.getY() == 0) return true;

		// otherwise, a new piece must be adjacent to an existing piece
		return isAdjacentToAnyPiece(coordinate) && !isTileAlreadyOccupied(coordinate);
	}
	
	
	// Determines if either player has anymore pieces left to put on the board.
	private boolean anyPiecesLeftToPlay() {
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
	

	// Given a particular player color, this method checks to see if that Butterfly is surrounded
	private boolean checkIfLost(HantoPlayerColor color) {
		HantoPiece tempPiece;
		Iterator<Entry<HantoCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		PieceCoordinate coordinate = null;
		while(pieces.hasNext()) {
			Entry<HantoCoordinate, HantoPiece> entry = pieces.next();
			coordinate = (PieceCoordinate) entry.getKey();
			tempPiece = board.get(coordinate);
			if (tempPiece.getType() == HantoPieceType.BUTTERFLY && tempPiece.getColor() == color) {
				return has6Neighbors(coordinate);
			}
		}
		return false;
	}

	// determines if a particular PieceCoordinate is surrounded by 6 other occupied tiles/pieces
	private boolean has6Neighbors(PieceCoordinate butterflyPos) {
		int neighbors = 0;
		
		Iterator<Entry<HantoCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		PieceCoordinate next;
		while(pieces.hasNext()) {
			Entry<HantoCoordinate, HantoPiece> entry = pieces.next();
			next = (PieceCoordinate) entry.getKey();
			if (next.isAdjacentTo(butterflyPos)) neighbors++;
		}
		
		return neighbors == 6;
		
	}
	
	// determines if a given HantoCoordinate is adjacent to any already placed HantoPieces
	private boolean isAdjacentToAnyPiece(HantoCoordinate coordinate) {
		Iterator<Entry<HantoCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		PieceCoordinate next;
		while(pieces.hasNext()) {
			Entry<HantoCoordinate, HantoPiece> entry = pieces.next();
			next = (PieceCoordinate) entry.getKey();			
			if (next.isAdjacentTo(coordinate)) {
				return true;			 
			}
		}
		return false;
	}
	
	// determines if a given HantoCoordinate already has a piece on it
	private boolean isTileAlreadyOccupied(HantoCoordinate coordinate) {
		Iterator<Entry<HantoCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		PieceCoordinate next;
		while(pieces.hasNext()) {
			Entry<HantoCoordinate, HantoPiece> entry = pieces.next();
			next = (PieceCoordinate) entry.getKey();
			// if there is already a piece in the coordinate we are trying to add one to
			if(next.equals(coordinate)) return true;
		}
		return false;
	}
	
	// determines how many of a particular piece type the given player has left to put on the board
	private int playerPieceTypeRemaining(HantoPlayerColor player, HantoPieceType pieceType) {
		if (player == HantoPlayerColor.BLUE) {
			return bluePiecesLeft.get(pieceType);
		}
		else {
			return redPiecesLeft.get(pieceType);
		}
	}
	
	// given a HantoPlayerColor, this method determines if that player has placed his/her Butterfly yet
	private boolean playerPlacedButterfly(HantoPlayerColor player) {
		boolean hasButterfly = true;
		if (player == HantoPlayerColor.BLUE) {
			if (bluePiecesLeft.get(HantoPieceType.BUTTERFLY) > 0) hasButterfly = false;
		}
		
		else {
			if (redPiecesLeft.get(HantoPieceType.BUTTERFLY) > 0) hasButterfly = false;
		}
		
		return hasButterfly;
	}
	
	/**
	 * The board's toString method will return a string representing the current state of the board.
	 * The string lists each piece on the board along with its corresponding coordinates.
	 */
	public String toString() {
		String result = "";
		
		Iterator<Entry<HantoCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		HantoPiece piece;
		PieceCoordinate coordinate;
		String color;
		while(pieces.hasNext()) {
			Entry<HantoCoordinate, HantoPiece> entry = pieces.next();
			coordinate = (PieceCoordinate) entry.getKey();
			piece = board.get(coordinate);
			color = piece.getColor() == HantoPlayerColor.RED ? "RED" : "BLUE";
			result += piece.getType().getPrintableName() + " " + color + " " + coordinate.toString() + "\n";
		}
		return result;
	}

}
