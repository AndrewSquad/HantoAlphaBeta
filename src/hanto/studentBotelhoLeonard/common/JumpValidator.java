/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * 
 * MoveValidator class for validating Walk movements in the Hanto game.
 */

package hanto.studentBotelhoLeonard.common;

import java.util.ArrayList;
import java.util.List;

/**
 * WalkValidator class is used to validate Walk movements for any HantoPiece.
 */
public class JumpValidator implements MoveValidator {

	private HantoBoard board;	

	/**
	 * Constructor for a WalkValidator object
	 * @param distanceLimit how far the piece is allowed to walk
	 * @param board the board that the WalkValidator will be interacting with.
	 */
	public JumpValidator(int distanceLimit, HantoBoard board) {
		this.board = board;
	}

	@Override
	public boolean isMoveLegal(PieceCoordinate from, PieceCoordinate to) {

		if (board.getPieceAt(to) != null) {
			return false;
		}
		else if (!isStraightLine(from, to)) {
			return false;
		}
		else {
			return piecesInWholePath(from, to);
		}

	}


	@Override
	public boolean existsLegalMove(PieceCoordinate coord) {
		// If it has any neighbor it can jump. If the board is contiguous after jump, move is legal
		if (board.isAdjacentToAnyPiece(coord)) {
			HantoBoard boardCopy = new HantoBoard(board);
			boardCopy.getBoardMap().remove(coord);
			if (boardCopy.isBoardContiguous()) {
				return true;
			}
			else {
				List<PieceCoordinate> neighbors = new ArrayList<PieceCoordinate>();
				for (PieceCoordinate c : coord.getSixAdjacentCoordinates()) {
					if (board.getPieceAt(coord) != null) neighbors.add(c);	
				}
				for (PieceCoordinate c : neighbors) {
					PieceCoordinate move = returnJump(coord, c);
					boardCopy.getBoardMap().put(move, board.getPieceAt(coord));
					if (boardCopy.isBoardContiguous()) return true;
					boardCopy.getBoardMap().remove(move);
				}
			}
		}

		return false;
	}


	//
	private PieceCoordinate returnJump(PieceCoordinate from, PieceCoordinate neighbor) {
		int deltaX = neighbor.getX() - from.getX();
		if (deltaX != 0 ) deltaX = deltaX > 1 ? 1 : -1;
		int deltaY = neighbor.getY() - from.getY();
		if (deltaY != 0 ) deltaY = deltaY > 1 ? 1 : -1;

		PieceCoordinate tempCoord = new PieceCoordinate(from.getX(), from.getY());

		// Stop looping when from + i = destination
		while (board.getPieceAt(tempCoord) != null) { // i is not equal to the destination
			tempCoord = new PieceCoordinate(tempCoord.getX() + deltaX, tempCoord.getY() + deltaY);
		}

		return new PieceCoordinate(tempCoord.getX(), tempCoord.getY());
	}


	private boolean piecesInWholePath(PieceCoordinate from, PieceCoordinate to) {
		// Slope
		// 3 Possibilities
		// deltaX = 0 and deltaY != 0
		// deltaY = 0 and deltaX != 0
		// deltaY equals -deltaX
		int deltaX = to.getX() - from.getX();
		if (deltaX != 0 ) deltaX = deltaX > 1 ? 1 : -1;
		int deltaY = to.getY() - from.getY();
		if (deltaY != 0 ) deltaY = deltaY > 1 ? 1 : -1;

		PieceCoordinate tempCoord = new PieceCoordinate(from.getX(), from.getY());

		boolean piecesInPath = true;

		// In 2/3 cases, using deltaY will get a number of iterations
		// In the case where Y remains constant, only X changes and therefore can be used.
		int i = (deltaY != 0) ? from.getY() : from.getX();

		// When x doesn't change, subtract delta y, when it does, add delta y to y.
		int multY = (deltaX == 0 && deltaY == -1) ? -1 : 1;

		// Stop looping when from + i = destination
		while (i != ((deltaY != 0) ? to.getY() - multY*deltaY : to.getX() - deltaX)) { // i is not equal to destination
			tempCoord = new PieceCoordinate(tempCoord.getX() + deltaX, tempCoord.getY() + deltaY);
			if (board.getPieceAt(tempCoord) == null) return false;
			i += (deltaY != 0) ? deltaY : deltaX; // i++ or i--
		}

		return piecesInPath;
	}

	private boolean isStraightLine(PieceCoordinate from, PieceCoordinate to) {
		int deltaX = to.getX() - from.getX();
		int deltaY = to.getY() - from.getY();

		if (deltaX == 0 && deltaY != 0) return true; // only y changed
		if (deltaY == 0 && deltaX != 0) return true; // only x changed
		if (deltaX != deltaY && Math.abs(deltaX) == Math.abs(deltaY)) return true; // change in x and y is equal and opposite


		return false;
	}

	@Override
	public PieceCoordinate optimalMove(PieceCoordinate from, PieceCoordinate target) {

		List<PieceCoordinate> neighbors = new ArrayList<PieceCoordinate>();
		for (PieceCoordinate c : from.getSixAdjacentCoordinates()) {
			if (board.getPieceAt(from) != null) neighbors.add(c);	
		}

		int minDist = Integer.MAX_VALUE;
		PieceCoordinate minDistCoord = null;

		for (PieceCoordinate c : neighbors) {
			PieceCoordinate move = returnJump(from, c);
			if (move.distanceFrom(target) < minDist) {
				minDist = move.distanceFrom(target);
				minDistCoord = move;
			}
		}

		return minDistCoord;
	}

	@Override
	public List<PieceCoordinate> allMoves(PieceCoordinate from) {
		List<PieceCoordinate> neighbors = new ArrayList<PieceCoordinate>();
		
		for (PieceCoordinate c : from.getSixAdjacentCoordinates()) {
			if (board.getPieceAt(from) != null) neighbors.add(c);	
		}

		List<PieceCoordinate> possMoves = new ArrayList<PieceCoordinate>();

		for (PieceCoordinate c : neighbors) {
			PieceCoordinate move = returnJump(from, c);
			possMoves.add(move);
		}

		return possMoves;
	}	
}
