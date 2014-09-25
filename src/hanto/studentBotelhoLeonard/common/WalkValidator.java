package hanto.studentBotelhoLeonard.common;

import java.util.ArrayList;

public class WalkValidator implements MoveValidator {
	
	private int distanceLimit;
	private HantoBoard board;
	
	public WalkValidator(int distanceLimit, HantoBoard board) {
		this.distanceLimit = distanceLimit;
		this.board = board;
	}

	@Override
	public boolean isMoveLegal(PieceCoordinate from, PieceCoordinate to) {
		ArrayList<PieceCoordinate> path = findAvailablePath(from, to);
		boolean isMoveValid = !path.isEmpty();
		
		return isMoveValid;
	}
	
	private ArrayList<PieceCoordinate> findAvailablePath(PieceCoordinate from, PieceCoordinate to) {
		// when the distances to the points in the open list are equal or greater than the distance limit this can return
		ArrayList<PieceCoordinate> path = new ArrayList<PieceCoordinate>();
		//
		ArrayList<PieceCoordinate> closedList = new ArrayList<PieceCoordinate>();
		ArrayList<PieceCoordinate> openList = new ArrayList<PieceCoordinate>();
		
		openList.addAll(board.getTwoTileOpenings(from));
		
		path = findPath(path, openList, closedList, from, to);
		
		
		return path;
	}
	
	private ArrayList<PieceCoordinate> findPath(ArrayList<PieceCoordinate> path, ArrayList<PieceCoordinate> openList, 
			ArrayList<PieceCoordinate> closedList, PieceCoordinate from, PieceCoordinate to) {
		
		ArrayList<PieceCoordinate> copyPath = new ArrayList<PieceCoordinate>(path);
		PieceCoordinate coord;
		if (!openList.isEmpty()) coord = nextNodeToCheck(openList, to);
		else return copyPath;
		
		// If it's found, clear the open list and end recursion, valid move.
		if (coord == to) {
			openList.removeAll(openList);
			return copyPath;
		}
		
		// If we've gone to far never look at this node again.
		if (coord.distanceFrom(from) >= distanceLimit) {
			closedList.add(coord);
			openList.remove(coord);
			return copyPath;
		}
		
		else {
			copyPath.add(coord);
			for (PieceCoordinate possCoord : board.getTwoTileOpenings(coord))
				if (!openList.contains(possCoord)) openList.add(possCoord);
		}
		
		copyPath = findPath(copyPath, openList, closedList, from, to);		
		return copyPath;
	}
	
	// Finds the next node to check based upon distance from destination.
	private PieceCoordinate nextNodeToCheck(ArrayList<PieceCoordinate> openList, PieceCoordinate to) {
		int minDistance;
		minDistance = openList.get(0).distanceFrom(to);
		PieceCoordinate minDistanceCoord = openList.get(0);
		
		for (PieceCoordinate coord : openList) {
			if (coord.distanceFrom(to) < minDistance) minDistanceCoord = coord;
		}
		
		return minDistanceCoord;
		
	}

}
