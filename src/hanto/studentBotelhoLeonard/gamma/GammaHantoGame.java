/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * HantoGame class for the Gamma version implementation of Hanto.
 */

package hanto.studentBotelhoLeonard.gamma;

import java.util.HashMap;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.common.BaseHantoGame;

public class GammaHantoGame extends BaseHantoGame implements HantoGame {
	
	public GammaHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		
		bluePiecesLeft = new HashMap<HantoPieceType, Integer>();
		bluePiecesLeft.put(HantoPieceType.BUTTERFLY, 1);
		bluePiecesLeft.put(HantoPieceType.SPARROW, 5);
		redPiecesLeft = new HashMap<HantoPieceType, Integer>(bluePiecesLeft);
	}

	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {
		// TODO Auto-generated method stub
		return null;
	}


}
