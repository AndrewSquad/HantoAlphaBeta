package hanto.studentBotelhoLeonard.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class WalkValidator implements MoveValidator {
	
	private int distanceLimit;
	private HantoBoard board;
	private List<PieceCoordinate> nodesVisited;
	
	public WalkValidator(int distanceLimit, HantoBoard board) {
		this.distanceLimit = distanceLimit;
		this.board = board;
		nodesVisited = new ArrayList<PieceCoordinate>();
	}

	@Override
	public boolean isMoveLegal(PieceCoordinate from, PieceCoordinate to) {
		
		PieceCoordinate currentNode;
		HashMap<PieceCoordinate, ArrayList<PieceCoordinate>> fringe = new HashMap<PieceCoordinate, ArrayList<PieceCoordinate>>();
		ArrayList<PieceCoordinate> pastMoves, tempList;
		
		ArrayList<PieceCoordinate> root = new ArrayList<PieceCoordinate>();
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
