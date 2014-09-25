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
import hanto.studentBotelhoLeonard.common.MoveValidator;
import hanto.studentBotelhoLeonard.common.WalkValidator;


public class GammaHantoGame extends BaseHantoGame implements HantoGame {

	public GammaHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		
		turnLimit = 40; // game ends after 20th full turn

		bluePiecesLeft = new HashMap<HantoPieceType, Integer>();
		bluePiecesLeft.put(HantoPieceType.BUTTERFLY, 1);
		bluePiecesLeft.put(HantoPieceType.SPARROW, 5);
		redPiecesLeft = new HashMap<HantoPieceType, Integer>(bluePiecesLeft);
		
		pieceAbilities = new HashMap<HantoPieceType, MoveValidator>();
		pieceAbilities.put(HantoPieceType.BUTTERFLY, new WalkValidator(1, board));
		pieceAbilities.put(HantoPieceType.SPARROW, new WalkValidator(1, board));
		
	}
	
	
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {
		if (turnCount >= 40) throw new HantoException("Gamma Hanto can only last 20 full turns!");
		
		return super.makeMove(pieceType, from, to);
	}

}
