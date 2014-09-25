package hanto.studentBotelhoLeonard.common;

public class WalkValidator implements MoveValidator {
	
	private int distanceLimit;
	private HantoBoard board;
	
	public WalkValidator(int distanceLimit, HantoBoard board) {
		this.distanceLimit = distanceLimit;
		this.board = board;
	}

	@Override
	public boolean isMoveLegal(PieceCoordinate from, PieceCoordinate to) {
		boolean isMoveValid = true;
		
		if (!board.existsTwoTileOpening(from)) isMoveValid = false;
		
		
		
		return isMoveValid;
	}

}
