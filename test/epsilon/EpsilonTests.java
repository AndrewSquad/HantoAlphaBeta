/**
 * 
 */
package epsilon;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static org.junit.Assert.*;
import static hanto.common.HantoPlayerColor.*;
import static hanto.studentBotelhoLeonard.common.MoveType.*;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.common.HantoBoard;
import hanto.studentBotelhoLeonard.common.MoveValidator;
import hanto.studentBotelhoLeonard.common.MoveValidatorFactory;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;
import hanto.studentBotelhoLeonard.common.pieces.Butterfly;
import hanto.studentBotelhoLeonard.common.pieces.Crab;
import hanto.studentBotelhoLeonard.common.pieces.Horse;
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
	
	
	@Test
	public void legalMovesAvailable() throws HantoException {
		HantoBoard board = new HantoBoard();
		MoveValidatorFactory validatorFactory = MoveValidatorFactory.getInstance();
		board.addPiece(new PieceCoordinate(0, 0), new Butterfly(BLUE));
		board.addPiece(new PieceCoordinate(0, 1), new Butterfly(RED));
		board.addPiece(new PieceCoordinate(1, 0), new Sparrow(RED));
		board.addPiece(new PieceCoordinate(-1, 0), new Sparrow(RED));
		board.addPiece(new PieceCoordinate(-1, 1), new Crab(RED));
		board.addPiece(new PieceCoordinate(0, -1), new Horse(BLUE));
		
		MoveValidator validator = validatorFactory.makeMoveValidator(FLY, 5, board);
		assertTrue(validator.existsLegalMove(new PieceCoordinate(1, 0)));
		//butterfly walk
		validator = validatorFactory.makeMoveValidator(WALK, 1, board);
		assertTrue(validator.existsLegalMove(new PieceCoordinate(0, 1)));
		//horse
		validator = validatorFactory.makeMoveValidator(JUMP, board);
		assertTrue(validator.existsLegalMove(new PieceCoordinate(0, -1)));
	}
	
	
	@Test(expected=HantoException.class)
	public void illegalJumpest() throws HantoException {
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
