/**
 * 
 */
package common;

import hanto.common.HantoPlayerColor;
import hanto.studentBotelhoLeonard.delta.DeltaHantoGame;

/**
 * @author Andrew Leonard
 *
 */
public class DeltaHantoTestGame extends DeltaHantoGame implements HantoTestGame {

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
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see common.HantoTestGame#setTurnNumber(int)
	 */
	@Override
	public void setTurnNumber(int turnNumber) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see common.HantoTestGame#setPlayerMoving(hanto.common.HantoPlayerColor)
	 */
	@Override
	public void setPlayerMoving(HantoPlayerColor player) {
		// TODO Auto-generated method stub

	}

}
