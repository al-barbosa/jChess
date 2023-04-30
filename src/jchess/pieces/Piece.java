package jchess.pieces;

import java.util.Collection;

import jchess.board.Board;
import jchess.board.Move;

public abstract class Piece {
	protected final int piecePosition;
	protected final Alliance pieceAlliance;
	
	Piece(final int piecePosition, final Alliance pieceAlliance) {
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
	}
	
	public abstract Collection<Move> calculateLegalMoves(final Board board);

	public Alliance getPieceAlliance() {
		return pieceAlliance;
	}
}
