/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * 
 * MoveValidatorFactory class for making MoveValidator objects.
 */

package hanto.studentBotelhoLeonard.common;


/**
 * MoveValidatorFactory class for making MoveValidator objects.
 */
public class MoveValidatorFactory {

	private static final MoveValidatorFactory instance = new MoveValidatorFactory();

	private MoveValidatorFactory() {
		// Empty constructor
	}

	public static MoveValidatorFactory getInstance() {
		return instance;
	}
	
	/**
	 * Method for creating a MoveValidator of the given type.
	 * This method should be called when there is no distance limit to the type of movement.
	 * @param moveType the type of movement that will be validated (Walk, Fly, etc.)
	 * @param board the HantoBoard that will be interacting with the MoveValidator
	 * @return a MoveValidator
	 */
	public MoveValidator makeMoveValidator(MoveType moveType, HantoBoard board) {
		// call the other makeMoveValidator but give max int for distance limit
		return makeMoveValidator(moveType, Integer.MAX_VALUE, board);
	}


	/**
	 * Method for creating a MoveValidator of the given type.
	 * @param moveType the type of movement (Walk, Fly, etc.)
	 * @param distanceLimit how far the piece will be able to move
	 * @param board the board that the validator will be interacting with.
	 * @return the right type of MoveValidator
	 */
	public MoveValidator makeMoveValidator(MoveType moveType, int distanceLimit, HantoBoard board) {
		MoveValidator validator = null;
		switch(moveType) {
		case WALK:
			validator = new WalkValidator(distanceLimit, board);
			break;
		case FLY:
			validator = new FlyValidator(distanceLimit, board);
			break;
		default:
			break;
		}
		return validator;
	}
}
