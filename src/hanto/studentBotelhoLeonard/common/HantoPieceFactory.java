/**
 * @author Andy Botelho
 * @author Andrew Botelho
 * 
 * A Factory class for making different Hanto Pieces.
 */

package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoException;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * A Factory class for making different Hanto Pieces.
 */
public class HantoPieceFactory {
	
	private static final HantoPieceFactory instance = new HantoPieceFactory();
	
	private HantoPieceFactory() {}
	
	public static HantoPieceFactory getInstance() {
		return instance;
	}
	
	/**
	 * Factory method for creating a new HantoPiece
	 * @param type - the type of piece (Butterfly, Sparrow, etc.)
	 * @param color - the color of the piece
	 * @return the HantoPiece object that was created.
	 * @throws HantoException when an unrecognizable piece type is given.
	 */
	public HantoPiece makePiece(HantoPieceType type, HantoPlayerColor color) throws HantoException {
		HantoPiece thePiece = null;
		switch(type) {
		case BUTTERFLY:
			thePiece = new Butterfly(color);
			break;
		case SPARROW:
			thePiece = new Sparrow(color);
			break;
		case CRAB:
			thePiece = new Crab(color);
			break;
		default:
			throw new HantoException("Inavlid piece type specified. Cannot create piece of type " + type.toString() + ".");		
		}
		return thePiece;
	}

}
