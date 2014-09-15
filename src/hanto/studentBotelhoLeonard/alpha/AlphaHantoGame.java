/**
 * @author Andrew & Andrew || Botelho & Leonard
 * AlphaHantoGame version of Hanto.
 * Each player can only place one butterfly.
 */

package hanto.studentBotelhoLeonard.alpha;

import java.util.HashMap;
import java.util.Map;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.common.Butterfly;
import hanto.studentBotelhoLeonard.common.HantoBoard;

public class AlphaHantoGame implements HantoGame {

	private int turnCount;
	private HantoBoard board;

	public AlphaHantoGame() {
		turnCount = 0;
		
		Map<HantoPieceType, Integer> pieceLimits = new HashMap<HantoPieceType, Integer>();
		pieceLimits.put(HantoPieceType.BUTTERFLY, 1);
		board = new HantoBoard(pieceLimits);
	}

	
	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {
		// We only have butterflies, we realize this isn't technically extensible
		// but no other pieces exist yet so TDD dictates we must do it this way. 
		MoveResult result;
		HantoPlayerColor color;
		
		// This line is 100% alpha hanto code specific.
		if (from != null) throw new HantoException("Can't move pieces in Alpha Hanto. Only place new pieces.");
		
		if (pieceType != HantoPieceType.BUTTERFLY) throw new HantoException("Unrecognized Piece Type");
						
		if (turnCount % 2 ==  0) { // if the turn count is even - blue should go
			color = HantoPlayerColor.BLUE;
		}
		else { // turn count is odd - red should go
			color = HantoPlayerColor.RED;
		}
		
		Butterfly butterfly = new Butterfly(color);
		board.addPiece(to, butterfly, turnCount);

		turnCount++;
		
		if (turnCount == 2) { // Alpha Hanto ends after turn 2
			result = MoveResult.DRAW;
		}
		else {
			result = MoveResult.OK;
		}
		
		return result;
	}


	@Override
	public HantoPiece getPieceAt(HantoCoordinate where) {
		return board.getPieceAt(where);
	}

	
	@Override
	public String getPrintableBoard() {
		return board.toString();
	}

}
