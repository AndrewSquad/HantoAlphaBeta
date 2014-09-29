package hanto.studentBotelhoLeonard.common;

public class FlyValidator implements MoveValidator {
	
	private int distanceLimit;
	private HantoBoard board;
	
	
	public FlyValidator(int distanceLimit, HantoBoard board) {
		this.distanceLimit = distanceLimit;
		this.board = board;
	}

	@Override
	public boolean isMoveLegal(PieceCoordinate from, PieceCoordinate to) {
		
		boolean moveLegal = true;
		int distance = from.distanceFrom(to);
		
		if (distance > distanceLimit) moveLegal = false;
		
		else if (board.isTileAlreadyOccupied(to) || !board.isAdjacentToAnyPiece(to)) moveLegal = false;
		
		return moveLegal;
	}

}
