/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * Implementation of HantoGame for the Delta version of Hanto.
 */

package hanto.studentBotelhoLeonard.delta;

import java.util.HashMap;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.common.BaseHantoGame;
import hanto.studentBotelhoLeonard.common.MoveType;
import hanto.studentBotelhoLeonard.common.MoveValidator;

/**
 * Implementation of HantoGame for the Delta version of Hanto.
 */
public class DeltaHantoGame extends BaseHantoGame implements HantoGame {

	public DeltaHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		
		bluePiecesLeft = new HashMap<HantoPieceType, Integer>();
		bluePiecesLeft.put(HantoPieceType.BUTTERFLY, 1);
		bluePiecesLeft.put(HantoPieceType.SPARROW, 4);
		bluePiecesLeft.put(HantoPieceType.CRAB, 4);
		redPiecesLeft = new HashMap<HantoPieceType, Integer>(bluePiecesLeft);
				
		pieceAbilities = new HashMap<HantoPieceType, MoveValidator>();
		pieceAbilities.put(HantoPieceType.BUTTERFLY, moveValidatorFactory.makeMoveValidator(MoveType.WALK, 1, board));
		pieceAbilities.put(HantoPieceType.CRAB, moveValidatorFactory.makeMoveValidator(MoveType.WALK, 1, board));
		pieceAbilities.put(HantoPieceType.SPARROW, moveValidatorFactory.makeMoveValidator(MoveType.FLY, board));
		
		turnLimit = Integer.MAX_VALUE;
	}
	
	
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {
		
		HantoPlayerColor color = whoseTurnIsIt();
		if (pieceType == null && from == null && to == null) {
			gameHasEnded = true;
			MoveResult result = (color == HantoPlayerColor.RED) ? MoveResult.BLUE_WINS: MoveResult.RED_WINS;
			return result;
		}
		
		if(pieceType != null && pieceType != HantoPieceType.BUTTERFLY && pieceType != HantoPieceType.SPARROW && pieceType != HantoPieceType.CRAB) throw new HantoException("Illegal piece type for Delta!");
		
		return super.makeMove(pieceType, from, to);
	}

}
