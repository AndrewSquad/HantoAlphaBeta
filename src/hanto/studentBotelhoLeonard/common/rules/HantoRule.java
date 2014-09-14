/**
 * 
 */
package hanto.studentBotelhoLeonard.common.rules;

import hanto.common.HantoCoordinate;
import hanto.common.HantoPiece;

/**
 * @author Andy Botelho & Andrew Leonard
 *
 */
public interface HantoRule {
	
	// Players must move to adjacent squares
	
	// Players must place new pieces next to a piece of their own color unless it's first turn
	
	
	/**
	 * Given a destination position and source position, this method will validate the move based on the rule.
	 * @param from the source position
	 * @param dest the destination
	 * @param piece the piece the rule is being applied to
	 * @return true if rule is followed, false if rule is broken
	 */
	boolean applyRule(HantoCoordinate from, HantoCoordinate dest, HantoPiece piece);

}
