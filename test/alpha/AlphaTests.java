package alpha;

import static org.junit.Assert.*;
import hanto.HantoGameFactory;
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


public class AlphaTests {
	// Test Butterfly exists
	//private static HantoGameFactory factory;
	private HantoGame game;
	
	@BeforeClass
	public static void initializeClass() {
		//factory = HantoGameFactory.getInstance();
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
		assertNotSame(HantoPlayerColor.BLUE, butterfly.getColor());
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
	public void testAddPieceToBoard() throws HantoException {
		HantoBoard hantoBoard = new HantoBoard();
		hantoBoard.addPiece(new PieceCoordinate(0,0), new Butterfly(HantoPlayerColor.RED), 0);
		assertFalse(hantoBoard.getBoard().isEmpty());
	}

	@Test
	public void testAddPieceToBoardAtPostion() throws HantoException {
		HantoBoard hantoBoard = new HantoBoard();
		Butterfly butterfly = new Butterfly(HantoPlayerColor.RED);
		PieceCoordinate coordinate = new PieceCoordinate(0,0);
		hantoBoard.addPiece(coordinate, butterfly, 0);
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
	public void redPlacesButterflyNextToBlueButterfly() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 1));
		final HantoPiece p = game.getPieceAt(new PieceCoordinate(0, 1));
		assertEquals(HantoPieceType.BUTTERFLY, p.getType());
		assertEquals(HantoPlayerColor.RED, p.getColor());
	}
	
	@Test
	public void testCoordinatesAdjacent() {
		PieceCoordinate coordinate1 = new PieceCoordinate(0,0);
		PieceCoordinate coordinate2 = new PieceCoordinate(1,0);
		PieceCoordinate coordinate3 = new PieceCoordinate(1,10);
		
		assertTrue(coordinate1.isAdjacentTo(coordinate2));
		assertFalse(coordinate1.isAdjacentTo(coordinate3));
	}
	
	@Test
	public void testSameCoordinatesNotAdjacent() {
		PieceCoordinate coordinate1 = new PieceCoordinate(0,0);
		PieceCoordinate coordinate2 = new PieceCoordinate(0,0);
		assertFalse(coordinate1.isAdjacentTo(coordinate2));
	}
	
	@Test
	public void testCoordinatesNotAdjacent() {
		PieceCoordinate coordinate1 = new PieceCoordinate(0, 0);
		PieceCoordinate coordinate2 = new PieceCoordinate(4, 4);
		assertFalse(coordinate1.isAdjacentTo(coordinate2));
	}
	
	@Test(expected=HantoException.class)
	public void redPlacesButterflyNotNextToBlueButterfly() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(5, 5));

	}
	
	@Test
	public void redMakesValidSecondMoveAndGameIsDrawn() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		final MoveResult mr = game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(-1, 1));
		assertEquals(MoveResult.DRAW, mr);
	}
	
	
	@Test(expected=HantoException.class)
	public void redPlacesButterflyNonAdjacentToBlueButterfly() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 2));
	}
	
	
	@Test(expected=HantoException.class)
	public void attemptToMoveRatherThanPlace() throws HantoException
	{
		game.makeMove(HantoPieceType.BUTTERFLY, new PieceCoordinate(0, 1), new PieceCoordinate(0, 0));
	}
	
}
