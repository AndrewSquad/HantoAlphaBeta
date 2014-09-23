package hanto.studentBotelhoLeonard.common;

public class WalkValidator implements MoveValidator {
	
	private int distanceLimit;
	private HantoBoard board;
	
	public WalkValidator(int distanceLimit) {
		this.distanceLimit = distanceLimit;
	}

	@Override
	public boolean isMoveLegal(PieceCoordinate from, PieceCoordinate to) {
		// TODO Auto-generated method stub
		return false;
	}

}
