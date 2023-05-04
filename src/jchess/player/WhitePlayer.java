package jchess.player;

import java.util.Collection;

import jchess.board.Board;
import jchess.board.Move;
import jchess.pieces.Alliance;
import jchess.pieces.Piece;

public class WhitePlayer extends Player {
	public WhitePlayer(final Board board,
			final Collection<Move> whiteStandardLegalMoves,
			final Collection<Move> blackStandardLegalMoves) {
		super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}
	
	@Override
	public Player getOpponent() {
		return this.board.blackPlayer();
	}
}
