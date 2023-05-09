package jchess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.google.common.collect.Lists;

import jchess.board.Board;
import jchess.board.BoardUtils;
import jchess.board.Move;
import jchess.board.Tile;
import jchess.pieces.Piece;
import jchess.player.MoveTransition;


public class Table {
	private final JFrame gameFrame;
//	private final GameHistory gameHistory;
//	private final TakenPiecesPanel takenPiecesPanel;
	private final BoardPanel boardPanel;
	private final MoveLog moveLog;
	private boolean highlightMoves;
	private Board chessBoard;
	
	private Tile sourceTile;
	private Tile destinationTile;	
	private Piece humanMovedPiece;
	private BoardDirection boardDirection;
	
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
	private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);	
	private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
	private static String defautPieceImagesPath = "images/pieces/";
	
	public Table() {
		this.boardDirection = BoardDirection.NORMAL;
		this.highlightMoves = true;
		this.chessBoard = Board.createStandarBoard();
		this.gameFrame = new JFrame("JChess");
		this.gameFrame.setLayout(new BorderLayout());
		this.gameFrame.setJMenuBar(createTableMenuBar());
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
//		this.gameHistory = new GameHistory();
//		this.takenPiecesPanel = new TakenPiecesPanel();
		
		this.boardPanel = new BoardPanel();
//		this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
//		this.gameFrame.add(this.gameHistory, BorderLayout.EAST);
		
		this.gameFrame.setVisible(true);
		this.moveLog = new MoveLog();
	}

	private JMenuBar createTableMenuBar() {
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createPreferencesMenu());
		return tableMenuBar;
	}

	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem openPGN = new JMenuItem("Load PGN File");
		openPGN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Open up!!!");
			}
		});
		fileMenu.add(openPGN);
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);
		return fileMenu;
	}
	
	private JMenu createPreferencesMenu() {
		final JMenu preferencesMenu = new JMenu("Preferences");
		final JMenuItem flipBoard = new JMenuItem("Flip Board");
		flipBoard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boardDirection = boardDirection.opposite();
				boardPanel.drawBoard(chessBoard);
			}
		});
		preferencesMenu.add(flipBoard);
		
		preferencesMenu.addSeparator();
		
		final JMenuItem showMoves = new JCheckBoxMenuItem("Show Moves", true);
		showMoves.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				highlightMoves = !highlightMoves;
			}
		});
		preferencesMenu.add(showMoves);
		return preferencesMenu;
	}
	
	public enum BoardDirection {
		NORMAL {
			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				return boardTiles;
			}

			@Override
			BoardDirection opposite() {
				return FLIPPED;
			}
		},
		FLIPPED {
			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				return Lists.reverse(boardTiles);
			}

			@Override
			BoardDirection opposite() {
				return NORMAL;
			}
		};
		
		abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
		abstract BoardDirection opposite();
	}
	
	private class BoardPanel extends JPanel {
		final List<TilePanel> boardTiles;
		
		BoardPanel() {
			super(new GridLayout(8, 8));
			this.boardTiles = new ArrayList<>();
			for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
				final TilePanel tilePanel = new TilePanel(this, i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}

		public void drawBoard(Board board) {
			removeAll();
			for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
				tilePanel.drawTile(board);
				add(tilePanel);
			}
			validate();
			repaint();
		}
	}
	
	public static class MoveLog {
		private final List<Move> moves;
		
		MoveLog() {
			this.moves = new ArrayList<>();
		}
		
		public List<Move> getMoves() {
			return this.moves;
		}
		
		public void addMove(final Move move) {
			this.moves.add(move);
		}
		
		public int size() {
			return this.moves.size();
		}
		
		public void clear() {
			this.moves.clear();
		}
		
		public Move removeMove(int index) {
			return this.moves.remove(index);
		}
		
		public boolean removeMove(final Move move) {
			return this.moves.remove(move);
		}
	}
	
	private class TilePanel extends JPanel {
		private final int tileId;
		private final Color lightTileColor = Color.decode("#FFFACD");
		private final Color darkTileColor = Color.decode("#593E1A");
		
		TilePanel(final BoardPanel boardPanel, final int tileId) {
			super(new GridBagLayout());
			this.tileId = tileId;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignTilePieceIcon(chessBoard);
			
			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(final MouseEvent e) {
					if(SwingUtilities.isRightMouseButton(e)) {
						sourceTile = null;
						destinationTile = null;
						humanMovedPiece = null;
					} else if(SwingUtilities.isLeftMouseButton(e)) {
						if(sourceTile == null) {
							sourceTile = chessBoard.getTile(tileId);
							humanMovedPiece = sourceTile.getPiece();
							if(humanMovedPiece == null) {
								sourceTile = null;
							}
						} else {
							destinationTile = chessBoard.getTile(tileId);
							final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
							final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
							if(transition.getMoveStatus().isDone()) {
								chessBoard = transition.getTransitionBoard();
								moveLog.addMove(move);
							}
							sourceTile = null;
							destinationTile = null;
							humanMovedPiece = null;
						}
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
//								gameHistory.redo(chessBoard, moveLog);
//								takenPiecesPanel.redo(moveLog);
								boardPanel.drawBoard(chessBoard);
							}
						});
					}
				}
				
				@Override
				public void mousePressed(final MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(final MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(final MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(final MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			
			validate();
		}
		
		public void drawTile(Board board) {
			assignTileColor();
			assignTilePieceIcon(board);
			validate();
			repaint();
			highlightLegals(board);
		}

		private void assignTilePieceIcon(final Board board) {
			this.removeAll();
			if(board.getTile(this.tileId).isTileOccupied()) {
				try {
					final BufferedImage image =
							ImageIO.read(new File(defautPieceImagesPath +
									board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1) +
									board.getTile(this.tileId).getPiece().toString() +
									".gif"));					
					add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void highlightLegals(final Board board) {
			if(highlightMoves) {
				for(final Move move : pieceLegalMoves(board)) {
					if(move.getDestinationCoordinate() == this.tileId) {
						try {
							add(new JLabel(new ImageIcon(ImageIO.read(new File("images/etc/green_dot.png")))));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		private Collection<Move> pieceLegalMoves(Board board) {
			if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
				return humanMovedPiece.calculateLegalMoves(board);
			}
			return Collections.emptyList();
		}

		private void assignTileColor() {
			if (BoardUtils.EIGHTH_RANK[this.tileId] ||
					BoardUtils.SIXTH_RANK[this.tileId] ||
					BoardUtils.FOURTH_RANK[this.tileId] ||
					BoardUtils.SECOND_RANK[this.tileId]) {
				setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
			} else if (BoardUtils.SEVENTH_RANK[this.tileId] ||
					BoardUtils.FIFTH_RANK[this.tileId] ||
					BoardUtils.THIRD_RANK[this.tileId] ||
					BoardUtils.FIRST_RANK[this.tileId]) {
				setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
			}
		}
	}
}
