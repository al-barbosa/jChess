package jchess.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import jchess.board.Board;
import jchess.board.BoardUtils;
import jchess.board.Move;
import jchess.board.Tile;
import jchess.board.Move.AttackMove;
import jchess.board.Move.MajorMove;

public class Rook extends Piece {
	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -8, -1, 1, 8 };

	public Rook(final Alliance pieceAlliance, final int piecePosition) {
		super(piecePosition, pieceAlliance, PieceType.ROOK);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		
		for (final int candidateCoordinate : CANDIDATE_MOVE_VECTOR_COORDINATES) {
			int candidateDestinationCoordinate = this.piecePosition;
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinate) ||
						isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinate)) {
					break;
				}
				candidateDestinationCoordinate += candidateCoordinate;
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					final Tile candidateDestionationTile = board.getTile(candidateDestinationCoordinate);
					if(!candidateDestionationTile.isTileOccupied()) {
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
					} else {
						final Piece pieceAtDestination = candidateDestionationTile.getPiece();
						final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
						if(this.pieceAlliance != pieceAlliance) {
							legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
						}
						break;
					}
				}
			}
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public String toString() {
		return PieceType.ROOK.toString();
	}
	
	@Override
	public Piece movePiece(Move move) {
		return new Rook(move.getMovedPiece().getPieceAlliance(), move.getDestinationCordinate());
	}

	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 1);
	}
}
