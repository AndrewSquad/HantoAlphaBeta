/**
 * @author Andrew & Andrew || Botelho & Leonard
 * This is the HantoBoard class that manages if moves are valid and what is currently on the board.
 */
package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

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

	private Map<PieceCoordinate, HantoPiece> board;
	
	
	/**
	 * Constructor for a HantoBoard.  Initializes Maps used to keep track of which pieces are on 
	 * the board and how many pieces each player has placed.
	 * @param pieceLimits a Map that indicates how many of each HantoPieceType 
	 * a player can play throughout the game
	 */
	public HantoBoard() {
		board = new HashMap<PieceCoordinate, HantoPiece>();
	}

	
	/** A getter for the HashMap that maps each HantoCoordinate to which HantoPiece is at that coordinate.
	 * @return the board HashMap
	 */
	public Map<PieceCoordinate, HantoPiece> getBoard() {
		return board;
	}
	
	/**
	 * Looks at the HantoBoard's Hashmap and gets the HantoPiece object at the given HantoCoordinate.
	 * @param coordinate the HantoCoordinate to look at.
	 * @return the HantoPiece found at the given coordinate.
	 */
	public HantoPiece getPieceAt(PieceCoordinate coordinate) {
		return board.get(coordinate);
	}

	
	/**
	 * The method used for adding a piece to the HantoBoard.
	 * @param coordinate the HantoCoordinate where the piece will be placed
	 * @param piece the HantoPiece being placed
	 */
	public void addPiece(PieceCoordinate coordinate, HantoPiece piece) {
		board.put(coordinate, piece);
	}
	
	
	/**
	 * Given a particular player color, this method checks to see if that Butterfly is surrounded, i.e. the player has lost
	 * @param color - the color of the player we're checking
	 * @return a boolean indicating whether or not the given player has lost
	 */
	public boolean checkIfPlayerLost(HantoPlayerColor color) {
		HantoPiece tempPiece;
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		PieceCoordinate coordinate = null;
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			coordinate = entry.getKey();
			tempPiece = board.get(coordinate);
			if (tempPiece.getType() == HantoPieceType.BUTTERFLY && tempPiece.getColor() == color) {
				return has6Neighbors(coordinate);
			}
		}
		return false;
	}
	
	/**
	 * Determines if a given HantoCoordinate is adjacent to any already-placed HantoPieces
	 * @param coordinate the HantoCoordinate we are checking for
	 * @return a boolean indicating whether or not the given HantoCoordinate is adjacent to any pieces.
	 */
	public boolean isAdjacentToAnyPiece(PieceCoordinate coordinate) {
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		PieceCoordinate next;
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			next = entry.getKey();			
			if (next.isAdjacentTo(coordinate)) {
				return true;			 
			}
		}
		return false;
	}
	
	/**
	 * Determines if a given HantoCoordinate already has a piece on it
	 * @param coordinate the HantoCoordinate we are checking for
	 * @return a boolean indicating whether or not the given HantoCoordinate already has a piece on it
	 */
	public boolean isTileAlreadyOccupied(PieceCoordinate coordinate) {
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		PieceCoordinate next;
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			next = entry.getKey();
			// if there is already a piece in the coordinate we are trying to add one to
			if(next.equals(coordinate)) return true;
		}
		return false;
	}
	
	// determines if a particular PieceCoordinate is surrounded by 6 other occupied tiles/pieces
	private boolean has6Neighbors(PieceCoordinate butterflyPos) {
		int neighbors = 0;
		
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		PieceCoordinate next;
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			next = entry.getKey();
			if (next.isAdjacentTo(butterflyPos)) neighbors++;
		}
		
		return neighbors == 6;
	}
	
	/**
	 * The board's toString method will return a string representing the current state of the board.
	 * The string lists each piece on the board along with its corresponding coordinates.
	 */
	public String toString() {
		String result = "";
		
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		HantoPiece piece;
		PieceCoordinate coordinate;
		String color;
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			coordinate = entry.getKey();
			piece = board.get(coordinate);
			color = piece.getColor() == HantoPlayerColor.RED ? "RED" : "BLUE";
			result += piece.getType().getPrintableName() + " " + color + " " + coordinate.toString() + "\n";
		}
		return result;
	}

}
