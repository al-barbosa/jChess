package jchess.player;

import jchess.board.Board;
import jchess.board.Move;

public class MoveTransition {
	private final Board transitionBoard;
	private final Move move;
	private final MoveStatus moveStatus;
	
	public MoveTransition(Board transitionBoard, Move move, MoveStatus moveStatus) {
		super();
		this.transitionBoard = transitionBoard;
		this.move = move;
		this.moveStatus = moveStatus;
	}

	public MoveStatus getMoveStatus() {
		return this.moveStatus;
	}

	public Board getTransitionBoard() {
		return transitionBoard;
	}
}
