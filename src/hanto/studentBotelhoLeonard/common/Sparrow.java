/**
 * 
 */
package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * @author Andy Botelho and Andrew Leonard
 * This is the class for Sparrow HantoPieces. 
 * It extends the HantoGamePiece abstract class.
 */
public class Sparrow extends HantoGamePiece {

	/**
	 * Parameterized constructor for a Sparrow
	 * @param color the color of the player who owns to piece.
	 */
	public Sparrow(HantoPlayerColor color) {
		type = HantoPieceType.SPARROW;
		this.color = color;
	}

}
