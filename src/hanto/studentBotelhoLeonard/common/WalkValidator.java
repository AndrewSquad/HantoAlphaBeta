/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * 
 * MoveValidator class for validating Walk movements in the Hanto game.
 */

package hanto.studentBotelhoLeonard.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * WalkValidator class is used to validate Walk movements for any HantoPiece.
 */
public class WalkValidator implements MoveValidator {

	private int distanceLimit;
	private HantoBoard board;
	private List<PieceCoordinate> nodesVisited;


	/**
	 * Constructor for a WalkValidator object
	 * @param distanceLimit how far the piece is allowed to walk
	 * @param board the board that the WalkValidator will be interacting with.
	 */
	public WalkValidator(int distanceLimit, HantoBoard board) {
		this.distanceLimit = distanceLimit;
		this.board = board;
	}

	@Override
	public boolean isMoveLegal(PieceCoordinate from, PieceCoordinate to) {
		nodesVisited = new ArrayList<PieceCoordinate>();

		// the fringe will act as a queue of all tiles we need to explore
		// the fringe will be a hashmap mapping a tile to the list of tiles needed to move there
		PieceCoordinate currentNode;
		Map<PieceCoordinate, List<PieceCoordinate>> fringe = new HashMap<PieceCoordinate, List<PieceCoordinate>>();
		List<PieceCoordinate> pastMoves, tempList;

		List<PieceCoordinate> root = new ArrayList<PieceCoordinate>();
		root.add(from); // add the root (starting location) to fringe
		// add all "two tile openings" to the fringe
		for (PieceCoordinate adjacentToRoot : board.getTwoTileOpenings(from)) {
			fringe.put(adjacentToRoot, root);
		}

		while(fringe.size() > 0) {
			currentNode = closestToDest(fringe.keySet(), to);
			pastMoves = fringe.get(currentNode);
			fringe.remove(currentNode);

			if (nodesVisited.contains(currentNode)) continue;
			nodesVisited.add(currentNode);

			if (pastMoves.size() > distanceLimit) continue; // # of past moves can't be bigger than distanceLimit

			// current node must be adjacent to an occupied tile (not including previous tile in path)
			if (!isAdjacentToOccupiedTile(currentNode, pastMoves.get(pastMoves.size() - 1))) continue;

			// if we've reached the destination (goal state)
			if (currentNode.equals(to)) {
				HantoBoard boardCopy = new HantoBoard(board);
				boardCopy.moveExistingPiece(from, to, board.getPieceAt(from));
				if (!boardCopy.isBoardContiguous()) return false;
				return true;
			}

			// add current node's "two tile openings" to the fringe
			for (PieceCoordinate adjacentNode : board.getTwoTileOpenings(currentNode)) {
				tempList = new ArrayList<PieceCoordinate>(pastMoves);
				tempList.add(currentNode);
				fringe.put(adjacentNode, tempList);
			}
		}

		return false;
	}


	@Override
	public boolean existsLegalMove(PieceCoordinate coord) {
		List<PieceCoordinate> potentialMoves = allMoves(coord);
		if (potentialMoves.size() == 0) return false;
		
		return true;
	}

	// given a set of PieceCoordinate, determines which one is closest to the destination
	private PieceCoordinate closestToDest(Set<PieceCoordinate> nodes, PieceCoordinate dest) {
		int minDistance = Integer.MAX_VALUE;
		PieceCoordinate bestNode = null;
		int tempDistance;

		for (PieceCoordinate currentNode : nodes) {
			tempDistance = currentNode.distanceFrom(dest);
			if (tempDistance < minDistance){
				minDistance = tempDistance;
				bestNode = currentNode;
			}
		}

		return bestNode;
	}




	// determines if the given coordinate is adjacent to an occupied tile
	// does not look at the most recent tile in the path leading up to this current node
	private boolean isAdjacentToOccupiedTile(PieceCoordinate to, PieceCoordinate from) {

		for (PieceCoordinate adjacentCoordinate : to.getSixAdjacentCoordinates()) {
			if (adjacentCoordinate.equals(from)) continue; // don't look at previous tile in path
			if (board.isTileAlreadyOccupied(adjacentCoordinate)) return true;
		}

		return false;
	}

	@Override
	public PieceCoordinate optimalMove(PieceCoordinate from, PieceCoordinate target) {
		Set<PieceCoordinate> moves = new HashSet<PieceCoordinate>(allMoves(from));

		PieceCoordinate closest = null; 
		int minDist = Integer.MAX_VALUE;
		
		for (PieceCoordinate coord : moves) {
			int distance = coord.distanceFrom(target);
			if (isMoveLegal(from, coord) && distance < minDist) {
				minDist = distance;
				closest = coord;
			}
		}

		return closest;
	}

	@Override
	public List<PieceCoordinate> allMoves(PieceCoordinate from) {
		List<PieceCoordinate> allPossMoves = board.getTwoTileOpenings(from);
		List<PieceCoordinate> possMoves = new ArrayList<PieceCoordinate>();
		
		for (PieceCoordinate move : allPossMoves) {
			if (isMoveLegal(from, move)) possMoves.add(move);
		}
		
		return possMoves;
		
	}

}
