package alpha;

import static org.junit.Assert.*;
import hanto.common.HantoPieceType;
import hanto.studentBotelhoLeonard.common.Butterfly;

import org.junit.Test;

public class AlphaTests {

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

}
