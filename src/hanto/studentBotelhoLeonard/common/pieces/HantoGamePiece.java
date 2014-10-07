/**
 * 
 */
package hanto.studentBotelhoLeonard.common.pieces;

import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * @author Andy Botelho and Andrew Leonard
 * Abstract class for game pieces in Hanto.
 * This abstract class implements the HantoPiece interface.
 * This abstract class was created because HantoPiece objects share a lot of attributes and behavior.
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
