/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * 
 * The AI player for our Epsilon Hanto Tournament.
 * Not the dumbest AI on the planet.
 */

package hanto.studentBotelhoLeonard.tournament;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import hanto.common.HantoException;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentBotelhoLeonard.common.HantoBoard;
import hanto.studentBotelhoLeonard.common.MoveValidator;
import hanto.studentBotelhoLeonard.common.PieceCoordinate;
import hanto.studentBotelhoLeonard.epsilon.EpsilonHantoGame;
import hanto.tournament.HantoGamePlayer;
import hanto.tournament.HantoMoveRecord;

/**
 * HantoPlayer class for our AI implementation.
 * Once created, this object can start and play a game with another implementation of HantoGamePlayer.
 * makeMove() takes a HantoMoveRecord describing the opponent's last move.  The method then returns a 
 * HantoMoveRecord describing our player's next move.
 *
 */
public class HantoPlayer implements HantoGamePlayer {

	private HantoPlayerColor myColor, opponentColor;
	private EpsilonHantoGame game;
	private boolean endNear = false; // When opponents butterfly has 4 pieces around it, set flag
	private boolean moveButterfly = false; // Set flag when my butterfly has 4 neighbors.

	private PieceCoordinate myButterflyLoc = null;
	private PieceCoordinate opponentButterflyLoc = null;


	/**
	 * Constructor for a HantoPlayer object.
	 * Determines what the player's color is, as well as what the opponent's color is.
	 * @param myColor - this player's color
	 * @param doIMoveFirst - boolean indicating wheter or not this player moves first in the game.
	 */
	public HantoPlayer(HantoPlayerColor myColor, boolean doIMoveFirst) {
		this.myColor = myColor;
		opponentColor = (myColor == HantoPlayerColor.RED) ? HantoPlayerColor.BLUE : HantoPlayerColor.RED;  
	}


	@Override
	public void startGame(HantoGameID version, HantoPlayerColor myColor,
			boolean doIMoveFirst) {

		HantoPlayerColor movesFirst = (doIMoveFirst) ? myColor : opponentColor;
		game = new EpsilonHantoGame(movesFirst);
	}


	@Override
	public HantoMoveRecord makeMove(HantoMoveRecord opponentsMove) {

		HantoMoveRecord myMove = null;

		// update our game state for the opponent's previous move
		if (opponentsMove != null) {
			try {
				game.makeMove(opponentsMove.getPiece(), opponentsMove.getFrom(), opponentsMove.getTo());
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
			setButterflyDanger();
			//Early Game
			// For turn < 5 Place horse, horse, butterfly, horse, horse
			// Horses close to butterfly
			// Butterfly has minimal neighbors, //and has far away as possible from other pieces
			if (game.getTurnCount() < 10) {
				myMove = earlyGameWithButterfly(game.getTurnCount());
			}

			//Mid Game 
			else if (!endNear) {
				myMove = midGame();
			}

			// End Game
			else myMove = endGame();
		}

		else { // Early game logic 
			// For turn < 5  Place horse, horse, butterfly, horse, horse randomly
			myMove = earlyGameWithoutButterfly(game.getTurnCount());
		}		

		// Double checking
		try {
			if ((myMove == null) || (myMove.getPiece() == null && myMove.getFrom() == null && myMove.getTo() == null)) {
				if (!game.playerHasLegalMove(myColor)) {
					return new HantoMoveRecord(null, null, null);
				}
				else {
					myMove = randomLegalMove();
				}
			}
			game.makeMove(myMove.getPiece(), myMove.getFrom(), myMove.getTo());
		}
		catch (HantoException e) {
			return new HantoMoveRecord(null, null, null);
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

		if (turnCount == 1) {
			to = new PieceCoordinate(0, 1);
		}
		else if  (turnCount == 4 || turnCount == 5) {
			to = getWorstAddLocation();
		}
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
			if (optimalButterflyMove() != null) {
				return optimalButterflyMove();
			}
			else if (freeButterfly() != null) return freeButterfly();
		}

		int sparrows = game.playerPieceTypeRemaining(myColor, HantoPieceType.SPARROW);
		if (sparrows > 0) {
			if (getWorstAddLocation() != null) {
				to = getWorstAddLocation();
				return new HantoMoveRecord(piece, from, to);
			}
		}
		
		List<HantoMoveRecord> optimalMoves = new ArrayList<HantoMoveRecord>();
		Iterator<Map.Entry<PieceCoordinate, HantoPiece>> pieceIter = game.getHantoBoard().getBoardMap().entrySet().iterator();
		PieceCoordinate current;
		while (pieceIter.hasNext()) {
			current = pieceIter.next().getKey();
			HantoMoveRecord record = findOptimalPieceMove(current);
			if (record != null) optimalMoves.add(record);
		}
		
		int crabs = game.playerPieceTypeRemaining(myColor, HantoPieceType.CRAB);
		if (crabs > 0) {
			PieceCoordinate optimalAddLoc = getOptimalAddLocation();
			if (optimalAddLoc != null) optimalMoves.add(new HantoMoveRecord(HantoPieceType.CRAB, null, optimalAddLoc));
		}
	
		return calculateMostOptimal(optimalMoves);

	}
	
	private HantoMoveRecord calculateMostOptimal(List<HantoMoveRecord> possMoves) {
		HantoBoard boardCopy = new HantoBoard(game.getHantoBoard());
		
		double maxUtil = 0;
		HantoMoveRecord bestMove = null;
		
		for (HantoMoveRecord possMove : possMoves) {
			PieceCoordinate tempTo = new PieceCoordinate(possMove.getTo());
			PieceCoordinate tempFrom = null;
			if (possMove.getFrom() != null) tempFrom = new PieceCoordinate(possMove.getFrom());
			
			if (tempFrom != null) {
				boardCopy.getBoardMap().remove(tempFrom);
				boardCopy.getBoardMap().put(tempTo, game.getHantoBoard().getPieceAt(tempFrom));
				double tempUtil = calculateUtility(boardCopy);
				if (tempUtil > maxUtil) {
					maxUtil = tempUtil;
					bestMove = possMove;
				}
				boardCopy.getBoardMap().remove(tempTo);
				boardCopy.getBoardMap().put(tempFrom, game.getHantoBoard().getPieceAt(tempFrom));
			}
			
			else {
				boardCopy.getBoardMap().put(tempTo, game.getHantoBoard().getPieceAt(tempFrom));
				double tempUtil = calculateUtility(boardCopy);
				if (tempUtil > maxUtil) {
					maxUtil = tempUtil;
					bestMove = possMove;
				}
				boardCopy.getBoardMap().remove(tempTo);
			}
				
		}
		
		return bestMove;
	}

	private HantoMoveRecord freeButterfly() {
		PieceCoordinate from = null;
		PieceCoordinate to = null;
		HantoPieceType pieceType = null;

		List<PieceCoordinate> piecesAdjToButterfly = new ArrayList<PieceCoordinate>();
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

		for (PieceCoordinate pieceCoord : piecesAdjToButterfly) {
			List<PieceCoordinate> possMoves = findAllMoves(pieceCoord);
			for (PieceCoordinate move : possMoves) {
				if (!to.isAdjacentTo(myButterflyLoc)) {
					to = move;
					break;
				}
			}
		}

		return new HantoMoveRecord(pieceType, from, to);
	}


	private List<PieceCoordinate> findAllMoves(PieceCoordinate pieceCoord) {
		return game.getValidator(game.getPieceAt(pieceCoord).getType()).allMoves(pieceCoord);
	}

	private HantoMoveRecord findOptimalPieceMove(PieceCoordinate coord) {
		PieceCoordinate from = coord;
		PieceCoordinate to = null;
		HantoPieceType piece = game.getHantoBoard().getPieceAt(coord).getType();
		if (piece == null) return null;

		to = game.getValidator(game.getPieceAt(coord).getType()).optimalMove(coord, opponentButterflyLoc);

		return new HantoMoveRecord(piece, from, to);		
	}

	private HantoMoveRecord optimalButterflyMove() {
		PieceCoordinate from = myButterflyLoc;
		PieceCoordinate to = null;
		HantoPieceType piece = HantoPieceType.BUTTERFLY;


		List<PieceCoordinate> moveLocs;
		PieceCoordinate minNeighborsLoc = null;
		int minNeighbors = Integer.MAX_VALUE;

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
		
		int sparrows = game.playerPieceTypeRemaining(myColor, HantoPieceType.SPARROW);
		if (sparrows != 0) return midGame();
		
		Iterator<Map.Entry<PieceCoordinate, HantoPiece>> pieceIter = game.getHantoBoard().getBoardMap().entrySet().iterator();
		PieceCoordinate current;

		while (pieceIter.hasNext()) {
			current = pieceIter.next().getKey();
			HantoPiece tempPiece = game.getPieceAt(current);
			if (tempPiece.getColor() == myColor && tempPiece.getType() == HantoPieceType.SPARROW) {
				// If not adjacent
				if (current.distanceFrom(opponentButterflyLoc) > 2) {
					HantoMoveRecord aMove = findOptimalPieceMove(current);
					if (aMove != null) return aMove;
				}
			}
			
		}
			
		return midGame();

	}

	private HantoMoveRecord earlyGameWithoutButterfly(int turnCount) {
		PieceCoordinate to = null;
		HantoPieceType piece = (turnCount == 4 || turnCount == 5) ? HantoPieceType.BUTTERFLY : HantoPieceType.HORSE;

		// First Turn
		if (turnCount == 0) {
			to = new PieceCoordinate(0, 0);
		}
		else if (turnCount == 1) {
			to = new PieceCoordinate(0, 1);
		}

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
	
	
	// finds any random legal move - last ditch effort to make a legal move if AI fails us :(
	private HantoMoveRecord randomLegalMove() {
		HantoMoveRecord legalMove = null;
		boolean canIPlacePiece = game.getHantoBoard().canPlayerPlacePiece(myColor);
		
		// place a piece somewhere
		if (canIPlacePiece) {
			PieceCoordinate dest = getRandomAddLocation();
			HantoPieceType pieceType;
			if (game.playerPieceTypeRemaining(myColor, HantoPieceType.CRAB) > 0) {
				pieceType = HantoPieceType.CRAB;
			}
			else if (game.playerPieceTypeRemaining(myColor, HantoPieceType.HORSE) > 0) {
				pieceType = HantoPieceType.HORSE;
			}
			else if (game.playerPieceTypeRemaining(myColor, HantoPieceType.SPARROW) > 0) {
				pieceType = HantoPieceType.SPARROW;
			}
			else pieceType = null; // better not reach here!
			
			legalMove = new HantoMoveRecord(pieceType, null, dest);
		}
		
		else { // move an existing piece
			Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = game.getHantoBoard().getBoardMap().entrySet().iterator();
			PieceCoordinate coord;
			HantoPiece piece;
			HantoPlayerColor pieceColor;
			while(pieces.hasNext()) {
				Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
				coord = entry.getKey();
				piece = entry.getValue();
				pieceColor = piece.getColor();
				if (myColor == pieceColor) {
					MoveValidator validator = game.getValidator(piece.getType());
					if (validator.existsLegalMove(coord)) { // we can move a piece!
						Random rand = new Random();
						List<PieceCoordinate> possMoves = validator.allMoves(coord);
						int randomIndex = rand.nextInt(possMoves.size());
						legalMove = new HantoMoveRecord(piece.getType(), coord, possMoves.get(randomIndex));												
					}
				}
			}
		}
		
		return legalMove;
	}

}
