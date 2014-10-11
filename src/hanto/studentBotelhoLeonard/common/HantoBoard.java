/**
 * @author Andrew & Andrew || Botelho & Leonard
 * This is the HantoBoard class that manages if moves are valid and what is currently on the board.
 */
package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;


/**
 * Class in charge of keeping track what pieces are on the board and at which location.
 * This class validates a potential move and keeps track of how many pieces each player can play during the game.
 * @author Andy Botelho and Andrew Leonard
 *
 */
public class HantoBoard {

	private Map<PieceCoordinate, HantoPiece> board;
	
	
	/**
	 * Constructor for a HantoBoard.
	 */
	public HantoBoard() {
		board = new HashMap<PieceCoordinate, HantoPiece>();
	}
	
	public HantoBoard(HantoBoard otherBoard) {
		board = new HashMap<PieceCoordinate, HantoPiece>(otherBoard.getBoardMap());
	}

	
	/** A getter for the HashMap that maps each HantoCoordinate to which HantoPiece is at that coordinate.
	 * @return the board HashMap
	 */
	public Map<PieceCoordinate, HantoPiece> getBoardMap() {
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
	 * The method used for moving an existing piece to a different coordinate on the HantoBoard.
	 * @param from the original coordinate of the piece
	 * @param to the new coordinate of the piece
	 * @param piece the piece we are moving
	 */
	public void moveExistingPiece(PieceCoordinate from, PieceCoordinate to, HantoPiece piece) {
		board.remove(from);
		board.put(to, piece);
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
	 * Determines if a given HantoCoordinate is adjacent to any already-placed HantoPieces of the same color
	 * @param coordinate the HantoCoordinate we are checking for
	 * @param color - the color that the piece must be adjacent to for this method to return true
	 * @return a boolean indicating whether or not the given HantoCoordinate is adjacent to any pieces of the same color.
	 */
	public boolean isAdjacentSameColorPiece(PieceCoordinate coordinate, HantoPlayerColor color) {
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		PieceCoordinate next;
		HantoPlayerColor pieceColor;
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			next = entry.getKey();
			pieceColor = entry.getValue().getColor();
			if (next.isAdjacentTo(coordinate) && color == pieceColor) {
				return true;			 
			}
		}
		return false;
	}
	
	/**
	 * Determines if a given HantoCoordinate is adjacent to any already-placed HantoPieces of the opposite color
	 * @param coordinate the HantoCoordinate we are checking for
	 * @param color - the color that given coordinate cannot be adjacent to
	 * @return a boolean indicating whether or not the given HantoCoordinate is adjacent to any pieces.
	 */
	public boolean isAdjacentToOtherColorPiece(PieceCoordinate coordinate, HantoPlayerColor color) {
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		PieceCoordinate next;
		HantoPlayerColor pieceColor;
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			next = entry.getKey();
			pieceColor = entry.getValue().getColor();
			if (next.isAdjacentTo(coordinate) && color != pieceColor) {
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
	
	
	/**
	 * Returns a list of coordinates that the given coordinate can move to.
	 * A tile that a coordinate can move to must be adjacent to another open tile.
	 * @param coordinate the coordinate we are trying to move from
	 * @return the list of coordinates that the move can go through
	 */
	public List<PieceCoordinate> getTwoTileOpenings(PieceCoordinate coordinate) {
		
		List<PieceCoordinate> openTiles = new ArrayList<PieceCoordinate>();
		List<PieceCoordinate> twoTileOpenings = new ArrayList<PieceCoordinate>();
		
		for (PieceCoordinate adjacentCoordinate : coordinate.getSixAdjacentCoordinates()) { // for each of the six adjacent coordinates
			if (!isTileAlreadyOccupied(adjacentCoordinate)) { // if the coordinate is unoccupied
				openTiles.add(adjacentCoordinate); // add to openTiles list
			}
		}
		
		// create a list of all the open tiles that are adjacent to another open tile
		for (PieceCoordinate i : openTiles) {
			for (PieceCoordinate j : openTiles) {
				if (i.isAdjacentTo(j) && !twoTileOpenings.contains(i)) {
					twoTileOpenings.add(i);
				}
			}
		}
		
		return twoTileOpenings;
	}
	
	
	private List<PieceCoordinate> getOpenTiles(PieceCoordinate coordinate) {
		List<PieceCoordinate> openTiles = new ArrayList<PieceCoordinate>();
		for (PieceCoordinate adjacentCoordinate : coordinate.getSixAdjacentCoordinates()) { // for each of the six adjacent coordinates
			if (!isTileAlreadyOccupied(adjacentCoordinate)) { // if the coordinate is unoccupied
				openTiles.add(adjacentCoordinate); // add to openTiles list
			}
		}
		
		return openTiles;
	}
	
	
	/**
	 * Determines whether or not all pieces on the board are contiguous to each other.
	 * @return boolean indicating whether or not the board is currently contiguous.
	 */
	public boolean isBoardContiguous() {
		int pieceCount = board.size();
		boolean isContiguous;
		Queue<PieceCoordinate> visited = new LinkedList<PieceCoordinate>();
		LinkedList<PieceCoordinate> toVisit = new LinkedList<PieceCoordinate>();
		PieceCoordinate startingCoord = board.keySet().iterator().next(); // any coordinate on board
		
		toVisit.add(startingCoord);
		
		PieceCoordinate currentCoord;
		while (toVisit.size() > 0) {
			currentCoord = toVisit.pop();
			// if any adjacent coordinate has piece on it, add the coordinate to the toVisit list
			for (PieceCoordinate neighbor : currentCoord.getSixAdjacentCoordinates()) {
				if (isTileAlreadyOccupied(neighbor) && !visited.contains(neighbor) && !toVisit.contains(neighbor)){
					toVisit.add(neighbor);
				}
			}
			// currentCoord can now go to the visited list
			visited.add(currentCoord);
		}
		
		isContiguous = (pieceCount == visited.size())? true : false;
		
		return isContiguous;
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
	
	
	public boolean canPlayerPlacePiece(HantoPlayerColor color) {
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		PieceCoordinate next;
		HantoPiece piece;
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			piece = entry.getValue();
			if (piece.getColor() != color) continue;
			next = entry.getKey();
			if (pieceCanBePlacedNextTo(next, color)) return true;
		}
		return false;
	}
	

	private boolean pieceCanBePlacedNextTo(PieceCoordinate coord, HantoPlayerColor color) {
		List<PieceCoordinate> adjCoordinates = getOpenTiles(coord);
		if (adjCoordinates.size() == 0) return false;
		
		for (PieceCoordinate adjCoordinate : adjCoordinates) {
			if (!isAdjacentToOtherColorPiece(adjCoordinate, color)) return true;
		}
		
		return false;
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
