package gamma;

import static org.junit.Assert.*;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.studentBotelhoLeonard.HantoGameFactory;

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
	public void test() {
		fail("Not yet implemented");
	}

}
