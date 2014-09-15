/**
 * 
 */
package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * @author Andy
 *
 */
public abstract class HantoGamePiece implements HantoPiece {
	
	protected HantoPieceType type;
	protected HantoPlayerColor color;

	@Override
	public HantoPlayerColor getColor() {
		return color;
	}

	@Override
	public HantoPieceType getType() {
		return type;
	}

}
