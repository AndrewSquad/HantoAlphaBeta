/**
 * 
 */
package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * @author Andrew & Andrew | Leonard and Botelho
 *
 */
public class Butterfly extends HantoGamePiece {


	/**
	 * Parameterized constructor for 
	 * @param color the color of the player who owns to piece.
	 */
	public Butterfly(HantoPlayerColor color) {
		type = HantoPieceType.BUTTERFLY;
		this.color = color;
	}
	

}