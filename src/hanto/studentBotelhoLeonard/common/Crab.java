/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * Class for a Crab piece in Hanto.
 */

package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * Class for a Crab piece in Hanto.
 */
public class Crab extends HantoGamePiece {

	/**
	 * Constructor for a Crab.
	 * @param color the color of the Player using the Crab.
	 */
	public Crab(HantoPlayerColor color) {
		this.color = color; 
		type = HantoPieceType.CRAB;
	}

}
