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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class HantoBoard {

	private Map<HantoCoordinate, HantoPiece> board;
	private Map<HantoPieceType, Integer> bluePiecesLeft;
	private Map<HantoPieceType, Integer> redPiecesLeft;
	
	
	/**
	 * 
	 * @param pieceLimits
	 */
	public HantoBoard(Map<HantoPieceType, Integer> pieceLimits) {
		board = new HashMap<HantoCoordinate, HantoPiece>();
		
		bluePiecesLeft = new HashMap<HantoPieceType, Integer>(pieceLimits);
		redPiecesLeft = new HashMap<HantoPieceType, Integer>(pieceLimits);
	}

	
	/**
	 * @return the board
	 */
	public Map<HantoCoordinate, HantoPiece> getBoard() {
		return board;
	}
	
	/**
	 * 
	 * @param coordinate
	 * @return
	 */
	public HantoPiece getPieceAt(HantoCoordinate coordinate) {
		return board.get(coordinate);
	}

	
	/**
	 * 
	 * @param coordinate
	 * @param piece
	 * @param turnCount
	 * @throws HantoException
	 */
	public void addPiece(HantoCoordinate coordinate, HantoPiece piece, int turnCount) throws HantoException{
		if (!validateNewPieceAdd(coordinate, piece, turnCount)) {
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
	 * 
	 * @param coordinate
	 * @param piece
	 * @param turnCount
	 * @return
	 */
	public boolean validateNewPieceAdd(HantoCoordinate coordinate, HantoPiece piece, int turnCount) {
		// if first turn, piece must be put in center of board
		if (turnCount == 0 && coordinate.getX() == 0 && coordinate.getY() == 0) return true;

		// otherwise, a new piece must be adjacent to an existing piece
		return isAdjacentToAnyPiece(coordinate, piece) && !isTileAlreadyOccupied(coordinate, piece);
	}
	
	
	/**
	 * Determines if either player has anymore pieces left to put on the board.
	 * @return boolean indicating if more pieces can be played
	 */
	public boolean anyPiecesLeftToPlay() {
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
	
	/**
	 * 
	 * @param color player color
	 * @return true if given player has lost
	 */
	public boolean checkIfLost(HantoPlayerColor color) {
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
	
	
	private boolean isAdjacentToAnyPiece(HantoCoordinate coordinate, HantoPiece piece) {
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
	
	
	private boolean isTileAlreadyOccupied(HantoCoordinate coordinate, HantoPiece piece) {
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
	
	
	private int playerPieceTypeRemaining(HantoPlayerColor player, HantoPieceType pieceType) {
		if (player == HantoPlayerColor.BLUE) {
			return bluePiecesLeft.get(pieceType);
		}
		else {
			return redPiecesLeft.get(pieceType);
		}
	}
	
	
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
