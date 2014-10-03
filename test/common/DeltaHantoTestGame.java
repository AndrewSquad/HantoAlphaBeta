/**
 * 
 */
package common;

import hanto.common.HantoException;
import hanto.common.HantoPiece;
import hanto.common.HantoPlayerColor;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;
import hanto.studentBotelhoLeonard.delta.DeltaHantoGame;

/**
 * @author Andrew Leonard
 *
 */
public class DeltaHantoTestGame extends DeltaHantoGame implements HantoTestGame {
	
	HantoPlayerColor setMoving = null;
	Boolean turnCountSet = false;

	/**
	 * @param movesFirst
	 */
	public DeltaHantoTestGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see common.HantoTestGame#initializeBoard(common.HantoTestGame.PieceLocationPair[])
	 */
	@Override
	public void initializeBoard(PieceLocationPair[] initialPieces) {
		board.getBoardMap().clear();
		setMoving = null;
		turnCountSet = false;
		for (int i = 0; i < initialPieces.length; i++) {
			PieceLocationPair givenPieceLoc = initialPieces[i];
			PieceCoordinate tempCoord = new PieceCoordinate(givenPieceLoc.location);
			HantoPiece tempPiece = null;
			try {
				tempPiece = pieceFactory.makePiece(givenPieceLoc.pieceType, givenPieceLoc.player);
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
		turnCount = (turnNumber*2) - 2; // our turn count counts the first players second turn as turn 2. (0->1->2)
		turnCountSet = true;
		if (setMoving != null) setPlayerMoving(setMoving);
	}

	@Override
	public void setPlayerMoving(HantoPlayerColor player) {
		if (!turnCountSet) {
			setMoving = player;
			if (player != movesFirst) {
				turnCount++;
			}
		}
		else if (movesFirst != player) turnCount++; // Since turns are 1 turn for both players, then setting movesFirst determines player moving.
	}

}
