/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * 
 * MoveValidator class for validating Fly movements in the Hanto game.
 */

package hanto.studentBotelhoLeonard.common;


/**
 * MoveValidator class for validating Fly movements in the Hanto game.
 */
public class FlyValidator implements MoveValidator {
	
	private int distanceLimit;
	private HantoBoard board;
	
	
	/**
	 * Constructor for a FlyValidator
	 * @param distanceLimit how many hexagons the piece can fly at once.
	 * @param board the HantoBoard that FlyValidator will be interacting with.
	 */
	public FlyValidator(int distanceLimit, HantoBoard board) {
		this.distanceLimit = distanceLimit;
		this.board = board;
	}

	@Override
	public boolean isMoveLegal(PieceCoordinate from, PieceCoordinate to) {
		
		boolean moveLegal = true;
		int distance = from.distanceFrom(to);
		
		// destination can't be farther away than the distanceLimit
		if (distance > distanceLimit) {
			moveLegal = false;
		}
		
		// destination must be empty and adjacent to an occupied tile
		else if (board.isTileAlreadyOccupied(to) || !board.isAdjacentToAnyPiece(to)) moveLegal = false;
		
		return moveLegal;
	}

	@Override
	public boolean existsLegalMove(PieceCoordinate coord) {
		HantoBoard boardCopy = new HantoBoard(board);
		
		boardCopy.getBoardMap().remove(coord);
		return boardCopy.isBoardContiguous();
	}

}
