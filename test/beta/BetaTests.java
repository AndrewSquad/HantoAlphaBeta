/**
 * @author Andrew & Andrew || Botelho & Leonard
 * Tests for Beta Hanto
 */
package beta;

import static org.junit.Assert.*;
import static hanto.common.HantoPieceType.*;

import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.HantoGameFactory;
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
	public void testSparrowPrintsProperly() {		
		Sparrow sparrow = new Sparrow(HantoPlayerColor.BLUE);
		assertEquals("Sparrow", sparrow.getType().getPrintableName());
	}

	@Test
	public void testButterflySymbolPrintsProperly() {		
		Butterfly butterfly = new Butterfly(HantoPlayerColor.BLUE);
		assertEquals("B", butterfly.getType().getSymbol());
	}
	
	@Test
	public void testSparrowSymbolPrintsProperly() {		
		Sparrow sparrow = new Sparrow(HantoPlayerColor.BLUE);
		assertEquals("S", sparrow.getType().getSymbol());
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

	@Test
	public void testAddPieceToBoard() throws HantoException {
		HantoBoard hantoBoard = new HantoBoard();
		hantoBoard.addPiece(new PieceCoordinate(0,0), new Butterfly(HantoPlayerColor.RED));
		assertFalse(hantoBoard.getBoardMap().isEmpty());
	}

	@Test
	public void testAddPieceToBoardAtPostion() throws HantoException {
		HantoBoard hantoBoard = new HantoBoard();
		Butterfly butterfly = new Butterfly(HantoPlayerColor.RED);
		PieceCoordinate coordinate = new PieceCoordinate(0,0);
		hantoBoard.addPiece(coordinate, butterfly);
		assertEquals(butterfly, hantoBoard.getPieceAt(coordinate));
	}
	
	@Test
	public void testHantoGetPieceAt() throws HantoException {
		PieceCoordinate destinationCoord = new PieceCoordinate(0, 0);
		MoveResult moveResult = game.makeMove(HantoPieceType.BUTTERFLY, null, destinationCoord);		
		assertEquals(MoveResult.OK, moveResult);
	}
	
	@Test
	public void afterFirstMoveBlueButterflyIsAt0_0() throws HantoException	{
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		final HantoPiece p = game.getPieceAt(new PieceCoordinate(0, 0));
		assertEquals(HantoPieceType.BUTTERFLY, p.getType());
		assertEquals(HantoPlayerColor.BLUE, p.getColor());
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
	
	@Test
	public void testCopyingHantoCoordinates() {
		PieceCoordinate coordinate1 = new PieceCoordinate(0, 0);
		PieceCoordinate coordinate2 = new PieceCoordinate(coordinate1);
		assertEquals(coordinate1, coordinate2);
	}
	
	
	@Test
	public void testNullNotEqualCoordinate() {
		PieceCoordinate coordinate1 = new PieceCoordinate(0, 0);
		PieceCoordinate coordinate2 = null;
		assertFalse(coordinate1.equals(coordinate2));
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
	public void putPieceOnSameTileRightAway() throws HantoException {
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
	}
	
	@Test(expected=HantoException.class)
	public void putPieceOnSameTileLater() throws HantoException {
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

	
	@Test(expected=HantoException.class)
	public void noButterflyAfterThreeTurns() throws HantoException {
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(1, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(1, 2));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(2, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(2, 2));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(3, 2));

	}

	@Test(expected=HantoException.class)
	public void onlyOneButterFlyAfterThreeTurns() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(SPARROW, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(1, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(2, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(2, 2));
		game.makeMove(SPARROW, null, new PieceCoordinate(3, 2));
		game.makeMove(SPARROW, null, new PieceCoordinate(3, 3));
		game.makeMove(SPARROW, null, new PieceCoordinate(4, 3));

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
	
	
	@Test
	public void redIsWinner() throws HantoException {
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(1, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(1, -1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, -1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(-1, 0));
		MoveResult mv = game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(-1, 1));
		assertEquals(MoveResult.RED_WINS, mv);
	}
	
	@Test
	public void blueIsWinner() throws HantoException {
		game = HantoGameFactory.makeHantoGame(HantoGameID.BETA_HANTO, HantoPlayerColor.RED);
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(1, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(1, -1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, -1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(-1, 0));
		MoveResult mv = game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(-1, 1));
		assertEquals(MoveResult.BLUE_WINS, mv);
	}
	
	@Test
	public void bothButterfliesSurrounded() throws HantoException {
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(1, 0));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, -1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(-1, 0));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(-1, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(1, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(2, 0));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(2, -1));
		final HantoPiece p = game.getPieceAt(new PieceCoordinate(1, 0));
		MoveResult mv = game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(1, -1));
		assertEquals(HantoPieceType.BUTTERFLY, p.getType());
		assertEquals(HantoPlayerColor.RED, p.getColor());
		assertEquals(MoveResult.DRAW, mv);
	}
	
	@Test
	public void testToString() throws HantoException {
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 1));
		assertEquals("Butterfly RED (0, 1)\nButterfly BLUE (0, 0)\n", game.getPrintableBoard());
	}
	
	@Test(expected=HantoException.class)
	public void tooManyButterflies() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(SPARROW, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(1, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(2, 1));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(3, 1));
	}
	
	@Test
	public void someLegalMoves() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(SPARROW, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(1, 1));
		MoveResult mv = game.makeMove(SPARROW, null, new PieceCoordinate(2, 1));
		final HantoPiece p = game.getPieceAt(new PieceCoordinate(1, 1));
		assertEquals(HantoPieceType.SPARROW, p.getType());
		assertEquals(HantoPlayerColor.BLUE, p.getColor());
		assertEquals(MoveResult.OK, mv);
	}
	
	@Test
	public void someMoreLegalMove() throws HantoException {
		game = HantoGameFactory.makeHantoGame(HantoGameID.BETA_HANTO, HantoPlayerColor.RED);
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(SPARROW, null, new PieceCoordinate(0, -1));
		game.makeMove(SPARROW, null, new PieceCoordinate(1, 0));
		game.makeMove(SPARROW, null, new PieceCoordinate(-1, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(2, 0));
		game.makeMove(SPARROW, null, new PieceCoordinate(-1, 0));
		game.makeMove(SPARROW, null, new PieceCoordinate(-2, 0));
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(-3, 0));
		MoveResult mv = game.makeMove(SPARROW, null, new PieceCoordinate(-4, 0));
		final HantoPiece p = game.getPieceAt(new PieceCoordinate(1, 0));
		assertEquals(HantoPieceType.SPARROW, p.getType());
		assertEquals(HantoPlayerColor.RED, p.getColor());
		assertEquals(MoveResult.OK, mv);
	}
	
	@Test(expected=HantoException.class)
	public void putSparrowOnTopButterfly() throws HantoException {
		game.makeMove(BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(SPARROW, null, new PieceCoordinate(0, 1));
		game.makeMove(SPARROW, null, new PieceCoordinate(0, 0));
	}
	
	@Test(expected=HantoException.class)
	public void pieceNotAdjacent() throws HantoException {
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(0, 0));
		game.makeMove(HantoPieceType.BUTTERFLY, null, new PieceCoordinate(1, 0));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, -1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(-1, 0));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(-1, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(0, 1));
		game.makeMove(HantoPieceType.SPARROW, null, new PieceCoordinate(4, 1));
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


}
