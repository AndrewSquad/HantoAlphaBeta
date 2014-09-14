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

	// Coordinates
	private int x;
	private int y;

	/**
	 * Constructor for
	 */
	public PieceCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
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
	
	

}
