package jchess.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jchess.board.Board;
import jchess.board.BoardUtils;
import jchess.board.Move;

public class Pawn extends Piece {
	private final static int[] CANDIDATE_MOVE_COORDINATE = { 8 };
	
	Pawn(int piecePosition, Alliance pieceAlliance) {
		super(piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		for (final int currentCandidate : CANDIDATE_MOVE_COORDINATE) {
			int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidate);
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				continue;
			}
			if(currentCandidate == 8 && board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
				
			}
		}
		return legalMoves;
	}
}
