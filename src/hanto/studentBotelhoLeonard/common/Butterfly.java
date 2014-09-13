/**
 * 
 */
package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * @author Andrew & Andrew | Leonard and Botelho
 *
 */
public class Butterfly implements HantoPiece{
	private HantoPieceType type;
	private HantoPlayerColor color;
	/**
	 * Default constructor for the Butterfly piece.
	 */
	public Butterfly() {
		type = HantoPieceType.BUTTERFLY;
	}
	/**
	 * Parameterized constructor for 
	 * @param color the color of the player who owns to piece.
	 */
	public Butterfly(HantoPlayerColor color) {
		this.type = HantoPieceType.BUTTERFLY;
		this.color = color;
	}
	

	@Override
	public HantoPlayerColor getColor() {
		return this.color;
	}

	@Override
	public HantoPieceType getType() {
		return this.type;
	}
}
