/**
 * 
 */
package hanto.studentBotelhoLeonard.common;

import hanto.common.HantoCoordinate;

/**
 * @author Hemuro
 *
 */
public class PieceCoordinate implements HantoCoordinate {
	
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
