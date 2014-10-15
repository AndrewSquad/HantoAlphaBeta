/**
 * @author Andy Botelho
 * @author Andrew Leonard
 * 
 * The AI player for our Epsilon Hanto Tournament.
 * Not the dumbest AI on the planet.
 */

package hanto.studentBotelhoLeonard.tournament;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

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
	
	private HantoMoveRecord previousMove = null;


	public HantoPlayer() {

	}


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

		this.myColor = myColor;
		opponentColor = (myColor == HantoPlayerColor.RED) ? HantoPlayerColor.BLUE : HantoPlayerColor.RED;

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
					System.out.println("BotelhoLeonard doesn't have a legal move -> Resign");
					return new HantoMoveRecord(null, null, null);
				}
				else {
					myMove = randomLegalMove();
				}
			}
			if (myMove != null) {
				HantoPieceType thePieceType = myMove.getPiece();
				PieceCoordinate theFrom = null;
				if (myMove.getFrom() != null) theFrom = new PieceCoordinate(myMove.getFrom());
				PieceCoordinate theTo = new PieceCoordinate(myMove.getTo());
				// If you're about to undo your previous move, do a random move
				if (previousMove != null){
					if (myMove.getTo() == previousMove.getFrom()) myMove = randomLegalMove(); 
				}
				game.makeMove(thePieceType, theFrom, theTo);
			}
		}
		catch (HantoException e) {
			System.out.println("BotelhoLeonard AI returned a HantoException when determining move. Calculating safer move...");
			System.out.println(e);
			myMove = randomLegalMove();
			try {
				game.makeMove(myMove.getPiece(), myMove.getFrom(), myMove.getTo());
			}
			catch (HantoException error){
				System.out.println("Could not calculate a safer move! Resigning :(");
				System.out.println(error);
				return new HantoMoveRecord(null, null, null);
			}
		}

		if (myMove == null || myMove.getPiece() == null || myMove.getTo() == null) return new HantoMoveRecord(null, null, null);
		if (myMove.getPiece() == HantoPieceType.BUTTERFLY) myButterflyLoc = new PieceCoordinate(myMove.getTo());
		previousMove = new HantoMoveRecord(myMove.getPiece(), myMove.getFrom(), myMove.getTo());
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
				piece = HantoPieceType.SPARROW;
				return new HantoMoveRecord(piece, from, to);
			}
		}

		List<HantoMoveRecord> optimalMoves = new ArrayList<HantoMoveRecord>();		
		int crabs = game.playerPieceTypeRemaining(myColor, HantoPieceType.CRAB);
		if (crabs > 0) {
			PieceCoordinate optimalAddLoc = getOptimalAddLocation();
			if (optimalAddLoc != null) optimalMoves.add(new HantoMoveRecord(HantoPieceType.CRAB, null, optimalAddLoc));
		}

		Iterator<Map.Entry<PieceCoordinate, HantoPiece>> pieceIter = game.getHantoBoard().getBoardMap().entrySet().iterator();
		PieceCoordinate current;
		while (pieceIter.hasNext()) {
			current = pieceIter.next().getKey();
			HantoPiece aPiece = game.getPieceAt(current);
			if (aPiece.getColor() == myColor && aPiece.getType() != HantoPieceType.SPARROW) {
				HantoMoveRecord record = findOptimalPieceMove(current);
				if (record != null) optimalMoves.add(record);
			}
		}

		if (optimalMoves.size() == 0) {
			HantoMoveRecord mr = endGameLogic();
			if (mr != null) optimalMoves.add(mr);
		}

		return calculateMostOptimal(optimalMoves);

	}

	private HantoMoveRecord calculateMostOptimal(List<HantoMoveRecord> possMoves) {
		HantoBoard boardCopy = new HantoBoard(game.getHantoBoard());

		double maxUtil = 0;
		List<HantoMoveRecord> bestMoves = new ArrayList<HantoMoveRecord>();

		for (HantoMoveRecord possMove : possMoves) {
			PieceCoordinate tempTo = new PieceCoordinate(possMove.getTo());
			PieceCoordinate tempFrom = null;
			if (possMove.getFrom() != null) tempFrom = new PieceCoordinate(possMove.getFrom());

			if (tempFrom != null) {
				boardCopy.getBoardMap().remove(tempFrom);
				boardCopy.getBoardMap().put(tempTo, game.getHantoBoard().getPieceAt(tempFrom));
				double tempUtil = calculateUtility(boardCopy);
				if (tempUtil == maxUtil) {
					bestMoves.add(possMove);
				}
				else if (tempUtil > maxUtil) {
					bestMoves.removeAll(bestMoves);
					maxUtil = tempUtil;
					bestMoves.add(possMove);
				}
				boardCopy.getBoardMap().remove(tempTo);
				boardCopy.getBoardMap().put(tempFrom, game.getHantoBoard().getPieceAt(tempFrom));
			}

			else {
				boardCopy.getBoardMap().put(tempTo, game.getHantoBoard().getPieceAt(tempFrom));
				double tempUtil = calculateUtility(boardCopy);
				if (tempUtil == maxUtil) {
					bestMoves.add(possMove);
				}
				else if (tempUtil > maxUtil) {
					bestMoves.removeAll(bestMoves);
					maxUtil = tempUtil;
					bestMoves.add(possMove);
				}
				boardCopy.getBoardMap().remove(tempTo);
			}

		}

		Random rand = new Random();
		int moveSize = bestMoves.size();
		if (moveSize == 0) return null;
		if (moveSize == 1) return bestMoves.get(0);

		int randIndex = rand.nextInt(Integer.MAX_VALUE);
		int randNum = randIndex % (moveSize-1);
		return bestMoves.get(randNum);
	}

	private HantoMoveRecord freeButterfly() {
		PieceCoordinate from = null;
		PieceCoordinate to = null;
		HantoPieceType pieceType = null;

		List<PieceCoordinate> piecesAdjToButterfly = new ArrayList<PieceCoordinate>();
		for (PieceCoordinate coord : myButterflyLoc.getSixAdjacentCoordinates()) {
			HantoPiece piece = game.getHantoBoard().getPieceAt(coord);
			if (piece != null && piece.getColor() == myColor) {
				HantoMoveRecord mr = findOptimalPieceMove(coord);
				if (mr == null) continue;
				PieceCoordinate poss = new PieceCoordinate(findOptimalPieceMove(coord).getTo());
				if (!poss.isAdjacentTo(myButterflyLoc)) {
					piecesAdjToButterfly.add(poss);
				}				
			}
		}

		for (PieceCoordinate pieceCoord : piecesAdjToButterfly) {
			HantoPiece tempPiece = game.getPieceAt(pieceCoord);
			if (tempPiece == null) continue;
			List<PieceCoordinate> possMoves = findAllMoves(pieceCoord);
			for (PieceCoordinate move : possMoves) {
				if (!to.isAdjacentTo(myButterflyLoc) && game.getValidator(tempPiece.getType()).isMoveLegal(pieceCoord, move)) {
					from = pieceCoord;
					to = move;
					break;
				}
			}
		}
		if (from == null) return null;
		pieceType = game.getPieceAt(from).getType();
		return new HantoMoveRecord(pieceType, from, to);
	}


	private List<PieceCoordinate> findAllMoves(PieceCoordinate pieceCoord) {
		HantoPiece piece = game.getPieceAt(pieceCoord);
		// Concern
		if (piece == null) {
			return null;
		}
		HantoPieceType type = piece.getType();
		MoveValidator validator = game.getValidator(type);
		return validator.allMoves(pieceCoord);
	}

	private HantoMoveRecord findOptimalPieceMove(PieceCoordinate coord) {
		PieceCoordinate from = coord;
		PieceCoordinate to = null;
		HantoPieceType piece = game.getHantoBoard().getPieceAt(coord).getType();
		if (piece == null) return null;

		to = game.getValidator(game.getPieceAt(coord).getType()).optimalMove(coord, opponentButterflyLoc);

		if (to == null) return null;

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
				if (occupiedNeighbors < minNeighbors && game.getValidator(HantoPieceType.BUTTERFLY).isMoveLegal(from, move)) {
					minNeighbors = occupiedNeighbors;
					minNeighborsLoc = move;
				}
			}
		}

		to = minNeighborsLoc;

		if (to == null) return null;

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

	private HantoMoveRecord endGameLogic() {
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

		return null;
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
					if (!game.getHantoBoard().isAdjacentToOtherColorPiece(coord, myColor) && game.getHantoBoard().getPieceAt(coord) == null) {
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
					if (!game.getHantoBoard().isAdjacentToOtherColorPiece(coord, myColor) && game.getHantoBoard().getPieceAt(coord) == null) return coord;
				}
			}
		}
		return null;
	}


	private boolean butterflyInDanger(HantoPlayerColor color) {
		int occupiedNeighbors = 0;

		PieceCoordinate butterflyPos = (color == myColor) ? myButterflyLoc : opponentButterflyLoc;
		List<PieceCoordinate> coords = butterflyPos.getSixAdjacentCoordinates();
		for (PieceCoordinate coord : coords) {
			if (game.getHantoBoard().getPieceAt(coord) != null) occupiedNeighbors++;
		}

		return occupiedNeighbors >= 4;
	}


	private void setButterflyDanger() {
		if (opponentButterflyLoc != null) endNear = butterflyInDanger(opponentColor);
		if (myButterflyLoc != null) moveButterfly = butterflyInDanger(myColor);
	}


	// finds any random legal move - last ditch effort to make a legal move if AI fails us :(
	private HantoMoveRecord randomLegalMove() {
		HantoMoveRecord legalMove = null;
		//boolean canIPlacePiece = game.getHantoBoard().canPlayerPlacePiece(myColor);
		PieceCoordinate dest = getRandomAddLocation();

		// place a piece somewhere
		while (dest != null) {
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
			else break; // better not reach here!

			return new HantoMoveRecord(pieceType, null, dest);

		}

		// move an existing piece
		Iterator<Entry<PieceCoordinate, HantoPiece>> pieces = game.getHantoBoard().getBoardMap().entrySet().iterator();
		PieceCoordinate coord;
		HantoPiece piece;
		HantoPlayerColor pieceColor;
		List<HantoMoveRecord> allLegalMoves = new ArrayList<HantoMoveRecord>();
		while(pieces.hasNext()) {
			Entry<PieceCoordinate, HantoPiece> entry = pieces.next();
			coord = entry.getKey();
			piece = entry.getValue();
			pieceColor = piece.getColor();
			if (myColor == pieceColor) {
				MoveValidator validator = game.getValidator(piece.getType());
				if (validator.existsLegalMove(coord)) { // we can move a piece!
					List<PieceCoordinate> possMoves = validator.allMoves(coord);
					Random rand = new Random();
					int moveSize = possMoves.size();
					if (moveSize == 0) continue;
					int randomIndex = rand.nextInt(moveSize - 1);
					// Grab a random move this piece can do, not necessarily an optimal one.
					if (moveSize == 1) {
						//legalMove = new HantoMoveRecord(piece.getType(), coord, possMoves.get(0));
						allLegalMoves.add(new HantoMoveRecord(piece.getType(), coord, possMoves.get(0)));
					}
					else allLegalMoves.add(new HantoMoveRecord(piece.getType(), coord, possMoves.get(randomIndex)));												
				}
			}
		}
		
		Random rand = new Random();
		int moveSize = allLegalMoves.size();
		if (moveSize == 0) return null;
		else if (moveSize == 1) return allLegalMoves.get(0);
		int randomIndex = rand.nextInt(moveSize - 1);
		legalMove = allLegalMoves.get(randomIndex);

		return legalMove;
	}

}
