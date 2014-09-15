package beta;

import static org.junit.Assert.*;


import hanto.HantoGameFactory;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentBotelhoLeonard.beta.BetaHantoGame;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;
import hanto.studentBotelhoLeonard.common.Sparrow;

import org.junit.Before;
import org.junit.Test;

public class BetaTests {
	
	private HantoGame game;
	
	@Before
	public void setup()	{
		game = HantoGameFactory.makeHantoGame(HantoGameID.BETA_HANTO);
	}
	
	@Test
	public void makeBetaGame() {
		assertTrue(game instanceof BetaHantoGame);
	}

	@Test
	public void createBetaGame() {
		assertNotNull(game);
	}
	
	@Test
	public void canMakeSparrows() {
		Sparrow sparrow = new Sparrow(HantoPlayerColor.BLUE);
		assertNotNull(sparrow);
		assertEquals(HantoPlayerColor.BLUE, sparrow.getColor());
		assertEquals(HantoPieceType.SPARROW, sparrow.getType());
	}
	
	@Test
	public void canMoveButterfly() throws HantoException {
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0,0));
		HantoPiece piece = game.getPieceAt(new PieceCoordinate(0,0));
		assertEquals(HantoPieceType.BUTTERFLY, piece.getType());
	}
	
	@Test
	public void canMoveSparrow() throws HantoException {
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0,0));
		HantoPiece piece = game.getPieceAt(new PieceCoordinate(0,0));
		assertEquals(HantoPieceType.SPARROW, piece.getType());
	}
	
	@Test(expected=HantoException.class)
	public void cantMoveCrab() throws HantoException {
		game.makeMove(HantoPieceType.CRAB, null, new PieceCoordinate(0,0));
	}
	
	@Test
	public void firstPieceColorIsCorrect() throws HantoException {
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0,0));
		HantoPiece piece = game.getPieceAt(new PieceCoordinate(0,0));
		assertEquals(HantoPlayerColor.BLUE, piece.getColor());
		
		game = HantoGameFactory.makeHantoGame(HantoGameID.BETA_HANTO, HantoPlayerColor.RED);
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0,0));
		piece = game.getPieceAt(new PieceCoordinate(0,0));
		assertEquals(HantoPlayerColor.RED, piece.getColor());
	}
	
	@Test(expected=HantoException.class)
	public void blueAttemptsToPlaceButterflyAtWrongLocation() throws HantoException	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(-1, 1));
	}
	
	@Test
	public void redPlacesButterflyNextToBlueButterfly() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 1));
		final HantoPiece p = game.getPieceAt(new PieceCoordinate(0, 1));
		assertEquals(HantoPieceType.BUTTERFLY, p.getType());
		assertEquals(HantoPlayerColor.RED, p.getColor());
	}
	
	@Test(expected=HantoException.class)
	public void redPlacesButterflyNotNextToBlueButterfly() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(5, 5));
	}
	
	@Test(expected=HantoException.class)
	public void attemptToMoveRatherThanPlace() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, new PieceCoordinate(0, 1), new PieceCoordinate(0, 0));
	}

	@Test(expected=HantoException.class)
	public void bluePlacesTwoButterflies() throws HantoException 
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(1, 1));
	}

}
