/**
 * 
 */
package epsilon;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static org.junit.Assert.*;
import static hanto.common.HantoPlayerColor.*;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.HantoGameFactory;
import hanto.studentBotelhoLeonard.common.FlyValidator;
import hanto.studentBotelhoLeonard.common.HantoBoard;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;
import hanto.studentBotelhoLeonard.common.pieces.Butterfly;
import hanto.studentBotelhoLeonard.common.pieces.Sparrow;

import org.junit.Before;
import org.junit.Test;

import common.HantoTestGame;
import common.HantoTestGameFactory;
import common.HantoTestGame.PieceLocationPair;

/**
 * @author Andrew Leonard
 *
 */
public class EpsilonTests {

	/**
	 * 
	 */

	private HantoGame game;
	private HantoTestGame testGame;

	@Before
	public void setup()
	{
		// By default, blue moves first.
		testGame = HantoTestGameFactory.getInstance().makeHantoTestGame(HantoGameID.EPSILON_HANTO);
		game = testGame;
	}

	
	@Test
	public void legalJumpTest() throws HantoException {
		testGame.setTurnNumber(10);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, BUTTERFLY, new PieceCoordinate(0, 0)),
						new PieceLocationPair(RED, BUTTERFLY, new PieceCoordinate(0, 1)),
						new PieceLocationPair(RED, SPARROW, new PieceCoordinate(1, 0)),
						new PieceLocationPair(RED, SPARROW, new PieceCoordinate(-1, 0)),
						new PieceLocationPair(RED, CRAB, new PieceCoordinate(-1, 1)),
						new PieceLocationPair(BLUE, HORSE, new PieceCoordinate(0,-1))
				}
				);

		testGame.setPlayerMoving(BLUE);

		MoveResult mv = game.makeMove(HORSE, new PieceCoordinate(0, -1), new PieceCoordinate(0, 2));
		assertEquals(OK, mv);
	}
	
	@Test(expected=HantoException.class)
	public void illegalJumpTest() throws HantoException {
		testGame.setTurnNumber(10);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, BUTTERFLY, new PieceCoordinate(0, 0)),
						new PieceLocationPair(RED, BUTTERFLY, new PieceCoordinate(0, 1)),
						new PieceLocationPair(RED, SPARROW, new PieceCoordinate(1, 0)),
						new PieceLocationPair(RED, SPARROW, new PieceCoordinate(-1, 0)),
						new PieceLocationPair(RED, CRAB, new PieceCoordinate(-1, 1)),
						new PieceLocationPair(BLUE, HORSE, new PieceCoordinate(0,-1))
				}
				);

		testGame.setPlayerMoving(BLUE);

		MoveResult mv = game.makeMove(HORSE, new PieceCoordinate(0, -1), new PieceCoordinate(-2, 2));
		assertEquals(OK, mv);
	}


}
