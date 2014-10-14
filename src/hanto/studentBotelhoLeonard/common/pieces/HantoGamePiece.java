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
		
	/**
	 * Abstract constructor.  Used by all subclasses to set the color of the piece.
	 * @param color - the HantoPlayerColor of the piece.
	 */
	protected HantoGamePiece(HantoPlayerColor color) {
		this.color = color;
	}

	@Override
	public HantoPlayerColor getColor() {
		return color;
	}

	@Override
	public HantoPieceType getType() {
		return type;
	}
	

}
