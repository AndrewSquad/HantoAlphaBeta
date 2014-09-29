package hanto.studentBotelhoLeonard.delta;

import java.util.HashMap;

import hanto.common.HantoGame;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentBotelhoLeonard.common.BaseHantoGame;
import hanto.studentBotelhoLeonard.common.FlyValidator;
import hanto.studentBotelhoLeonard.common.MoveValidator;
import hanto.studentBotelhoLeonard.common.WalkValidator;

public class DeltaHantoGame extends BaseHantoGame implements HantoGame {

	public DeltaHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		
		bluePiecesLeft = new HashMap<HantoPieceType, Integer>();
		bluePiecesLeft.put(HantoPieceType.BUTTERFLY, 1);
		bluePiecesLeft.put(HantoPieceType.SPARROW, 4);
		bluePiecesLeft.put(HantoPieceType.CRAB, 4);
		redPiecesLeft = new HashMap<HantoPieceType, Integer>(bluePiecesLeft);
				
		pieceAbilities = new HashMap<HantoPieceType, MoveValidator>();
		pieceAbilities.put(HantoPieceType.BUTTERFLY, new WalkValidator(1, board));
		pieceAbilities.put(HantoPieceType.CRAB, new WalkValidator(1, board));
		pieceAbilities.put(HantoPieceType.SPARROW, new FlyValidator(board));
		
		turnLimit = Integer.MAX_VALUE;
	}

}
