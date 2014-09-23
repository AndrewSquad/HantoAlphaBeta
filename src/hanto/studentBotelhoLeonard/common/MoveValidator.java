/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * This interface will be implemented by classes such as WalkValidator and FlyValidator.
 * Each of these classes will determine if a move is valid based on their own individual criteria.
 */
package hanto.studentBotelhoLeonard.common;

public interface MoveValidator {
	
	/**
	 * Determines the legality of moving a piece from one coordinate on the board to another.
	 * @param from - the starting coordinate
	 * @param to - the destination coordinate
	 * @return boolean indicating whether or not the proposed move is valid
	 */
	public boolean isMoveLegal(PieceCoordinate from, PieceCoordinate to);

}
