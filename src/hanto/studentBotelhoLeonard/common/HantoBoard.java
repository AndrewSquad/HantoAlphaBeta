/**
 * 
 */
package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoCoordinate;
import hanto.common.HantoPiece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrew & Andrew || Botelho & Leonard
 *
 */
public class HantoBoard {

	private Map<HantoCoordinate, HantoPiece> board; 

	public HantoBoard() {
		board = new HashMap<HantoCoordinate, HantoPiece>();
	}
	
	/**
	 * @return the board
	 */
	public Map<HantoCoordinate, HantoPiece> getBoard() {
		return board;
	}

	public void addPiece(HantoCoordinate coordinate, HantoPiece piece) {
		board.put(coordinate, piece);
	}

	public HantoPiece getPieceAt(HantoCoordinate coordinate) {
		return board.get(coordinate);
	}

}
