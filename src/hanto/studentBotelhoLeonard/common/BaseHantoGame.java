package hanto.studentBotelhoLeonard.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
	
	
	// given a player color, this method determines whether or not he/she has placed a butterfly yet
	protected boolean hasPlayerPlacedButterfly(HantoPlayerColor player) {
		boolean hasPlacedButterfly = true;
		if (player == HantoPlayerColor.BLUE) {
			if (bluePiecesLeft.get(HantoPieceType.BUTTERFLY) > 0) hasPlacedButterfly = false;
		}
		
		else {
			if (redPiecesLeft.get(HantoPieceType.BUTTERFLY) > 0) hasPlacedButterfly = false;
		}

		return hasPlacedButterfly;
	}
	
	// decrements how many of a certain piece type the given player has left to use
	protected void decrementPieceTypeForPlayer(HantoPlayerColor player, HantoPieceType pieceType) {
		int piecesLeft = playerPieceTypeRemaining(player, pieceType);
		if (player == HantoPlayerColor.BLUE) {
			bluePiecesLeft.put(pieceType, (piecesLeft - 1));
		}
		else {
			redPiecesLeft.put(pieceType, (piecesLeft - 1));
		}
	}
	


	// determines how many of a particular piece type the given player has left to put on the board
	protected int playerPieceTypeRemaining(HantoPlayerColor player, HantoPieceType pieceType) {
		if (player == HantoPlayerColor.BLUE) {
			return bluePiecesLeft.get(pieceType);
		}
		else {
			return redPiecesLeft.get(pieceType);
		}
	}
	
	// determines if either player has any pieces left to move with
	protected boolean anyPiecesLeftToPlay() {
		int totalPiecesLeft = 0;
		Iterator<Entry<HantoPieceType, Integer>> bluePieceTotals = bluePiecesLeft.entrySet().iterator();
		Iterator<Entry<HantoPieceType, Integer>> redPieceTotals = redPiecesLeft.entrySet().iterator();
		
		while(bluePieceTotals.hasNext()) {
			Entry<HantoPieceType, Integer> entry = bluePieceTotals.next();
			totalPiecesLeft += entry.getValue();
		}
		while(redPieceTotals.hasNext()) {
			Entry<HantoPieceType, Integer> entry = redPieceTotals.next();
			totalPiecesLeft += entry.getValue();
		}
		
		return totalPiecesLeft > 0;
	}

}
