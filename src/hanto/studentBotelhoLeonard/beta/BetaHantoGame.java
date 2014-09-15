package hanto.studentBotelhoLeonard.beta;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentBotelhoLeonard.common.Butterfly;
import hanto.studentBotelhoLeonard.common.HantoBoard;
import hanto.studentBotelhoLeonard.common.Sparrow;

public class BetaHantoGame implements HantoGame {
	
	private HantoPlayerColor movesFirst;
	private int turnCount;
	private HantoBoard board;
	
	public BetaHantoGame(HantoPlayerColor movesFirst) {
		this.movesFirst = movesFirst;
		this.turnCount = 0;
		this.board = new HantoBoard();
	}

	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {
		MoveResult result;
		HantoPlayerColor color;
		HantoPiece piece;
		
		// Beta can only place new pieces
		if (from != null) throw new HantoException("Can't move pieces in Alpha Hanto. Only place new pieces.");
		
		if (turnCount % 2 == 0) { // on even turns, the color movesFirst moves
			color = movesFirst;
		}
		else { // on odd turns, the color opposite of movesFirst moves
			if (movesFirst == HantoPlayerColor.BLUE) color = HantoPlayerColor.RED;
			else color = HantoPlayerColor.BLUE;
		}
		
		// switch case for adding different types of game pieces
		switch(pieceType) {
			case BUTTERFLY:
				piece = new Butterfly(color);
				break;
			case SPARROW:
				piece = new Sparrow(color);
				break;
			default:
				throw new HantoException("Unrecognized game piece type!");
		}
		
		board.addPiece(to, piece, turnCount);
		turnCount++;
		
		return MoveResult.OK;
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
