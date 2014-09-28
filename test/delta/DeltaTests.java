package delta;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.CRAB;
import static hanto.common.MoveResult.OK;
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
		game = HantoGameFactory.makeHantoGame(HantoGameID.GAMMA_HANTO);
	}


	// This has made me realize we need a piece factory. 
	@Test
	public void oneMoveIsOK() throws HantoException {
		MoveResult mv = game.makeMove(CRAB, null, new PieceCoordinate(0, 0));
		assertEquals(OK, mv);
	}


}
