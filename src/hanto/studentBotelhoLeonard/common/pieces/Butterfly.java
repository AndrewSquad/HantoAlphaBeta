/**
 * 
 */
package hanto.studentBotelhoLeonard.common.pieces;

import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * @author Andrew & Andrew | Leonard and Botelho
 * Class for a Butterfly game piece object.
 * This class extends the abstract class HantoGamePiece
 */
public class Butterfly extends HantoGamePiece {


	/**
	 * Parameterized constructor for the Butterfly
	 * @param color the color of the player who owns to piece.
	 */
	public Butterfly(HantoPlayerColor color) {
		type = HantoPieceType.BUTTERFLY;
		this.color = color;
	}
	

}