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
import hanto.common.HantoPrematureResignationException;
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
	private PieceCoordinate origin;
	
	@Before
	public void setup()
	{
		// By default, blue moves first.
		testGame = HantoTestGameFactory.getInstance().makeHantoTestGame(HantoGameID.EPSILON_HANTO);
		game = testGame;
		origin = new PieceCoordinate(0, 0);
	}

	
	@Test
	public void legalJumpTest() throws HantoException {
		testGame.setTurnNumber(10);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, BUTTERFLY, origin),
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
						new PieceLocationPair(BLUE, BUTTERFLY, origin),
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
	public void validJumps() throws HantoException {
		testGame.setTurnNumber(10);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, BUTTERFLY, origin),
						new PieceLocationPair(RED, BUTTERFLY, new PieceCoordinate(0, 1)),
						new PieceLocationPair(BLUE, HORSE, new PieceCoordinate(1, 0)),
						new PieceLocationPair(RED, CRAB, new PieceCoordinate(1, -1)),
						new PieceLocationPair(RED, CRAB, new PieceCoordinate(0, -1)),
						new PieceLocationPair(RED, HORSE, new PieceCoordinate(-1, 0)),
				}
				);
		testGame.setPlayerMoving(BLUE);
		MoveResult mv = game.makeMove(HORSE, new PieceCoordinate(1, 0), new PieceCoordinate(-2, 0));
		assertEquals(OK, mv);
		assertEquals(HORSE, game.getPieceAt(new PieceCoordinate(-2, 0)).getType());
		game.makeMove(CRAB, new PieceCoordinate(0, -1), new PieceCoordinate(-1, -1));
		mv = game.makeMove(HORSE, new PieceCoordinate(-2, 0), new PieceCoordinate(0, -2));
		assertEquals(OK, mv);
		assertEquals(HORSE, game.getPieceAt(new PieceCoordinate(0, -2)).getType());
	}
	
	@Test(expected=HantoException.class)
	public void invalidJump1() throws HantoException {
		testGame.setTurnNumber(10);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, BUTTERFLY, origin),
						new PieceLocationPair(RED, BUTTERFLY, new PieceCoordinate(0, 1)),
						new PieceLocationPair(BLUE, HORSE, new PieceCoordinate(1, 0)),
						new PieceLocationPair(RED, CRAB, new PieceCoordinate(1, -1)),
						new PieceLocationPair(RED, CRAB, new PieceCoordinate(0, -1)),
						new PieceLocationPair(RED, HORSE, new PieceCoordinate(-1, 0)),
				}
				);
		testGame.setPlayerMoving(BLUE);
		game.makeMove(HORSE, new PieceCoordinate(1, 0), new PieceCoordinate(-3, 0));
	}
	
	@Test(expected=HantoException.class)
	public void invalidJump2() throws HantoException {
		testGame.setTurnNumber(10);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, BUTTERFLY, origin),
						new PieceLocationPair(RED, BUTTERFLY, new PieceCoordinate(0, 1)),
						new PieceLocationPair(BLUE, HORSE, new PieceCoordinate(1, 0)),
						new PieceLocationPair(RED, CRAB, new PieceCoordinate(-1, 0))
				}
				);
		testGame.setPlayerMoving(BLUE);
		game.makeMove(HORSE, new PieceCoordinate(1, 0), new PieceCoordinate(-1, 0));
	}
	
	@Test(expected=HantoException.class)
	public void invalidJump3() throws HantoException {
		testGame.setTurnNumber(10);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, BUTTERFLY, origin),
						new PieceLocationPair(RED, BUTTERFLY, new PieceCoordinate(0, 1)),
						new PieceLocationPair(RED, HORSE, new PieceCoordinate(1, 0)),
						new PieceLocationPair(RED, CRAB, new PieceCoordinate(-1, 0))
				}
				);
		testGame.setPlayerMoving(BLUE);
		game.makeMove(BUTTERFLY, origin, new PieceCoordinate(-1, 1));
		game.makeMove(HORSE, new PieceCoordinate(1, 0), new PieceCoordinate(-2, 0));
	}	
	
	@Test
	public void horseCantJump() throws HantoException {
		HantoBoard board = new HantoBoard();
		MoveValidatorFactory validatorFactory = MoveValidatorFactory.getInstance();
		board.addPiece(origin, new Butterfly(BLUE));
		board.addPiece(new PieceCoordinate(0, 1), new Horse(RED));
		board.addPiece(new PieceCoordinate(0, 2), new Sparrow(BLUE));
		
		MoveValidator validator = validatorFactory.makeMoveValidator(JUMP, board);
		assertFalse(validator.existsLegalMove(new PieceCoordinate(0, 1)));
	}
	
	@Test
	public void loneHorseCantJump() throws HantoException {
		HantoBoard board = new HantoBoard();
		MoveValidatorFactory validatorFactory = MoveValidatorFactory.getInstance();
		board.addPiece(origin, new Horse(BLUE));
		
		MoveValidator validator = validatorFactory.makeMoveValidator(JUMP, board);
		assertFalse(validator.existsLegalMove(origin));
	}
	
	
	@Test
	public void legalMovesAvailable() throws HantoException {
		HantoBoard board = new HantoBoard();
		MoveValidatorFactory validatorFactory = MoveValidatorFactory.getInstance();
		board.addPiece(origin, new Butterfly(BLUE));
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
	
	@Test(expected=HantoPrematureResignationException.class)
	public void cantGiveupImmediately() throws HantoException {
		game.makeMove(null, null, null);
	}

	
	@Test
	public void testCanPlayerPlacePiece() {
		HantoBoard board = new HantoBoard();
		board.addPiece(origin, new Butterfly(BLUE));
		board.addPiece(new PieceCoordinate(0, 1), new Butterfly(RED));
		board.addPiece(new PieceCoordinate(1, 0), new Sparrow(BLUE));
		assertTrue(board.canPlayerPlacePiece(RED));		
	}
	
	@Test
	public void blueCantPlacePiece() {
		HantoBoard board = new HantoBoard();
		board.addPiece(origin, new Butterfly(BLUE));
		board.addPiece(new PieceCoordinate(0, 1), new Butterfly(RED));
		board.addPiece(new PieceCoordinate(1, 0), new Sparrow(RED));
		board.addPiece(new PieceCoordinate(1, -1), new Crab(RED));
		board.addPiece(new PieceCoordinate(0, -1), new Crab(RED));
		board.addPiece(new PieceCoordinate(-1, 0), new Crab(RED));
		board.addPiece(new PieceCoordinate(-1, 1), new Crab(RED));
		assertFalse(board.canPlayerPlacePiece(BLUE));
	}

	@Test(expected=HantoPrematureResignationException.class)
	public void redCantResign() throws HantoException {
		testGame.setTurnNumber(10);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, BUTTERFLY, origin),
						new PieceLocationPair(RED, BUTTERFLY, new PieceCoordinate(0, 1)),
						new PieceLocationPair(BLUE, CRAB, new PieceCoordinate(0, -1))
				}
				);
		testGame.setPlayerMoving(RED);
		game.makeMove(null, null, null);
	}
	
	@Test
	public void blueCanResign() throws HantoException {
		testGame.setTurnNumber(10);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, BUTTERFLY, origin),
						new PieceLocationPair(RED, BUTTERFLY, new PieceCoordinate(0, 1)),
						new PieceLocationPair(RED, CRAB, new PieceCoordinate(1, 0)),
						new PieceLocationPair(RED, CRAB, new PieceCoordinate(1, -1)),
						new PieceLocationPair(RED, CRAB, new PieceCoordinate(0, -1)),
						new PieceLocationPair(RED, CRAB, new PieceCoordinate(-1, 0)),
						new PieceLocationPair(RED, CRAB, new PieceCoordinate(1, -1))
				}
				);
		testGame.setPlayerMoving(BLUE);
		MoveResult mv = game.makeMove(null, null, null);
		assertEquals(mv, RED_WINS);
	}
	
	@Test(expected=HantoException.class)
	public void cantMakeCrane() throws HantoException {
		game.makeMove(CRANE, null, origin);
	}
	
	@Test
	public void jumpEdgeCase() throws HantoException {
		testGame.setTurnNumber(10);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, BUTTERFLY, origin),
						new PieceLocationPair(RED, BUTTERFLY, new PieceCoordinate(-1, 1)),
						new PieceLocationPair(RED, HORSE, new PieceCoordinate(-1, 0)),
						new PieceLocationPair(BLUE, HORSE, new PieceCoordinate(-1, 2))
				}
				);
		testGame.setPlayerMoving(BLUE);
		game.makeMove(HORSE, new PieceCoordinate(-1, 2), new PieceCoordinate(-1, -1));
	}
	
	@Test
	public void longDistance() {
		PieceCoordinate coord1 = new PieceCoordinate(-8, 10);
		PieceCoordinate coord2 = new PieceCoordinate(-4, 6);
		
		assertEquals(4, coord1.distanceFrom(coord2));
	}
}
