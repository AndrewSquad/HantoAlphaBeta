/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * This interface will be implemented by classes such as WalkValidator and FlyValidator.
 * Each of these classes will determine if a move is valid based on their own individual criteria.
 */
package hanto.studentBotelhoLeonard.common;

import java.util.List;




/**
 * Interface for different MoveValidator implementations (Walk, Fly, etc).
 * The main functionality of a MoveValidator is to determine if a given move is legal for a certain
 * type of movement given the current state of the board.
 * Every kind of MoveValidator must have an isMoveLegal() method.
 */
public interface MoveValidator {
	
	/**
	 * Determines the legality of moving a piece from one coordinate on the board to another.
	 * @param from - the starting coordinate
	 * @param to - the destination coordinate
	 * @return boolean indicating whether or not the proposed move is valid
	 */
	boolean isMoveLegal(PieceCoordinate from, PieceCoordinate to);

	/**
	 * Determines if a legal move can be made from the given coordinate 
	 * based on the implementation rules of the MoveValidator.
	 * @param coord PieceCoordinate to determine if there is a legal move from
	 * @return boolean indicating whether or not a legal move exists
	 */
	boolean existsLegalMove(PieceCoordinate coord);
	
	/**
	 * (Used by AI) Determines the optimal move from a given coordinate where the target is the 
	 * given target coordinate.
	 * @param from PieceCoordinate start point
	 * @param target PieceCoordinate target
	 * @return Optimal destination PieceCoordinate
	 */
	PieceCoordinate optimalMove(PieceCoordinate from, PieceCoordinate target);
	
	/**
	 * (Used by AI) Determines all legal moves that can be made from a given PieceCoordinate based on 
	 * the implementation rules of the MoveValidator.
	 * @param from PieceCoordinate start point
	 * @return List of all legal moves from the given start coordinate
	 */
	List<PieceCoordinate> allMoves(PieceCoordinate from);

}
