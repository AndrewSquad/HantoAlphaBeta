/**
 * 
 */
package hanto.studentBotelhoLeonard.common;

import java.util.ArrayList;
import java.util.List;

import hanto.common.HantoCoordinate;

/**
 * @author Andrew & Andrew || Leonard & Botelho
 * Class for PieceCoordinate objects, which represent coordinates on the hexagon game board.
 * This class implements the HantoCoordinate interface.
 */
public class PieceCoordinate implements HantoCoordinate {

	// Coordinates
	private int x;
	private int y;

	/**
	 * Constructor for a PieceCoordinate
	 * @param x - the x coordinate of the PieceCoordinate
	 * @param y - the y coordinate of the PieceCoordinate
	 */
	public PieceCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Copy constructor for coordinates (interface to class)
	 * @param coordinate the HantoCoordinate we are converting into a PieceCoordinate
	 */
	public PieceCoordinate(HantoCoordinate coordinate) {
		x = coordinate.getX();
		y = coordinate.getY();
	}

	/**
	 * returns X coordinate
	 */
	@Override
	public int getX() {
		return x;
	}

	/**
	 * returns Y coordinate
	 */
	@Override
	public int getY() {
		return y;
	}


	/**
	 * Given another HantoCoordinate object, this method determines if this HantoCoordinate and the other 
	 * HantoCoordinate are adjacent to each other.
	 * @param otherCoordinate the other HantoCoordinate to determine adjacency with.
	 * @return a boolean indicating if the two HantoCoordinates are adjacent.
	 */
	public boolean isAdjacentTo(HantoCoordinate otherCoordinate) {
		if (otherCoordinate.equals(this)) return false;

		int otherX = otherCoordinate.getX();
		int otherY = otherCoordinate.getY();

		int deltaX = otherX - x;
		int deltaY = otherY - y;
		
		if (deltaX < 0 && deltaY < 0 || deltaX > 0 && deltaY > 0) return false; 

		boolean xAdjacent = Math.abs(deltaX) < 2; 
		boolean yAdjacent = Math.abs(deltaY) < 2;

		return (xAdjacent && yAdjacent); 
	}
	
	
	/**
	 * Returns an ArrayList of the six adjacent PieceCoordinates to this PieceCoordinate object.
	 * @return the ArrayList of six adjacent PieceCoordinates
	 */
	public List<PieceCoordinate> getSixAdjacentCoordinates() {
		List<PieceCoordinate> adjacentCoordinates = new ArrayList<PieceCoordinate>();
		adjacentCoordinates.add(new PieceCoordinate(x, y + 1));
		adjacentCoordinates.add(new PieceCoordinate(x + 1, y));
		adjacentCoordinates.add(new PieceCoordinate(x + 1, y - 1));
		adjacentCoordinates.add(new PieceCoordinate(x, y - 1));
		adjacentCoordinates.add(new PieceCoordinate(x - 1, y));
		adjacentCoordinates.add(new PieceCoordinate(x - 1, y + 1));
		return adjacentCoordinates;
	}
	
	
	/**
	 * Calculates the distance between this PieceCoordinate and some other given PieceCoordinate.
	 * Distance is definted as the number of moves it would take to travel between the two coordinates.
	 * @param dest the other PieceCoordinate to calculate the distance between
	 * @return int the number of moves between the two PieceCoordinates.
	 */
	public int distanceFrom(PieceCoordinate dest) {
		
		int yDist = Math.abs(y - dest.y);
		int xDist = Math.abs(x - dest.x);
		int slopeDistance = Math.abs((x + y) - (dest.x + dest.y));
		
		int maxDist = yDist;
		if (xDist > maxDist) maxDist = xDist;
		if (slopeDistance > maxDist) maxDist = slopeDistance;
				
		return maxDist;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PieceCoordinate other = (PieceCoordinate) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

	/**
	 * toString() method for the PieceCoordinate that returns a string with the x and y coordinates
	 * of the PieceCoordinate.
	 */
	public String toString() {
		return "(" + x + ", " + y + ")";
	}


}
