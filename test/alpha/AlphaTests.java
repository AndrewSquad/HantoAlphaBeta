package alpha;

import static org.junit.Assert.*;
import hanto.HantoGameFactory;
import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.alpha.AlphaHantoGame;
import hanto.studentBotelhoLeonard.common.Butterfly;
import hanto.studentBotelhoLeonard.common.HantoBoard;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AlphaTests {
	// Test Butterfly exists
	private static HantoGameFactory factory;
	private HantoGame game;
	
	@BeforeClass
	public static void initializeClass() {
		factory = HantoGameFactory.getInstance();
	}

	@Before
	public void setup()	{
		game = HantoGameFactory.makeHantoGame(HantoGameID.ALPHA_HANTO);
	}

	@Test
	public void testButterflyExists() {		
		Butterfly butterfly = new Butterfly(HantoPlayerColor.BLUE);
		assertNotNull(butterfly);
	}

	@Test
	public void testButterflyIsTypeButterfly() {		
		Butterfly butterfly = new Butterfly(HantoPlayerColor.BLUE);
		assertEquals(HantoPieceType.BUTTERFLY, butterfly.getType());
	}

	@Test
	public void testButterflyPrintsProperly() {		
		Butterfly butterfly = new Butterfly(HantoPlayerColor.BLUE);
		assertEquals("Butterfly", butterfly.getType().getPrintableName());
	}

	@Test
	public void testButterflySymbolPrintsProperly() {		
		Butterfly butterfly = new Butterfly(HantoPlayerColor.BLUE);
		assertEquals("B", butterfly.getType().getSymbol());
	}

	@Test
	public void testButterflyIsBlue() {		
		Butterfly butterfly = new Butterfly(HantoPlayerColor.BLUE);
		assertEquals(HantoPlayerColor.BLUE, butterfly.getColor());
	}

	@Test
	public void testButterflyIsNotBlue() {		
		Butterfly butterfly = new Butterfly(HantoPlayerColor.RED);
		assertNotEquals(HantoPlayerColor.BLUE, butterfly.getColor());
	}


	// Test putting butterfly on board
	// First coordinates.
	@Test
	public void testCoordinatesExist() {
		PieceCoordinate destinationCoord = new PieceCoordinate(0, 0);
		assertNotNull(destinationCoord);
	}

	@Test
	public void testCoordinatesGetX() {
		PieceCoordinate destinationCoord = new PieceCoordinate(0, 0);
		assertEquals(0, destinationCoord.getX());
	}

	@Test
	public void testCoordinatesGetY() {
		PieceCoordinate destinationCoord = new PieceCoordinate(0, 0);
		assertEquals(0, destinationCoord.getY());
	}

	// Notion of board maintaining pieces

	@Test
	public void testAddPieceToBoard() {
		HantoBoard hantoBoard = new HantoBoard();
		hantoBoard.addPiece(new PieceCoordinate(0,0), new Butterfly(HantoPlayerColor.RED));
		assertFalse(hantoBoard.getBoard().isEmpty());
	}

	@Test
	public void testAddPieceToBoardAtPostion() {
		HantoBoard hantoBoard = new HantoBoard();
		Butterfly butterfly = new Butterfly(HantoPlayerColor.RED);
		PieceCoordinate coordinate = new PieceCoordinate(0,0);
		hantoBoard.addPiece(coordinate, butterfly);
		assertEquals(butterfly, hantoBoard.getPieceAt(coordinate));
	}

	@Test
	public void testMakeAlphaHantoGame() {
		HantoGame game = HantoGameFactory.makeHantoGame(HantoGameID.ALPHA_HANTO);
		assertTrue(game instanceof AlphaHantoGame);
	}

	@Test
	public void testAlphaHantoMakeMove() throws HantoException{
		HantoGame hantoGame = HantoGameFactory.makeHantoGame(HantoGameID.ALPHA_HANTO);
		PieceCoordinate destinationCoord = new PieceCoordinate(0, 0);
		MoveResult moveResult = hantoGame.makeMove(HantoPieceType.BUTTERFLY, null, destinationCoord);

		assertEquals(MoveResult.OK, moveResult);
	}

	@Test
	public void testAlphaHantoBlueButterflyMoveFirst() throws HantoException{
		HantoGame hantoGame = HantoGameFactory.makeHantoGame(HantoGameID.ALPHA_HANTO);
		PieceCoordinate destinationCoord = new PieceCoordinate(0, 0);
		hantoGame.makeMove(HantoPieceType.BUTTERFLY, null, destinationCoord);
		HantoPiece piece = hantoGame.getPieceAt(destinationCoord);

		assertEquals(HantoPlayerColor.BLUE, piece.getColor());
	}
	
	@Test
	public void testAlphaHantoGetPieceAt() throws HantoException {
		Butterfly butterfly = new Butterfly(HantoPlayerColor.BLUE);
		HantoGameFactory.getInstance();
		HantoGame hantoGame = HantoGameFactory.makeHantoGame(HantoGameID.ALPHA_HANTO);
		PieceCoordinate destinationCoord = new PieceCoordinate(0, 0);
		MoveResult moveResult = hantoGame.makeMove(butterfly.getType(), null, destinationCoord);		
		assertEquals(MoveResult.OK, moveResult);
	}
	
	@Test
	public void afterFirstMoveBlueButterflyIsAt0_0() throws HantoException	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		final HantoPiece p = game.getPieceAt(new PieceCoordinate(0, 0));
		assertEquals(HantoPieceType.BUTTERFLY, p.getType());
		assertEquals(HantoPlayerColor.BLUE, p.getColor());
	}

	@Test(expected=HantoException.class)
	public void bluePlacesNonButterfly() throws HantoException	{
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, 0));
	}
	
	@Test(expected=HantoException.class)
	public void blueAttemptsToPlaceButterflyAtWrongLocation() throws HantoException	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(-1, 1));
	}
	
	@Test
	public void testSecondPlayerIsRed() throws HantoException {
		HantoGame hantoGame = HantoGameFactory.makeHantoGame(HantoGameID.ALPHA_HANTO);
		PieceCoordinate destinationCoord = new PieceCoordinate(0, 0);
		hantoGame.makeMove(HantoPieceType.BUTTERFLY, null, destinationCoord);
	}

	//	@Test
	//	public void testPlaceButterflyOnBoard() {		
	//		Butterfly butterfly = new Butterfly(HantoPlayerColor.BLUE);
	//		HantoGameFactory.getInstance();
	//		HantoGame hantoGame = HantoGameFactory.makeHantoGame(HantoGameID.ALPHA_HANTO);
	//		
	//		MoveResult moveResult = hantoGame.makeMove(butterfly.getType(), null, destinationCoord);
	//		assertNotEquals(HantoPlayerColor.BLUE, butterfly.getColor());
	//		
	//		@Override
	//		public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
	//				HantoCoordinate to) throws HantoException {
	//			// TODO Auto-generated method stub
	//			return null;
	//		}
	//	}
}
