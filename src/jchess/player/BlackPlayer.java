package jchess.player;

import java.util.Collection;

import jchess.board.Board;
import jchess.board.Move;
import jchess.pieces.Alliance;
import jchess.pieces.Piece;

public class BlackPlayer extends Player {
	public BlackPlayer(final Board board,
			final Collection<Move> whiteStandardLegalMoves,
			final Collection<Move> blackStandardLegalMoves) {
		super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getBlackPieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}

	@Override
	public Player getOpponent() {
		return this.board.whitePlayer();
	}
}
