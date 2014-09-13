package alpha;

import static org.junit.Assert.*;
import hanto.HantoGameFactory;
import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.common.Butterfly;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;

import org.junit.Test;

public class AlphaTests {
	// Test Butterfly exists
	@Test
	public void testButterflyExists() {		
		Butterfly butterfly = new Butterfly();
		assertNotNull(butterfly);
	}
	
	@Test
	public void testButterflyIsTypeButterfly() {		
		Butterfly butterfly = new Butterfly();
		assertEquals(HantoPieceType.BUTTERFLY, butterfly.getType());
	}
	
	@Test
	public void testButterflyPrintsProperly() {		
		Butterfly butterfly = new Butterfly();
		assertEquals("Butterfly", butterfly.getType().getPrintableName());
	}
	
	@Test
	public void testButterflySymbolPrintsProperly() {		
		Butterfly butterfly = new Butterfly();
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
