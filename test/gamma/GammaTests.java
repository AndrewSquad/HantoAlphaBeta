package gamma;

import static org.junit.Assert.*;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.HantoGameFactory;
import hanto.studentBotelhoLeonard.common.Butterfly;
import hanto.studentBotelhoLeonard.common.HantoBoard;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;
import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;
import static hanto.common.MoveResult.*;


import org.junit.Before;
import org.junit.Test;

public class GammaTests {
	
	private HantoGame game;

	
	@Before
	public void setup()
	{
		// By default, blue moves first.
		game = HantoGameFactory.makeHantoGame(HantoGameID.GAMMA_HANTO);
	}
	
	@Test
	public void oneMoveIsOK() throws HantoException {
		MoveResult mv = game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		assertEquals(OK, mv);
	}

	@Test
	public void SecondPlayerPlaysProperColor() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		HantoPiece secondPiece = game.getPieceAt(new PieceCoordinate(0, 1));
		assertEquals(RED, secondPiece.getColor());
	}
	
	@Test(expected=HantoException.class)
	public void secondPieceNotAdjacent() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 2));
	}
	
	@Test
	public void firstPlayerMakesValidSecondMove() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		MoveResult mv = game.makeMove(SPARROW, null, new PieceCoordinate(0, -1));
		assertEquals(OK, mv);
	}
	
	@Test
	public void testAdjacentToSameColor() {
		HantoBoard board = new HantoBoard();
		board.addPiece(new PieceCoordinate(0,0), new Butterfly(BLUE));
		assertTrue(board.isAdjacentSameColorPiece(new PieceCoordinate(0,1), BLUE));
	}
	
	@Test
	public void testNotAdjacentToSameColor() {
		HantoBoard board = new HantoBoard();
		board.addPiece(new PieceCoordinate(0,0), new Butterfly(BLUE));
		board.addPiece(new PieceCoordinate(0,1), new Butterfly(RED));
		assertFalse(board.isAdjacentToOtherColorPiece(new PieceCoordinate(0,-1), BLUE));
	}
	
	@Test(expected=HantoException.class)
	public void FirstPlayerPlacesPieceAdjacentToBothColors() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(1, 0));
	}
	
	@Test(expected=HantoException.class)
	public void FirstPlayerPlacesPieceLeftAdjacentToBothColors() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(-1, -1));
	}
	
	@Test
	public void secondPlayerMakesValidSecondMove() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(1, -1));
		MoveResult mv = game.makeMove(SPARROW, null, new PieceCoordinate(-1, 2));
		assertEquals(OK, mv);
	}
	
	@Test(expected=HantoException.class)
	public void secondPlayerMakesInvalidSecondMove() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(1, -1));
		game.makeMove(SPARROW, null, new PieceCoordinate(1, 0));
	}

	
	

}
