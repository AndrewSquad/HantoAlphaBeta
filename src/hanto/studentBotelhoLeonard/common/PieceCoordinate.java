/**
 * 
 */
package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoCoordinate;

/**
 * @author Andrew & Andrew || Leonard & Botelho
 *
 */
public class PieceCoordinate implements HantoCoordinate {
	
	// Coordinates
	private int x;
	private int y;

	/**
	 * Constructor for coordinates
	 */
	public PieceCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Copy constructor for coordinates (interface to class)
	 */
	public PieceCoordinate(HantoCoordinate coordinate) {
		this.x = coordinate.getX();
		this.y = coordinate.getY();
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
	
	
	public boolean isAdjacentTo(HantoCoordinate otherCoordinate) {
		if (otherCoordinate.equals(this)) return false;
		
		int otherX = otherCoordinate.getX();
		int otherY = otherCoordinate.getY();
		
		int deltaX = Math.abs(otherX - x);
		int deltaY = Math.abs(otherY - y);
		
		boolean xAdjacent = deltaX < 2; 
		boolean yAdjacent = deltaY < 2;
		
		return (xAdjacent && yAdjacent) ; 
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PieceCoordinate other = (PieceCoordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	

}
