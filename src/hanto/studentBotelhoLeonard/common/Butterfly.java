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
	/**
	 * 
	 */
	
	
	public Butterfly() {
		// TODO Auto-generated constructor stub
		type = HantoPieceType.BUTTERFLY;
	}
	

	@Override
	public HantoPlayerColor getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HantoPieceType getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

}
