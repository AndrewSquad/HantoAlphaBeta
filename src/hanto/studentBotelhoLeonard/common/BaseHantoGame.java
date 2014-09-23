package hanto.studentBotelhoLeonard.common;

import java.util.Map;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;

public abstract class BaseHantoGame implements HantoGame {
	
	protected HantoPlayerColor movesFirst;
	protected int turnCount;
	protected HantoBoard board;
	protected Map<HantoPieceType, Integer> bluePiecesLeft;
	protected Map<HantoPieceType, Integer> redPiecesLeft;
	
	public BaseHantoGame(HantoPlayerColor movesFirst) {
		this.movesFirst = movesFirst;
		turnCount = 0;
		board = new HantoBoard();
	}

	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HantoPiece getPieceAt(HantoCoordinate where) {
		PieceCoordinate coordinate = new PieceCoordinate(where); // copy constructor
		return board.getPieceAt(coordinate);
	}

	@Override
	public String getPrintableBoard() {
		return board.toString();
	}

}
