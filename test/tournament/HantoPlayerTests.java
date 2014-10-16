package tournament;

import static org.junit.Assert.*;
import static hanto.common.HantoPlayerColor.*;
import static hanto.common.HantoPieceType.*;
import static hanto.studentBotelhoLeonard.common.MoveType.*;
import static hanto.common.MoveResult.*;

import java.util.List;

import hanto.studentBotelhoLeonard.common.HantoBoard;
import hanto.studentBotelhoLeonard.common.MoveValidator;
import hanto.studentBotelhoLeonard.common.MoveValidatorFactory;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;
import hanto.studentBotelhoLeonard.common.pieces.Butterfly;
import hanto.studentBotelhoLeonard.common.pieces.Crab;
import hanto.studentBotelhoLeonard.common.pieces.Horse;
import hanto.studentBotelhoLeonard.common.pieces.Sparrow;
import hanto.studentBotelhoLeonard.epsilon.EpsilonHantoGame;
import hanto.studentBotelhoLeonard.tournament.HantoPlayer;
import hanto.tournament.HantoMoveRecord;
import hanto.common.HantoException;
import hanto.common.HantoGameID;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;


import org.junit.Before;
import org.junit.Test;

public class HantoPlayerTests {

	private HantoPlayer bluePlayer = new HantoPlayer();
	private HantoPlayer redPlayer = new HantoPlayer();

	private PieceCoordinate origin;
	private MoveValidatorFactory validatorFactory;

	@Before
	public void setUp() throws Exception {
		bluePlayer = new HantoPlayer(BLUE, true);
		redPlayer = new HantoPlayer(RED, false);
		bluePlayer.startGame(HantoGameID.EPSILON_HANTO, BLUE, true);
		redPlayer.startGame(HantoGameID.EPSILON_HANTO, RED, false);

		origin = new PieceCoordinate(0, 0);
		validatorFactory = MoveValidatorFactory.getInstance();
	}

	@Test
	public void horseIsPlacedOnFirstMove() {
		HantoMoveRecord mvr = bluePlayer.makeMove(null);
		assertEquals(HORSE, mvr.getPiece());
	}

	@Test
	public void redResignsWhenGivenBadMove() {
		bluePlayer.makeMove(null);
		HantoMoveRecord mvr = redPlayer.makeMove(new HantoMoveRecord(CRAB, null, null));
		assertNull(mvr.getPiece());
		assertNull(mvr.getFrom());
		assertNull(mvr.getTo());
	}

	@Test
	public void walkOptimalMove() {		
		HantoBoard board = new HantoBoard();
		board.addPiece(origin, new Butterfly(BLUE));
		board.addPiece(new PieceCoordinate(0, -1), new Crab(RED));
		board.addPiece(new PieceCoordinate(-1, -1), new Crab(RED));

		MoveValidator validator = validatorFactory.makeMoveValidator(WALK, 1, board);
		PieceCoordinate optimalDest = validator.optimalMove(new PieceCoordinate(-1, -1), origin);
		assertEquals(new PieceCoordinate(-1, 0), optimalDest);
	}

	@Test
	public void jumpOptimalMove() {		
		HantoBoard board = new HantoBoard();
		board.addPiece(origin, new Butterfly(BLUE));
		board.addPiece(new PieceCoordinate(0, 1), new Crab(RED));
		board.addPiece(new PieceCoordinate(1, 1), new Butterfly(RED));
		board.addPiece(new PieceCoordinate(0, -1), new Crab(BLUE));
		board.addPiece(new PieceCoordinate(0, -2), new Horse(BLUE));

		MoveValidator validator = validatorFactory.makeMoveValidator(JUMP, board);
		PieceCoordinate optimalDest = validator.optimalMove(new PieceCoordinate(0, -2), new PieceCoordinate(1, 1));
		assertEquals(new PieceCoordinate(0, 2), optimalDest);
	}

	@Test
	public void noLegalJump() {		
		HantoBoard board = new HantoBoard();
		board.addPiece(origin, new Horse(BLUE));
		board.addPiece(new PieceCoordinate(0, 1), new Crab(RED));
		board.addPiece(new PieceCoordinate(0, -1), new Crab(BLUE));

		MoveValidator validator = validatorFactory.makeMoveValidator(JUMP, board);
		assertFalse(validator.existsLegalMove(origin));
	}

	@Test
	public void noLegalFly() {		
		HantoBoard board = new HantoBoard();
		board.addPiece(origin, new Sparrow(BLUE));
		board.addPiece(new PieceCoordinate(0, 1), new Crab(RED));
		board.addPiece(new PieceCoordinate(0, -1), new Crab(BLUE));

		MoveValidator validator = validatorFactory.makeMoveValidator(FLY, 4, board);
		assertFalse(validator.existsLegalMove(origin));

		assertEquals(0, validator.allMoves(origin).size());
	}

	@Test
	public void optimalFly() {
		HantoBoard board = new HantoBoard();
		board.addPiece(origin, new Butterfly(BLUE));
		board.addPiece(new PieceCoordinate(0, 1), new Crab(RED));
		board.addPiece(new PieceCoordinate(1, 0), new Butterfly(RED));
		board.addPiece(new PieceCoordinate(1, -1), new Crab(BLUE));
		board.addPiece(new PieceCoordinate(-1, 0), new Horse(BLUE));
		board.addPiece(new PieceCoordinate(-1, 1), new Horse(BLUE));
		board.addPiece(new PieceCoordinate(0, -1), new Sparrow(BLUE));

		board.addPiece(new PieceCoordinate(0, 2), new Sparrow(BLUE));
		board.addPiece(new PieceCoordinate(1, 1), new Crab(RED));
		board.addPiece(new PieceCoordinate(2, 0), new Crab(RED));
		board.addPiece(new PieceCoordinate(2, -1), new Crab(BLUE));
		board.addPiece(new PieceCoordinate(2, -2), new Horse(BLUE));
		board.addPiece(new PieceCoordinate(1, -2), new Horse(BLUE));
		board.addPiece(new PieceCoordinate(-1, -1), new Sparrow(BLUE));
		board.addPiece(new PieceCoordinate(-2, 0), new Crab(RED));
		board.addPiece(new PieceCoordinate(-2, 1), new Crab(RED));
		board.addPiece(new PieceCoordinate(-2, 2), new Crab(BLUE));
		board.addPiece(new PieceCoordinate(-1, 2), new Horse(BLUE));
		board.addPiece(new PieceCoordinate(0, 3), new Sparrow(BLUE));

		MoveValidator validator = validatorFactory.makeMoveValidator(FLY, 6, board);
		PieceCoordinate optimalDest = validator.optimalMove(new PieceCoordinate(0, 3), origin);
		assertEquals(new PieceCoordinate(0, -2), optimalDest);
	}

	@Test
	public void flyResumeContiguous() {
		HantoBoard board = new HantoBoard();
		board.addPiece(origin, new Butterfly(BLUE));
		board.addPiece(new PieceCoordinate(0, 1), new Crab(RED));
		board.addPiece(new PieceCoordinate(0, -1), new Sparrow(BLUE));
		board.addPiece(new PieceCoordinate(1, 0), new Sparrow(BLUE));
		board.addPiece(new PieceCoordinate(2, -1), new Sparrow(BLUE));
		MoveValidator validator = validatorFactory.makeMoveValidator(FLY, 4, board);
		assertTrue(validator.existsLegalMove(new PieceCoordinate(1, 0)));
	}

	@Test
	public void randomLegalMove() {
		HantoMoveRecord previousMove;
		HantoMoveRecord randomMove;

		previousMove = bluePlayer.makeMove(null);

		for (int i = 0; i < 5; i++) {
			previousMove = redPlayer.makeMove(previousMove);
			previousMove = bluePlayer.makeMove(previousMove);
		}

		randomMove = redPlayer.randomLegalMove();
		assertNotNull(randomMove.getTo());
	}

	@Test
	public void findAllMoves() {
		HantoMoveRecord previousMove;
		List<PieceCoordinate> moves = null;
		previousMove = bluePlayer.makeMove(null);
		PieceCoordinate sparrowCoord = null;

		while (true) {
			previousMove = redPlayer.makeMove(previousMove);
			if (previousMove.getPiece() == SPARROW) {
				sparrowCoord = new PieceCoordinate(previousMove.getTo());
				moves = redPlayer.findAllMoves(sparrowCoord);
				break;
			}
			previousMove = bluePlayer.makeMove(previousMove);
			if (previousMove.getPiece() == SPARROW) {
				sparrowCoord = new PieceCoordinate(previousMove.getTo());
				moves = bluePlayer.findAllMoves(sparrowCoord);
				break;
			}
		}

		assertFalse(moves.isEmpty());
		assertNull(bluePlayer.findAllMoves(new PieceCoordinate(100, 100)));
	}


	@Test
	public void theGameCanEnd() throws HantoException {
		EpsilonHantoGame theGame = new EpsilonHantoGame(HantoPlayerColor.BLUE);
		MoveResult mv;
		HantoMoveRecord previousMove;

		boolean running = true;
		previousMove = bluePlayer.makeMove(null);
		mv = theGame.makeMove(previousMove.getPiece(), previousMove.getFrom(), previousMove.getTo());

		while(running) {
			previousMove = redPlayer.makeMove(previousMove);
			mv = theGame.makeMove(previousMove.getPiece(), previousMove.getFrom(), previousMove.getTo());
			if (mv != OK) {
				running = false;
				continue;
			}
			previousMove = bluePlayer.makeMove(previousMove);
			mv = theGame.makeMove(previousMove.getPiece(), previousMove.getFrom(), previousMove.getTo());
			if (mv != OK) {
				running = false;
			}
		}

		boolean gameEndedProperly = (mv == OK || mv == RED_WINS || mv == BLUE_WINS);
		assertTrue(gameEndedProperly);
	}


	@Test
	public void testEndGame() {
		HantoMoveRecord previousMove;

		previousMove = bluePlayer.makeMove(null);

		for (int i = 0; i < 10; i++) {
			previousMove = redPlayer.makeMove(previousMove);
			previousMove = bluePlayer.makeMove(previousMove);
		}

		HantoMoveRecord aMove = bluePlayer.endGameLogic();
		assertEquals(SPARROW, aMove.getPiece());
	}

	@Test
	public void testRandomMoveGenerator() throws HantoException {
		HantoMoveRecord previousMove;

		previousMove = bluePlayer.makeMove(null);

		for (int i = 0; i < 1; i++) {
			previousMove = redPlayer.makeMove(previousMove);

			previousMove = bluePlayer.makeMove(previousMove);
		}


		for (int i = 0; i < 40; i++) {
			previousMove = redPlayer.randomLegalMove();

			previousMove = bluePlayer.randomLegalMove();
		}

		assertNotNull(previousMove.getPiece());
	}

	@Test
	public void testFreeButterfly() throws HantoException {
		EpsilonHantoGame theGame = new EpsilonHantoGame(HantoPlayerColor.BLUE);

		theGame.makeMove(BUTTERFLY, null, origin);
		theGame.makeMove(BUTTERFLY, null, new PieceCoordinate(1, -1));
		theGame.makeMove(HORSE, null, new PieceCoordinate(-1, 1));
		theGame.makeMove(HORSE, null, new PieceCoordinate(2, -2));
		theGame.makeMove(SPARROW, null, new PieceCoordinate(0, 1));
		theGame.makeMove(SPARROW, null, new PieceCoordinate(1, -2));

		redPlayer.setGame(theGame);
		bluePlayer.setGame(theGame);
		redPlayer.setMyButterflyLoc(new PieceCoordinate(1, -1));
		bluePlayer.setMyButterflyLoc(origin);
		redPlayer.setOpponentButterflyLoc(origin);
		bluePlayer.setOpponentButterflyLoc(new PieceCoordinate(1, -1));


		HantoMoveRecord mvr = bluePlayer.freeButterfly();
		assertNotNull(mvr); 

	}
	
	@Test
	public void jumpResumeContiguous() throws HantoException {
		HantoBoard board = new HantoBoard();
		board.addPiece(origin, new Butterfly(BLUE));
		board.addPiece(new PieceCoordinate(-1, 0), new Horse(RED));
		board.addPiece(new PieceCoordinate(-1, -1), new Sparrow(BLUE));
		board.addPiece(new PieceCoordinate(0, -2), new Sparrow(BLUE));
		board.addPiece(new PieceCoordinate(1, -2), new Sparrow(BLUE));
		board.addPiece(new PieceCoordinate(2, -2), new Sparrow(RED));
		board.addPiece(new PieceCoordinate(2, -1), new Sparrow(BLUE));
		board.addPiece(new PieceCoordinate(2, 0), new Sparrow(BLUE));

		MoveValidator validator = validatorFactory.makeMoveValidator(JUMP, 4, board);
		assertTrue(validator.existsLegalMove(new PieceCoordinate(-1, 0)));
		
		
	}

}
