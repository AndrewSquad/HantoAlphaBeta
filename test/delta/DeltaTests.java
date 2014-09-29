package delta;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static org.junit.Assert.*;
import static hanto.common.HantoPlayerColor.*;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.HantoGameFactory;
import hanto.studentBotelhoLeonard.common.Butterfly;
import hanto.studentBotelhoLeonard.common.FlyValidator;
import hanto.studentBotelhoLeonard.common.HantoBoard;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;
import hanto.studentBotelhoLeonard.common.Sparrow;

import org.junit.Before;
import org.junit.Test;

public class DeltaTests {

	private HantoGame game;

	@Before
	public void setup()
	{
		// By default, blue moves first.
		game = HantoGameFactory.makeHantoGame(HantoGameID.DELTA_HANTO);
	}


	// This has made me realize we need a piece factory. 
	@Test
	public void oneMoveIsOK() throws HantoException {
		MoveResult mv = game.makeMove(CRAB, null, new PieceCoordinate(0, 0));
		assertEquals(OK, mv);
	}
	
	@Test
	public void testBlueResign() throws HantoException {
		MoveResult mv = game.makeMove(null, null, null);
		assertEquals(RED_WINS, mv);
	}
	
	@Test
	public void testRedResign() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		MoveResult mv = game.makeMove(null, null, null);
		assertEquals(BLUE_WINS, mv);
	}

	@Test(expected=HantoException.class)
	public void moveAfterResignation() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(null, null, null);
		game.makeMove(SPARROW, null, new PieceCoordinate(0, 1));
	}
	
	@Test
	public void testFlyValidator() throws HantoException {
		HantoBoard board = new HantoBoard();
		FlyValidator flyValidator = new FlyValidator(2, board);
		assertFalse(flyValidator.isMoveLegal(new PieceCoordinate(0, 0), new PieceCoordinate(0, 1)));
		
		board.addPiece(new PieceCoordinate(0,0), new Butterfly(BLUE));
		board.addPiece(new PieceCoordinate(0,1), new Sparrow(RED));
		assertTrue(flyValidator.isMoveLegal(new PieceCoordinate(0, 0), new PieceCoordinate(0, 2)));
		assertFalse(flyValidator.isMoveLegal(new PieceCoordinate(0,0), new PieceCoordinate(-2, 0)));
	}
	
	@Test
	public void blueSparrowFlies() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(0, -1));
		game.makeMove(CRAB, null, new PieceCoordinate(-1, 2));
		game.makeMove(SPARROW, new PieceCoordinate(0, -1), new PieceCoordinate(0, 2));
	}
	
	@Test(expected=HantoException.class)
	public void cantMakeHorse() throws HantoException {
		game.makeMove(HORSE, null, new PieceCoordinate(0, 0));
	}

}
