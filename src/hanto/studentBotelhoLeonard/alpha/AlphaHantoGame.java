package hanto.studentBotelhoLeonard.alpha;

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
		this.turnCount = 0;
		this.board = new HantoBoard();
	}

	
	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {
		// We only have butterflies, we realize this isn't technically extensible
		// but no other pieces exist yet so TDD dictates we must do it this way. 
		if (pieceType != HantoPieceType.BUTTERFLY) throw new HantoException("Unrecognized Piece Type");
		//System.out.println(board.getBoard().size());
		if ((board.getBoard().size() == 0) && (to.getX() != 0) && (to.getY() != 0)) throw new HantoException("Invalid Position to move to");
		
		Butterfly butterfly = new Butterfly(HantoPlayerColor.BLUE);
		board.addPiece(to, butterfly);

		turnCount++;
		return MoveResult.OK;
	}



	@Override
	public HantoPiece getPieceAt(HantoCoordinate where) {
		return board.getPieceAt(where);
	}

	@Override
	public String getPrintableBoard() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @return the board
	 */
	public HantoBoard getBoard() {
		return board;
	}

	/**
	 * @return the turnCount
	 */
	public int getTurnCount() {
		return turnCount;
	}

}
