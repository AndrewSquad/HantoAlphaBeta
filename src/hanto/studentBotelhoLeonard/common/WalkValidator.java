/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * 
 * MoveValidator class for validating Walk movements in the Hanto game.
 */

package hanto.studentBotelhoLeonard.common;

import java.util.ArrayList;
import java.util.HashMap;
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
		nodesVisited = new ArrayList<PieceCoordinate>();
	}

	@Override
	public boolean isMoveLegal(PieceCoordinate from, PieceCoordinate to) {
		
		PieceCoordinate currentNode;
		Map<PieceCoordinate, List<PieceCoordinate>> fringe = new HashMap<PieceCoordinate, List<PieceCoordinate>>();
		List<PieceCoordinate> pastMoves, tempList;
		
		List<PieceCoordinate> root = new ArrayList<PieceCoordinate>();
		root.add(from);
		for (PieceCoordinate adjacentToRoot : board.getTwoTileOpenings(from)) {
			fringe.put(adjacentToRoot, root);
		}
		
		while(fringe.size() > 0) {
			currentNode = closestToDest(fringe.keySet(), to);
			pastMoves = fringe.get(currentNode);
			fringe.remove(currentNode);
			
			if (nodesVisited.contains(currentNode)) continue;
			nodesVisited.add(currentNode);
			
			if (pastMoves.size() > distanceLimit) continue;
			
			if (!isAdjacentToOccupiedTile(currentNode, pastMoves.get(pastMoves.size() - 1))) continue;
			
			if (currentNode.equals(to)) return true;
			
			for (PieceCoordinate adjacentNode : board.getTwoTileOpenings(currentNode)) {
				tempList = new ArrayList<PieceCoordinate>(pastMoves);
				tempList.add(currentNode);
				fringe.put(adjacentNode, tempList);
			}
		}
		
		return false;
	}
	
	
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
	
	
	private boolean isAdjacentToOccupiedTile(PieceCoordinate to, PieceCoordinate from) {
		
		for (PieceCoordinate adjacentCoordinate : to.getSixAdjacentCoordinates()) {
			if (adjacentCoordinate.equals(from)) continue;
			if (board.isTileAlreadyOccupied(adjacentCoordinate)) return true;
		}
		
		return false;
	}

}
