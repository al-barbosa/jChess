package jchess.pieces;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import jchess.board.Board;
import jchess.board.BoardUtils;
import jchess.board.Move;
import jchess.board.Move.AttackMove;
import jchess.board.Move.MajorMove;
import jchess.board.Tile;

public class Knight extends Piece{
	private final static int[] CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 17, 15, 10, 6 };
	
	Knight(final int piecePosition, final Alliance pieceAlliance) {
		super(piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int currentCandidate : CANDIDATE_MOVE_COORDINATES) {
			final int candidateDestinationCoordinate = this.piecePosition + currentCandidate;
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				if(isFirstColumnExclusion(this.piecePosition, currentCandidate) ||
						isSecondColumnExclusion(this.piecePosition, currentCandidate) ||
						isSeventhColumnExclusion(this.piecePosition, currentCandidate) ||
						isEighthColumnExclusion(this.piecePosition, currentCandidate)){
					continue;
				}
				final Tile candidateDestionationTile = board.getTile(candidateDestinationCoordinate);
				if(!candidateDestionationTile.isTileOccupied()) {
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
				} else {
					final Piece pieceAtDestination = candidateDestionationTile.getPiece();
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
					if(this.pieceAlliance != pieceAlliance) {
						legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}

	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 || candidateOffset == 6 || candidateOffset == 15);
	}
	
	private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
	}
	
	private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6 || candidateOffset == 10 || candidateOffset == 17);
	}
	
}
