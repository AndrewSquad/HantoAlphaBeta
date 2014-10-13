package hanto.studentBotelhoLeonard.tournament;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import hanto.common.HantoException;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentBotelhoLeonard.common.HantoBoard;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;
import hanto.studentBotelhoLeonard.epsilon.EpsilonHantoGame;
import hanto.tournament.HantoGamePlayer;
import hanto.tournament.HantoMoveRecord;

public class HantoPlayer implements HantoGamePlayer {

	private HantoPlayerColor myColor, opponentColor;
	private boolean doIMoveFirst;
	private EpsilonHantoGame game;
	private boolean endNear = false; // When opponents butterfly has 4 pieces around it, set flag
	private boolean moveButterfly = false; // Set flag when my butterfly has 4 neighbors.

	private PieceCoordinate myButterflyLoc = null;
	private PieceCoordinate opponentButterflyLoc = null;


	public HantoPlayer(HantoPlayerColor myColor, boolean doIMoveFirst) {
		this.myColor = myColor;
		this.doIMoveFirst = doIMoveFirst;

		opponentColor = (this.myColor == HantoPlayerColor.RED) ? HantoPlayerColor.BLUE : HantoPlayerColor.RED;  
	}


	@Override
	public void startGame(HantoGameID version, HantoPlayerColor myColor,
			boolean doIMoveFirst) {

		HantoPlayerColor movesFirst = (doIMoveFirst) ? myColor : opponentColor;
		game = new EpsilonHantoGame(movesFirst);
	}


	@Override
	public HantoMoveRecord makeMove(HantoMoveRecord opponentsMove) {

		double stateUtility;
		HantoMoveRecord myMove = null;

		// update our game state for the opponent's last move
		if (opponentsMove != null) {
			try {
				game.makeMove(opponentsMove.getPiece() , opponentsMove.getFrom(), opponentsMove.getTo());
			}
			catch (HantoException e) {
				// If there are no mistakes this will never happen.
				System.out.println("According to our rules, that move was illegal. Therefore we must resign.");
				return new HantoMoveRecord(null, null, null);
			}
			if (opponentsMove.getPiece() == HantoPieceType.BUTTERFLY) opponentButterflyLoc = new PieceCoordinate(opponentsMove.getTo());
		}

		// Our move logic
		if (game.hasPlayerPlacedButterfly(opponentColor)) {
			stateUtility = calculateUtility(new HantoBoard(game.getHantoBoard()));
			setButterflyDanger();
			//Early Game
			// For turn < 5 Place horse, horse, butterfly, horse, horse
			// Horses close to butterfly
			// Butterfly has minimal neighbors, //and has far away as possible from other pieces
			if (game.getTurnCount() < 10) myMove = earlyGameWithButterfly(game.getTurnCount());

			//Mid Game 
			else if (!endNear) myMove = midGame();

			// End Game
			else endGame();
		}

		else { // Early game logic 
			// For turn < 5  Place horse, horse, butterfly, horse, horse randomly
			myMove = earlyGameWithoutButterfly(game.getTurnCount());
		}		

		// Double checking
		try {
			game.makeMove(myMove.getPiece() , myMove.getFrom(), myMove.getTo());
		}
		catch (HantoException e) {
			// If we ever get here, make first safe move (loop)
		}
		if (myMove.getPiece() == HantoPieceType.BUTTERFLY) myButterflyLoc = new PieceCoordinate(myMove.getTo());

		return myMove;
	}


	// This assumes opponents butterfly is in play
	private double calculateUtility(HantoBoard board) {
		double utility = 0.0;

		if (opponentButterflyLoc == null) return 0.0;

		for (PieceCoordinate coord : opponentButterflyLoc.getSixAdjacentCoordinates()) {
			if (game.getHantoBoard().getPieceAt(coord) != null) utility++;
		}

		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = game.getHantoBoard().getBoardMap().entrySet().iterator();
		PieceCoordinate next;
		HantoPlayerColor pieceColor;
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			next = entry.getKey();
			pieceColor = entry.getValue().getColor();
			if (myColor == pieceColor) {
				utility += 1/(next.distanceFrom(opponentButterflyLoc));
			}
		}		
		return utility;
	}

	private HantoMoveRecord earlyGameWithButterfly(int turnCount){
		// will never get turn 0
		PieceCoordinate to = null;
		HantoPieceType piece = (turnCount == 4 || turnCount == 5) ? HantoPieceType.BUTTERFLY : HantoPieceType.HORSE;

		if (turnCount == 1) to = new PieceCoordinate(0, 1);
		else if  (turnCount == 4 || turnCount == 5) to = getWorstAddLocation();
		else to = getOptimalAddLocation();

		return new HantoMoveRecord(piece, null, to);

	}

	private HantoMoveRecord midGame() {
		// Check for should move butterfly
		// Move butterfly if need be
		// Discover optimal move for every piece (non flying)
		// Discover optimal place to put piece
		// Do which is more optimal	
		PieceCoordinate from = null;
		PieceCoordinate to = null;
		//(turnCount == 4 || turnCount == 5) ? HantoPieceType.BUTTERFLY : HantoPieceType.HORSE; CAN'T BE
		HantoPieceType piece = null;

		if (moveButterfly) {
			if (optimalButterflyMove() != null) return optimalButterflyMove();
			else if (freeButterfly() != null) return freeButterfly();
		}




		return new HantoMoveRecord(piece, from, to);

	}

	private HantoMoveRecord freeButterfly() {
		PieceCoordinate from = null;
		PieceCoordinate to = null;
		HantoPieceType pieceType = null;

		ArrayList<PieceCoordinate> piecesAdjToButterfly = new ArrayList<PieceCoordinate>();
		for (PieceCoordinate coord : myButterflyLoc.getSixAdjacentCoordinates()) {
			HantoPiece piece = game.getHantoBoard().getPieceAt(coord);
			if (piece != null && piece.getColor() == myColor) {
				PieceCoordinate poss = new PieceCoordinate(findOptimalPieceMove(coord).getTo());
				if (!poss.isAdjacentTo(myButterflyLoc)) {
					to = poss;
					break;
				}
				piecesAdjToButterfly.add(poss);
			}
		}
		if (piecesAdjToButterfly.size() < 2) { 
			for (PieceCoordinate pieceCoord : piecesAdjToButterfly) {
				PieceCoordinate poss = findFirstSubOptimalMove(pieceCoord);
				if (!to.isAdjacentTo(myButterflyLoc)) {
					to = poss;
					break;
				}
			}
		}
		


		return new HantoMoveRecord(pieceType, from, to);
	}
	
	
	private PieceCoordinate findFirstSubOptimalMove(PieceCoordinate pieceCoord) {
		// If walk, 
		return null;
	}
	
	private HantoMoveRecord findOptimalPieceMove(PieceCoordinate coord) {
		PieceCoordinate from = coord;
		PieceCoordinate to = null;
		HantoPieceType piece = game.getHantoBoard().getPieceAt(coord).getType();
		if (piece == null) return null;
		
		// This depends on walk, fly, jump
		// Walk basically two tile opening move that results in closest to opponent butterfly
		
		// fly as long as it doesn't break continuity, to tile adjacent to opponent butterfly
		
		// jump if doesn't break continuity, to the most optimal place possible
		
		return new HantoMoveRecord(piece, from, to);		
	}

	private HantoMoveRecord optimalButterflyMove() {
		PieceCoordinate from = myButterflyLoc;
		PieceCoordinate to = null;
		HantoPieceType piece = HantoPieceType.BUTTERFLY;


		List<PieceCoordinate> moveLocs;
		PieceCoordinate minNeighborsLoc = null;
		int minNeighbors = Integer.MAX_VALUE;
		//PieceCoordinate move;
		if (!(moveLocs = game.getHantoBoard().getTwoTileOpenings(myButterflyLoc)).isEmpty()) {
			for (PieceCoordinate move : moveLocs) {
				HantoBoard tempBoard = new HantoBoard(game.getHantoBoard());
				tempBoard.moveExistingPiece(from, move, tempBoard.getPieceAt(from));
				int occupiedNeighbors = 0;
				// count occupied neighbors
				for (PieceCoordinate coord : move.getSixAdjacentCoordinates()) {
					if (tempBoard.getPieceAt(coord) != null) occupiedNeighbors++;
				}
				if (occupiedNeighbors < minNeighbors) {
					minNeighbors = occupiedNeighbors;
					minNeighborsLoc = move;
				}

				//return occupiedNeighbors >= 4;
			}
		}
		
		to = minNeighborsLoc;

		return new HantoMoveRecord(piece, from, to);
	}

	private HantoMoveRecord endGame() {
		// Win, if possible move flying pieces next to butterfly
		// Otherwise optimal jumps or walks
		// Still check if optimal piece add > jump or walk
		PieceCoordinate from = null;
		PieceCoordinate to = null;
		//(turnCount == 4 || turnCount == 5) ? HantoPieceType.BUTTERFLY : HantoPieceType.HORSE; CAN'T BE
		HantoPieceType piece = null;

		return new HantoMoveRecord(piece, from, to);

	}

	private HantoMoveRecord earlyGameWithoutButterfly(int turnCount) {
		PieceCoordinate to = null;
		HantoPieceType piece = (turnCount == 4 || turnCount == 5) ? HantoPieceType.BUTTERFLY : HantoPieceType.HORSE;

		// First Turn
		if (turnCount == 0) to = new PieceCoordinate(0, 0);
		else if (turnCount == 1) to = new PieceCoordinate(0, 1);

		//Second and Third
		else to = getRandomAddLocation();

		return new HantoMoveRecord(piece, null, to);

	}

	private PieceCoordinate getOptimalAddLocation() {
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = game.getHantoBoard().getBoardMap().entrySet().iterator();
		PieceCoordinate next;
		HantoPlayerColor pieceColor;
		int minDist = Integer.MAX_VALUE;
		PieceCoordinate minDistCoord = null; 
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			next = entry.getKey();
			pieceColor = entry.getValue().getColor();
			if (myColor == pieceColor) {
				for (PieceCoordinate coord : next.getSixAdjacentCoordinates()) {
					if (!game.getHantoBoard().isAdjacentToOtherColorPiece(coord, myColor)) {
						int distance = coord.distanceFrom(opponentButterflyLoc);
						if (distance < minDist) {
							minDist = distance;
							minDistCoord = coord;
						}
					}
				}
			}
		}
		return minDistCoord;
	}

	private PieceCoordinate getWorstAddLocation() {
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = game.getHantoBoard().getBoardMap().entrySet().iterator();
		PieceCoordinate next;
		HantoPlayerColor pieceColor;
		int maxDist = 0;
		PieceCoordinate maxDistCoord = null; 
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			next = entry.getKey();
			pieceColor = entry.getValue().getColor();
			if (myColor == pieceColor) {
				for (PieceCoordinate coord : next.getSixAdjacentCoordinates()) {
					if (!game.getHantoBoard().isAdjacentToOtherColorPiece(coord, myColor)) {
						int distance = coord.distanceFrom(opponentButterflyLoc);
						if (distance >= maxDist) {
							maxDist = distance;
							maxDistCoord = coord;
						}
					}
				}
			}
		}
		return maxDistCoord;
	}


	private PieceCoordinate getRandomAddLocation() {
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = game.getHantoBoard().getBoardMap().entrySet().iterator();
		PieceCoordinate next;
		HantoPlayerColor pieceColor;
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			next = entry.getKey();
			pieceColor = entry.getValue().getColor();
			if (myColor == pieceColor) {
				for (PieceCoordinate coord : next.getSixAdjacentCoordinates()) {
					if (!game.getHantoBoard().isAdjacentToOtherColorPiece(coord, myColor)) return coord;
				}
			}
		}
		return null;
	}


	private boolean butterflyInDanger(HantoPlayerColor color) {
		int occupiedNeighbors = 0;

		PieceCoordinate butterflyPos = (color == myColor) ? myButterflyLoc : opponentButterflyLoc;

		for (PieceCoordinate coord : butterflyPos.getSixAdjacentCoordinates()) {
			if (isCoordinateOccupied(coord)) occupiedNeighbors++;
		}

		return occupiedNeighbors >= 4;
	}


	private void setButterflyDanger() {
		endNear = butterflyInDanger(opponentColor);
		moveButterfly = butterflyInDanger(myColor);
	}

	private boolean isCoordinateOccupied(PieceCoordinate coord) {
		return game.getHantoBoard().getPieceAt(coord) != null;
	}

}
