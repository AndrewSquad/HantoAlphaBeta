/**
 * 
 */
package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * @author Andy
 *
 */
public class Sparrow extends HantoGamePiece {

	/**
	 * Parameterized constructor for 
	 * @param color the color of the player who owns to piece.
	 */
	public Sparrow(HantoPlayerColor color) {
		this.type = HantoPieceType.SPARROW;
		this.color = color;
	}

}
