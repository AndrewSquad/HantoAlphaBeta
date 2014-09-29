package delta;

import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import static org.junit.Assert.assertEquals;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.HantoGameFactory;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;

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

}
