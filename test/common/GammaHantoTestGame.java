package common;

import hanto.common.HantoException;
import hanto.common.HantoPiece;
import hanto.common.HantoPlayerColor;
import hanto.studentBotelhoLeonard.common.HantoPieceFactory;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;
import hanto.studentBotelhoLeonard.gamma.GammaHantoGame;

public class GammaHantoTestGame extends GammaHantoGame implements HantoTestGame {

	public GammaHantoTestGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
	}

	@Override
	public void initializeBoard(PieceLocationPair[] initialPieces) {
		board.getBoardMap().clear();
		for (int i = 0; i < initialPieces.length; i++) {
			PieceLocationPair givenPieceLoc = initialPieces[i];
			PieceCoordinate tempCoord = new PieceCoordinate(givenPieceLoc.location);
			HantoPiece tempPiece = null;
			try {
				tempPiece = HantoPieceFactory.makePiece(givenPieceLoc.pieceType, givenPieceLoc.player);
			} catch (HantoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			board.getBoardMap().put(tempCoord, tempPiece);
			decrementPieceTypeForPlayer(givenPieceLoc.player, givenPieceLoc.pieceType);
		}
	}

	@Override
	public void setTurnNumber(int turnNumber) {
		turnCount = (turnNumber*2) - 1; // our turn count counts the first players second turn as turn 2. (0->1->2)
	}

	@Override
	public void setPlayerMoving(HantoPlayerColor player) {
		if (movesFirst == player) turnCount--; // Since turns are 1 turn for both players, then setting movesFirst determines player moving.
	}

}
