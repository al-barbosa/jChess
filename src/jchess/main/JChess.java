package jchess.main;

import jchess.board.Board;
import jchess.gui.Table;

public class JChess {

	public static void main(String[] args) {
		Board board = Board.createStandarBoard();
		
		System.out.println(board);
		
		Table table = new Table();
	}

}
