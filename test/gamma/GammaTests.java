package gamma;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.HantoGameFactory;
import hanto.studentBotelhoLeonard.common.HantoBoard;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;
import hanto.studentBotelhoLeonard.common.pieces.Butterfly;
import hanto.studentBotelhoLeonard.common.pieces.Sparrow;
import static hanto.common.HantoPieceType.*;
import static hanto.common.HantoPlayerColor.*;
import static hanto.common.MoveResult.*;

import org.junit.Before;
import org.junit.Test;

import common.HantoTestGame;
import common.HantoTestGame.PieceLocationPair;
import common.HantoTestGameFactory;

public class GammaTests {

	private HantoGame game;
	private HantoTestGame testGame;

	@Before
	public void setup()
	{
		// By default, blue moves first.
		testGame = HantoTestGameFactory.getInstance().makeHantoTestGame(HantoGameID.GAMMA_HANTO);
		game = testGame;
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

	@Test
	public void testSixAdjacentCoordinates() {
		PieceCoordinate coordinate = new PieceCoordinate(0 , 0);
		List<PieceCoordinate> six = coordinate.getSixAdjacentCoordinates();
		assertTrue(six.contains(new PieceCoordinate(1, 0)));
		assertTrue(six.contains(new PieceCoordinate(0, -1)));
		assertFalse(six.contains(new PieceCoordinate(0, -2)));
	}

	@Test(expected=HantoException.class)
	public void playerCantMovePieceBeforePlacingButterfly() throws HantoException {
		game.makeMove(SPARROW, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, new PieceCoordinate(0, 0), new PieceCoordinate(1, 0));
	}

	@Test
	public void testTwoUnoccupiedTiles_1() {
		HantoBoard board = new HantoBoard();
		PieceCoordinate origin = new PieceCoordinate(0, 0);
		List<PieceCoordinate> openings = new ArrayList<PieceCoordinate>();
		board.addPiece(origin, new Sparrow(BLUE));
		assertNotNull(board.getTwoTileOpenings(origin));
		assertEquals(origin.getSixAdjacentCoordinates(), board.getTwoTileOpenings(origin));

		board.addPiece(new PieceCoordinate(0, 1), new Sparrow(BLUE));
		board.addPiece(new PieceCoordinate(1, 0), new Sparrow(BLUE));
		openings = board.getTwoTileOpenings(origin);
		assertFalse(openings.isEmpty());
		assertTrue(openings.contains(new PieceCoordinate(1, -1)));
		assertTrue(openings.contains(new PieceCoordinate(0, -1)));
		assertTrue(openings.contains(new PieceCoordinate(-1, 0)));
		assertTrue(openings.contains(new PieceCoordinate(-1, 1)));
		assertEquals(4, openings.size());

		board.addPiece(new PieceCoordinate(1, -1), new Sparrow(BLUE));
		board.addPiece(new PieceCoordinate(0, -1), new Sparrow(BLUE));
		openings = board.getTwoTileOpenings(origin);
		assertNotNull(board.getTwoTileOpenings(new PieceCoordinate(0, 0)));
		assertTrue(openings.contains(new PieceCoordinate(-1, 0)));
		assertTrue(openings.contains(new PieceCoordinate(-1, 1)));
		assertEquals(2, openings.size());

		board.addPiece(new PieceCoordinate(-1, 0), new Sparrow(BLUE));
		openings = board.getTwoTileOpenings(origin);
		assertEquals(0, openings.size());
	}

	@Test
	public void testTwoUnoccupiedTiles_2() {
		HantoBoard board = new HantoBoard();
		PieceCoordinate origin = new PieceCoordinate(0, 0);
		List<PieceCoordinate> openings = new ArrayList<PieceCoordinate>();
		board.addPiece(origin, new Sparrow(BLUE));
		assertNotNull(board.getTwoTileOpenings(origin));

		board.addPiece(new PieceCoordinate(0, 1), new Sparrow(BLUE));
		board.addPiece(new PieceCoordinate(-1, 0), new Sparrow(BLUE));
		openings = board.getTwoTileOpenings(origin);
		assertNotNull(board.getTwoTileOpenings(origin));
		assertTrue(openings.contains(new PieceCoordinate(1, -1)));
		assertTrue(openings.contains(new PieceCoordinate(0, -1)));
		assertTrue(openings.contains(new PieceCoordinate(1, 0)));
		assertEquals(3, openings.size());

		board.addPiece(new PieceCoordinate(1, -1), new Sparrow(BLUE));
		assertEquals(0, board.getTwoTileOpenings(origin).size());
	}

	@Test
	public void testCoordinateDistances() {
		PieceCoordinate coord0_0 = new PieceCoordinate(0, 0);
		PieceCoordinate coord1_1 = new PieceCoordinate(1, 1);
		PieceCoordinate coord2_2 = new PieceCoordinate(2, 2);
		PieceCoordinate coord3_4 = new PieceCoordinate(3, 4);
		PieceCoordinate coordneg2_0 = new PieceCoordinate(-2, 0);
		PieceCoordinate coordneg3_neg4 = new PieceCoordinate(-3, -4);
		PieceCoordinate coord3_neg4 = new PieceCoordinate(3, -4);
		PieceCoordinate coordneg3_4 = new PieceCoordinate(-3, 4);

		assertEquals(2, coord0_0.distanceFrom(coord1_1));
		assertEquals(11, coord2_2.distanceFrom(coordneg3_neg4));
		assertEquals(9, coordneg3_neg4.distanceFrom(coord1_1));
		assertEquals(7, coord0_0.distanceFrom(coord3_4));
		assertEquals(5, coordneg2_0.distanceFrom(coordneg3_neg4));
		assertEquals(4, coord3_neg4.distanceFrom(coord0_0));	
		assertEquals(8, coord3_neg4.distanceFrom(coordneg3_4));
		assertEquals(5, coord3_neg4.distanceFrom(coord1_1));
		assertEquals(4, coordneg3_4.distanceFrom(coord1_1));
		assertEquals(8, coord3_neg4.distanceFrom(coord3_4));	

	}


	@Test
	public void blueMovesButterfly() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		MoveResult mv = game.makeMove(BUTTERFLY, new PieceCoordinate(0, 0), new PieceCoordinate(1, 0));
		assertEquals(OK, mv);
		assertNull(game.getPieceAt(new PieceCoordinate(0, 0)));

		HantoPiece piece = game.getPieceAt(new PieceCoordinate(1, 0));
		assertEquals(BLUE, piece.getColor());
		assertEquals(BUTTERFLY, piece.getType());
	}

	@Test(expected=HantoException.class)
	public void blueMovesButterflyTooFar() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(BUTTERFLY, new PieceCoordinate(0, 0), new PieceCoordinate(2, 0));
	}


	@Test(expected=HantoException.class)
	public void blueMovesButterflyToSpaceNotAdjacentToAnything() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(BUTTERFLY, new PieceCoordinate(0, 0), new PieceCoordinate(0, -1));
	}

	@Test(expected=HantoException.class)
	public void blueBreaksContiguous() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(0, -1));
		game.makeMove(SPARROW, null, new PieceCoordinate(1, 1));
		game.makeMove(BUTTERFLY, new PieceCoordinate(0, 0), new PieceCoordinate(-1, 1));
	}

	@SuppressWarnings("static-access")
	@Test(expected=HantoException.class)
	public void illegal4thMove() throws HantoException {
		//First move
		game = HantoGameFactory.getInstance().makeHantoGame(HantoGameID.GAMMA_HANTO);
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		//Second
		game.makeMove(SPARROW, null, new PieceCoordinate(0, -1));
		game.makeMove(SPARROW, null, new PieceCoordinate(1, 1));
		//Third
		game.makeMove(SPARROW, null, new PieceCoordinate(0, -2));
		game.makeMove(SPARROW, null,new PieceCoordinate(0, 2));
		// Fourth
		game.makeMove(BUTTERFLY, new PieceCoordinate(0, 0), new PieceCoordinate(-1, 1));
		game.makeMove(SPARROW, new PieceCoordinate(1, 1), new PieceCoordinate(0, 1));//illegal
	}

	@Test(expected=HantoException.class)
	public void pieceTypeMismatchOnMovement() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, new PieceCoordinate(0, 0), new PieceCoordinate(1, 0));
	}

	@Test(expected=HantoException.class)
	public void blueSparrowMovesTooFar() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(0, -1));
		game.makeMove(SPARROW, null, new PieceCoordinate(-1, 2));
		game.makeMove(SPARROW, new PieceCoordinate(0, -1), new PieceCoordinate(-1, 1));
	}

	@Test
	public void blueSparrowMovesSuccessfully() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(0, -1));
		game.makeMove(SPARROW, null, new PieceCoordinate(-1, 2));
		MoveResult mv = game.makeMove(SPARROW, new PieceCoordinate(0, -1), new PieceCoordinate(-1, 0));
		assertEquals(OK, mv);
		HantoPiece piece = game.getPieceAt(new PieceCoordinate(0, -1));
		assertNull(piece);
		piece = game.getPieceAt(new PieceCoordinate(-1, 0));
		assertEquals(BLUE, piece.getColor());
		assertEquals(SPARROW, piece.getType());
	}

	@Test(expected=HantoException.class)
	public void cantMakeCrab() throws HantoException {
		game.makeMove(CRAB, null, new PieceCoordinate(0, 0));
	}

	@Test
	public void gameEndsInDrawAfterMaxTurns() throws HantoException {
		testGame.setTurnNumber(20);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, BUTTERFLY, new PieceCoordinate(0, 0)),
						new PieceLocationPair(RED, BUTTERFLY, new PieceCoordinate(0, 1)),
						new PieceLocationPair(BLUE, SPARROW, new PieceCoordinate(-1, 0)),
						new PieceLocationPair(RED, SPARROW, new PieceCoordinate(0, 2)),
				}
		);
		testGame.setPlayerMoving(RED);
		MoveResult mv = game.makeMove(SPARROW, null, new PieceCoordinate(0, 3));
		assertEquals(DRAW, mv);
	}
	
	@Test(expected=HantoException.class)
	public void onlyOneButterFlyAfterThreeTurns() throws HantoException {
		testGame.setTurnNumber(4);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, SPARROW, new PieceCoordinate(0, 0)),
						new PieceLocationPair(RED, SPARROW, new PieceCoordinate(0, 1)),
				}
		);
		
		game.makeMove(SPARROW, null, new PieceCoordinate(0, -1));
		
	}
	
	@Test(expected=HantoException.class)
	public void playerRunsOutOfPiece() throws HantoException {
		testGame.setTurnNumber(2);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, BUTTERFLY, new PieceCoordinate(0, 0)),
						new PieceLocationPair(RED, BUTTERFLY, new PieceCoordinate(0, 1)),
				}
		);
		testGame.setPlayerMoving(BLUE);
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(-1, 0));
	}
	
	@Test(expected=HantoException.class)
	public void CantResign() throws HantoException {
		game.makeMove(null, null, null);
	}
	
	@Test(expected=HantoException.class)
	public void needDestination() throws HantoException {
		game.makeMove(BUTTERFLY, null, null);
	}
	
	@Test(expected=HantoException.class)
	public void needToSpecifyPiece() throws HantoException {
		game.makeMove(null, null, new PieceCoordinate(0,0));
	}
	
	@Test
	public void playersRunOutOfPieces() throws HantoException {
		testGame.setTurnNumber(15);
		testGame.initializeBoard(
				new PieceLocationPair[] {
						new PieceLocationPair(BLUE, BUTTERFLY, new PieceCoordinate(0, 0)),
						new PieceLocationPair(RED, BUTTERFLY, new PieceCoordinate(0, 1)),
						new PieceLocationPair(BLUE, SPARROW, new PieceCoordinate(0, -1)),
						new PieceLocationPair(RED, SPARROW, new PieceCoordinate(0, 2)),
						new PieceLocationPair(BLUE, SPARROW, new PieceCoordinate(0, -2)),
						new PieceLocationPair(RED, SPARROW, new PieceCoordinate(0, 3)),
						new PieceLocationPair(BLUE, SPARROW, new PieceCoordinate(0, -3)),
						new PieceLocationPair(RED, SPARROW, new PieceCoordinate(0, 4)),
						new PieceLocationPair(BLUE, SPARROW, new PieceCoordinate(0, -4)),
						new PieceLocationPair(RED, SPARROW, new PieceCoordinate(0, 5)),
						new PieceLocationPair(BLUE, SPARROW, new PieceCoordinate(0, -5)),
				}
		);
		testGame.setPlayerMoving(RED);
		MoveResult mv = game.makeMove(SPARROW, null, new PieceCoordinate(0, 6));
		assertEquals(mv, DRAW);
	}
}

