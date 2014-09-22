package alpha;

import static org.junit.Assert.*;
import static hanto.common.HantoPieceType.*;
import static hanto.common.MoveResult.*;
import hanto.HantoGameFactory;
import hanto.common.*;
import org.junit.*;

public class AlphaHantoMasterTest
{
	
	/**
	 * Internal class for these test cases.
	 * @version Sep 13, 2014
	 */
	class TestHantoCoordinate implements HantoCoordinate
	{
		private final int x, y;
		
		public TestHantoCoordinate(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		/*
		 * @see hanto.common.HantoCoordinate#getX()
		 */
		@Override
		public int getX()
		{
			return x;
		}

		/*
		 * @see hanto.common.HantoCoordinate#getY()
		 */
		@Override
		public int getY()
		{
			return y;
		}

	}
	
	private HantoGame game;
	
	@Before
	public void setup()
	{
		game = HantoGameFactory.makeHantoGame(HantoGameID.ALPHA_HANTO);
	}
	
	@Test
	public void blueMakesValidFirstMove() throws HantoException
	{
		final MoveResult mr = game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		assertEquals(OK, mr);
	}
	
	@Test
	public void afterFirstMoveBlueButterflyIsAt0_0() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		final HantoPiece p = game.getPieceAt(new TestHantoCoordinate(0, 0));
		assertEquals(BUTTERFLY, p.getType());
		assertEquals(HantoPlayerColor.BLUE, p.getColor());
	}
	
	@Test(expected=HantoException.class)
	public void bluePlacesNonButterfly() throws HantoException
	{
		game.makeMove(SPARROW, null, new TestHantoCoordinate(0, 0));
	}
	
	@Test
	public void redPlacesButterflyNextToBlueButterfly() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 1));
		final HantoPiece p = game.getPieceAt(new TestHantoCoordinate(0, 1));
		assertEquals(BUTTERFLY, p.getType());
		assertEquals(HantoPlayerColor.RED, p.getColor());
	}
	
	@Test(expected=HantoException.class)
	public void blueAttemptsToPlaceButterflyAtWrongLocation() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1, 1));
	}
	
	@Test
	public void redMakesValidSecondMoveAndGameIsDrawn() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		final MoveResult mr = game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1, 1));
		assertEquals(MoveResult.DRAW, mr);
	}
	
	@Test(expected=HantoException.class)
	public void redPlacesButterflyNonAdjacentToBlueButterfly() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(2, 2));
	}
	
	@Test(expected=HantoException.class)
	public void attemptToMoveRatherThanPlace() throws HantoException
	{
		game.makeMove(BUTTERFLY, new TestHantoCoordinate(0, 1), new TestHantoCoordinate(0, 0));
	}
	
	/* Tests for coverage */
	@Test
	public void redValidMove2() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		final MoveResult mr = game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1, 0));
		assertEquals(MoveResult.DRAW, mr);
		game.getPrintableBoard();
	}
	
	@Test
	public void redValidMove3() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		final MoveResult mr = game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, -1));
		
		assertEquals(MoveResult.DRAW, mr);
	}
	
	@Test
	public void redValidMove4() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		final MoveResult mr = game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(-1, 1));
		assertEquals(MoveResult.DRAW, mr);
		final HantoPiece p = game.getPieceAt(new TestHantoCoordinate(-1, 1));
		assertEquals(BUTTERFLY, p.getType());
		assertEquals(HantoPlayerColor.RED, p.getColor());
	}
	
	@Test
	public void redValidMove5() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		final MoveResult mr = game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(1, 0));
		assertEquals(MoveResult.DRAW, mr);
	}
	
	@Test(expected=HantoException.class)
	public void redTriesToPlaceButterflyAt0_0() throws HantoException
	{
		game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new TestHantoCoordinate(0, 0));
	}
}
