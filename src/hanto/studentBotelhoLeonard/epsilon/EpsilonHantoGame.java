/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * Implementation of HantoGame for the Delta version of Hanto.
 */

package hanto.studentBotelhoLeonard.epsilon;

import java.util.HashMap;

import hanto.common.HantoGame;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentBotelhoLeonard.common.BaseHantoGame;
import hanto.studentBotelhoLeonard.common.MoveType;
import hanto.studentBotelhoLeonard.common.MoveValidator;

/**
 * Implementation of HantoGame for the Delta version of Hanto.
 */
public class EpsilonHantoGame extends BaseHantoGame implements HantoGame {

	public EpsilonHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		
		bluePiecesLeft = new HashMap<HantoPieceType, Integer>();
		bluePiecesLeft.put(HantoPieceType.BUTTERFLY, 1);
		bluePiecesLeft.put(HantoPieceType.SPARROW, 2);
		bluePiecesLeft.put(HantoPieceType.CRAB, 6);
		bluePiecesLeft.put(HantoPieceType.HORSE, 4);
		redPiecesLeft = new HashMap<HantoPieceType, Integer>(bluePiecesLeft);
				
		pieceAbilities = new HashMap<HantoPieceType, MoveValidator>();
		pieceAbilities.put(HantoPieceType.BUTTERFLY, moveValidatorFactory.makeMoveValidator(MoveType.WALK, 1, board));
		pieceAbilities.put(HantoPieceType.CRAB, moveValidatorFactory.makeMoveValidator(MoveType.WALK, 1, board));
		pieceAbilities.put(HantoPieceType.SPARROW, moveValidatorFactory.makeMoveValidator(MoveType.FLY, 5, board));
		pieceAbilities.put(HantoPieceType.HORSE, moveValidatorFactory.makeMoveValidator(MoveType.JUMP, board));
		
		turnLimit = Integer.MAX_VALUE;
	}

}
