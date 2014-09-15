package beta;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;


import hanto.HantoGameFactory;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.beta.BetaHantoGame;
import hanto.studentBotelhoLeonard.common.Butterfly;
import hanto.studentBotelhoLeonard.common.HantoBoard;
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
		MoveResult mv = game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0,0));
		HantoPiece piece = game.getPieceAt(new PieceCoordinate(0,0));
		assertEquals(HantoPieceType.BUTTERFLY, piece.getType());
		assertEquals(MoveResult.OK, mv);
	}
	
	@Test
	public void canMoveSparrow() throws HantoException {
		MoveResult mv = game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0,0));
		HantoPiece piece = game.getPieceAt(new PieceCoordinate(0,0));
		assertEquals(HantoPieceType.SPARROW, piece.getType());
		assertEquals(MoveResult.OK, mv);
	}
	
	@Test(expected=HantoException.class)
	public void cantMoveCrab() throws HantoException {
		game.makeMove(HantoPieceType.CRAB, null, new PieceCoordinate(0,0));
	}
	
	@Test
	public void pieceColorIsCorrect() throws HantoException {
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
	public void putPieceOnSameTile() throws HantoException {
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, 0));
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
	
	@Test
	public void testBoardHasPiecesLeftToPlay() throws HantoException {
		Map<HantoPieceType, Integer> pieceLimits = new HashMap<HantoPieceType, Integer>();
		pieceLimits.put(HantoPieceType.BUTTERFLY, 1);
		HantoBoard board = new HantoBoard(pieceLimits);
		
		board.addPiece(new PieceCoordinate(0,0), new Butterfly(HantoPlayerColor.BLUE), 0);
		assertTrue(board.anyPiecesLeftToPlay());
		
		board.addPiece(new PieceCoordinate(0,1), new Butterfly(HantoPlayerColor.RED), 1);
		assertFalse(board.anyPiecesLeftToPlay());
	}
	
	@Test(expected=HantoException.class)
	public void noButterflyAfterThreeTurns() throws HantoException {
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(1, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(1, 2));
	}
	
	@Test
	public void testForDraw() throws HantoException {
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(1, 1));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(1, 2));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(2, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(2, 2));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(3, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(3, 2));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(3, 3));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(4, 2));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(4, 3));
		MoveResult mv = game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(4, 4));
		assertEquals(MoveResult.DRAW, mv);
	}


}
