/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * 
 * MoveValidator class for validating Walk movements in the Hanto game.
 */

package hanto.studentBotelhoLeonard.common;

/**
 * WalkValidator class is used to validate Walk movements for any HantoPiece.
 */
public class JumpValidator implements MoveValidator {
	
	private HantoBoard board;	
	
	/**
	 * Constructor for a WalkValidator object
	 * @param distanceLimit how far the piece is allowed to walk
	 * @param board the board that the WalkValidator will be interacting with.
	 */
	public JumpValidator(int distanceLimit, HantoBoard board) {
		this.board = board;
	}

	@Override
	public boolean isMoveLegal(PieceCoordinate from, PieceCoordinate to) {
		
		if (board.getPieceAt(to) != null) {
			return false;
		}
		else if (!isStraightLine(from, to)) {
			return false;
		}
		else {
			return piecesInWholePath(from, to);
		}
		
	}
	
	
	@Override
	public boolean existsLegalMove(PieceCoordinate coord) {
		// If it has any neighbor it can jump. If the board is contiguous after jump, move is legal
		if (board.isAdjacentToAnyPiece(coord)) {
			HantoBoard boardCopy = new HantoBoard(board);
			boardCopy.getBoardMap().remove(coord);
			return boardCopy.isBoardContiguous();
		}
		return false;
	}
	
	private boolean piecesInWholePath(PieceCoordinate from, PieceCoordinate to) {
		// Slope
		// 3 Possibilities
		// deltaX = 0 and deltaY != 0
		// deltaY = 0 and deltaX != 0
		// deltaY = -deltaX
		int deltaX = to.getX() - from.getX();
		if (deltaX != 0 ) deltaX = deltaX > 1 ? 1 : -1;
		int deltaY = to.getY() - from.getY();
		if (deltaY != 0 ) deltaY = deltaY > 1 ? 1 : -1;
		
		PieceCoordinate tempCoord = new PieceCoordinate(from.getX(), from.getY());
		
		boolean piecesInPath = true;
		
		// In 2/3 cases, using deltaY will get a number of iterations
		// In the case where Y remains constant, only X changes and therefore can be used.
		int i = (deltaY != 0) ? from.getY() : from.getX();
		
		// When x doesn't change, subtract delta y, when it does, add delta y to y.
		int multY = (deltaX == 0 && deltaY == -1) ? -1 : 1;
		
		// Stop looping when from + i = destination
		while (i != ((deltaY != 0) ? to.getY() - multY*deltaY : to.getX() - deltaX)) { // i != destination
			tempCoord = new PieceCoordinate(tempCoord.getX() + deltaX, tempCoord.getY() + deltaY);
			if (null == board.getPieceAt(tempCoord)) return false;
			i += (deltaY != 0) ? deltaY : deltaX; // i++ or i--
		}
		
		return piecesInPath;
	}
	
	private boolean isStraightLine(PieceCoordinate from, PieceCoordinate to) {
		int deltaX = to.getX() - from.getX();
		int deltaY = to.getY() - from.getY();
		
		if (deltaX == 0 && deltaY != 0) return true; // only y changed
		if (deltaY == 0 && deltaX != 0) return true; // only x changed
		if (deltaX != deltaY && Math.abs(deltaX) == Math.abs(deltaY)) return true; // change in x and y is equal and opposite
		
		
		return false;
	}	
}
