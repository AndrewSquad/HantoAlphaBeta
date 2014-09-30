package hanto.studentBotelhoLeonard.common;


public class MoveValidatorFactory {

	private static final MoveValidatorFactory instance = new MoveValidatorFactory();

	private MoveValidatorFactory() {}

	public static MoveValidatorFactory getInstance() {
		return instance;
	}
	
	public static MoveValidator makeMoveValidator(MoveType moveType, HantoBoard board) {
		return makeMoveValidator(moveType, Integer.MAX_VALUE, board);
	}


	public static MoveValidator makeMoveValidator(MoveType moveType, int distanceLimit, HantoBoard board) {
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
