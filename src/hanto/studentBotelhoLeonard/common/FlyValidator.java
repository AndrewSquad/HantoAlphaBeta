/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * 
 * MoveValidator class for validating Fly movements in the Hanto game.
 */

package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoPiece;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;


/**
 * MoveValidator class for validating Fly movements in the Hanto game.
 */
public class FlyValidator implements MoveValidator {

	private int distanceLimit;
	private HantoBoard board;


	/**
	 * Constructor for a FlyValidator
	 * @param distanceLimit how many hexagons the piece can fly at once.
	 * @param board the HantoBoard that FlyValidator will be interacting with.
	 */
	public FlyValidator(int distanceLimit, HantoBoard board) {
		this.distanceLimit = distanceLimit;
		this.board = board;
	}

	@Override
	public boolean isMoveLegal(PieceCoordinate from, PieceCoordinate to) {

		boolean moveLegal = true;
		int distance = from.distanceFrom(to);

		// destination can't be farther away than the distanceLimit
		if (distance > distanceLimit) {
			moveLegal = false;
		}

		// destination must be empty and adjacent to an occupied tile
		else if (board.isTileAlreadyOccupied(to) || !board.isAdjacentToAnyPiece(to)) moveLegal = false;

		return moveLegal;
	}

	@Override
	public boolean existsLegalMove(PieceCoordinate coord) {
		HantoBoard boardCopy = new HantoBoard(board);

		boardCopy.getBoardMap().remove(coord);
		if (!boardCopy.isBoardContiguous()) {
			ArrayList<PieceCoordinate> possibleMoves = getAllFlyLocations();
			for (PieceCoordinate move : possibleMoves) {
				boardCopy.getBoardMap().put(move, board.getPieceAt(coord));
				if (boardCopy.isBoardContiguous()) return true;
			}
			return false;
		}
		return true;
	}

	private ArrayList<PieceCoordinate> getAllFlyLocations() {
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = board.getBoardMap().entrySet().iterator();
		ArrayList<PieceCoordinate> possibleMoves = new ArrayList<PieceCoordinate>();
		PieceCoordinate next; 
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			next = entry.getKey();
			for (PieceCoordinate coord : next.getSixAdjacentCoordinates()) {
				if (board.getPieceAt(coord) != null) {
					if (!possibleMoves.contains(coord)) possibleMoves.add(coord);
				}
			}
		}

		return possibleMoves;
	}

	public PieceCoordinate optimalMove(PieceCoordinate currentPos, PieceCoordinate target) {
		ArrayList<PieceCoordinate> possibleMoves = getAllFlyLocations();
		int minDist = Integer.MAX_VALUE;
		PieceCoordinate minDistCoord = null;

		for (PieceCoordinate coord : target.getSixAdjacentCoordinates()) {
			if (board.getPieceAt(coord) == null) return coord;
		}
		
		for (PieceCoordinate move : possibleMoves) {
			if (move.distanceFrom(target) < minDist) {
				minDist = move.distanceFrom(target);
				minDistCoord = move;
			}
		}

		return minDistCoord;
	}

	@Override
	public List<PieceCoordinate> allMoves(PieceCoordinate from) {
		return getAllFlyLocations();
	}

}
