/**
 * 
 */
package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoPiece;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Andrew & Andrew || Botelho & Leonard
 *
 */
public class HantoBoard {

	private Map<HantoCoordinate, HantoPiece> board;
	//private Set<HantoRules> rules;

	public HantoBoard() {
		board = new HashMap<HantoCoordinate, HantoPiece>();
	}
	
	/**
	 * @return the board
	 */
	public Map<HantoCoordinate, HantoPiece> getBoard() {
		return board;
	}

	public void addPiece(HantoCoordinate coordinate, HantoPiece piece, int turnCount) throws HantoException{
		if (!validateNewPieceAdd(coordinate, piece, turnCount)) throw new HantoException("Adding new piece must be next to piece of same color!");
		board.put(coordinate, piece);
	}

	public HantoPiece getPieceAt(HantoCoordinate coordinate) {
		return board.get(coordinate);
	}
	
	// Rules
	
	// Place a new piece next to your own unless its first turn
	public boolean validateNewPieceAdd(HantoCoordinate coordinate, HantoPiece piece, int turnCount) {
		PieceCoordinate origin = new PieceCoordinate(0, 0);
		boolean isAdjacent = origin.isAdjacentTo(new PieceCoordinate(coordinate));
		
		if (turnCount == 0 && coordinate.getX() == 0 && coordinate.getY() == 0) return true;
		else if (turnCount == 1 && isAdjacent) return true;
		else if (turnCount > 1) return addedNextToSameColor(coordinate, piece);
		return false;
	}

	
	private boolean addedNextToSameColor(HantoCoordinate coordinate, HantoPiece piece) {
		Iterator<Entry<HantoCoordinate, HantoPiece>> pieces = board.entrySet().iterator();
		PieceCoordinate next;
		while(pieces.hasNext()) {
			 next = (PieceCoordinate) pieces.next();
			 if (next.isAdjacentTo(coordinate)) {
				 HantoPiece otherPiece = board.get(next);
				 if ((otherPiece.getColor() == piece.getColor())) return true;
			 }
		}
		return false;
	}
}
