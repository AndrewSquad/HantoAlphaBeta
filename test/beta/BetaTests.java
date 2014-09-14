package beta;

import static org.junit.Assert.*;
import hanto.HantoGameFactory;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
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

}
