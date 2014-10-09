/**
 * 
 */
package hanto.studentBotelhoLeonard.common.pieces;

import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * @author Andrew Leonard and Andy Botelho 
 * This is the class for Sparrow HantoPieces. 
 * It extends the HantoGamePiece abstract class.
 */
public class Horse extends HantoGamePiece {

	/**
	 * Parameterized constructor for a Sparrow
	 * @param color the color of the player who owns to piece.
	 */
	public Horse(HantoPlayerColor color) {
		super(color);
		type = HantoPieceType.HORSE;
	}
}
