package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoException;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

public class HantoPieceFactory {
	
	private static final HantoPieceFactory instance = new HantoPieceFactory();
	
	private HantoPieceFactory() {}
	
	public static HantoPieceFactory getInstance() {
		return instance;
	}
	
	public static HantoPiece makePiece(HantoPieceType type, HantoPlayerColor color) throws HantoException {
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
