package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.board.*;
import sk.stuba.fei.uim.oop.board.pipes.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameLogic {

    public static final int INITIAL_BOARD_SIZE = 8;
    @Getter
    private final JLabel boardSizeLabel;
    @Getter
    private final JLabel winsLabel;
    @Getter
    private final JFrame game;
    @Getter
    private Board currentBoard;
    @Getter @Setter
    private int currentBoardSize;
    @Setter
    private int wins;

    public GameLogic(JFrame gameFrame) {
        this.game = gameFrame;
        this.currentBoardSize = INITIAL_BOARD_SIZE;
        this.wins = 0;
        this.initializeBoard();
        this.game.add(currentBoard);
        this.boardSizeLabel = new JLabel("BOARD SIZE: " + currentBoardSize);
        this.setupLabel(this.boardSizeLabel);
        this.winsLabel = new JLabel("WIN STREAK: " + wins);
        this.setupLabel(this.winsLabel);
    }

    private void setupLabel(JLabel label) {
        label.setFont(new Font("Calibri", Font.BOLD, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void initializeBoard() {
        this.currentBoard = new Board(currentBoardSize);
        this.currentBoard.addMouseMotionListener(new Mouse(this));
        this.currentBoard.addMouseListener(new Mouse(this));
    }

    protected void restartGame() {
        this.game.remove(currentBoard);
        this.initializeBoard();
        this.game.add(currentBoard);
        this.winsLabel.setText("WIN STREAK: " + wins);
        this.game.setFocusable(true);
        this.game.requestFocus();
        this.game.revalidate();
        this.game.repaint();
    }

    protected void checkWin() {
        ArrayList<Pipe> stack = new ArrayList<>();
        ArrayList<Pipe> visited = new ArrayList<>();

        for (Tile[] tiles : this.currentBoard.getGrid()) {
            for (Tile tile : tiles) {
                tile.setConstantHighlight(false);
            }
        }

        Pipe current = (Pipe)currentBoard.getStart();
        stack.add(current);

        while (!stack.isEmpty()) {
            current = stack.remove(stack.size() - 1);
            if (current.equals(currentBoard.getStart())) {
                if ((current.getRow() != 0 || current.getColumn() != 0) && (current.getColumn() != 0 || current.getRow() != currentBoardSize - 1) && (current.getColumn() != currentBoardSize - 1 || current.getRow() != 0)) {
                    if (current.getRow() == 0) {
                        if (current instanceof LPipe) {
                            if (current.getAngle() == 270 || current.getAngle() == 0) {
                                break;
                            }
                        } else if (current instanceof IPipe) {
                            if (current.getAngle() != 90) {
                                break;
                            }
                        }
                    } else if (current.getColumn() == 0) {
                        if (current instanceof LPipe) {
                            if (current.getAngle() == 180 || current.getAngle() == 270) {
                                break;
                            }
                        } else if (current instanceof IPipe) {
                            if (current.getAngle() != 0) {
                                break;
                            }
                        }
                    }
                }
            }
            visited.add(current);

            if (current.equals(currentBoard.getEnd())) {
                if ((current.getRow() != currentBoardSize - 1 || current.getColumn() != currentBoardSize - 1) && (current.getColumn() != 0 || current.getRow() != currentBoardSize - 1) && (current.getColumn() != currentBoardSize - 1 || current.getRow() != 0)) {
                    if (current.getRow() == currentBoardSize - 1) {
                        if (current instanceof LPipe) {
                            if (current.getAngle() == 90 || current.getAngle() == 180) {
                                break;
                            }
                        } else if (current instanceof IPipe) {
                            if (current.getAngle() != 90) {
                                break;
                            }
                        }
                    } else if (current.getColumn() == currentBoardSize - 1) {
                        if (current instanceof LPipe) {
                            if (current.getAngle() == 0 || current.getAngle() == 90) {
                                break;
                            }
                        } else if (current instanceof IPipe) {
                            if (current.getAngle() != 0) {
                                break;
                            }
                        }
                    }
                }
                this.wins++;
                this.restartGame();
                return;
            }

            if (stack.isEmpty()) {
                current.setConstantHighlight(true);
            }

            ArrayList<Tile> neighbors = current.getNeighbors(currentBoard.getGrid());

            ArrayList<Pipe> pipes = new ArrayList<>();
            for (Tile tile : neighbors) {
                if (tile instanceof Pipe) {
                    pipes.add((Pipe) tile);
                }
            }

            for (Pipe neighbor : pipes) {
                if (!visited.contains(neighbor)) {
                    if (current.getColumn() == neighbor.getColumn()) {
                        if (current.getRow() < neighbor.getRow()) {
                            if (current instanceof IPipe && current.getAngle()==90
                                    || current instanceof LPipe && current.getAngle()==270
                                    || current instanceof LPipe && current.getAngle()==0) {
                                if (neighbor instanceof IPipe && neighbor.getAngle()==90
                                        || neighbor instanceof LPipe && neighbor.getAngle()==90
                                        || neighbor instanceof LPipe && neighbor.getAngle()==180) {
                                    stack.add(neighbor);
                                }
                            }
                        }
                        if (current.getRow() > neighbor.getRow()) {
                            if (current instanceof IPipe && current.getAngle()==90
                                    || current instanceof LPipe && current.getAngle()==90
                                    || current instanceof LPipe && current.getAngle()==180) {
                                if (neighbor instanceof IPipe && neighbor.getAngle()==90
                                        || neighbor instanceof LPipe && neighbor.getAngle()==270
                                        || neighbor instanceof LPipe && neighbor.getAngle()==0) {
                                    stack.add(neighbor);
                                }
                            }
                        }
                    } else if (current.getRow() == neighbor.getRow()) {
                        if (current.getColumn() < neighbor.getColumn()) {
                            if (current instanceof IPipe && current.getAngle()==0
                                    || current instanceof LPipe && current.getAngle()==270
                                    || current instanceof LPipe && current.getAngle()==180) {
                                if (neighbor instanceof IPipe && neighbor.getAngle()==0
                                        || neighbor instanceof LPipe && neighbor.getAngle()==0
                                        || neighbor instanceof LPipe && neighbor.getAngle()==90) {
                                    stack.add(neighbor);
                                }
                            }
                        }
                        if (current.getColumn() > neighbor.getColumn()) {
                            if (current instanceof IPipe && current.getAngle()==0
                                    || current instanceof LPipe && current.getAngle()==0
                                    || current instanceof LPipe && current.getAngle()==90) {
                                if (neighbor instanceof IPipe && neighbor.getAngle()==0
                                        || neighbor instanceof LPipe && neighbor.getAngle()==270
                                        || neighbor instanceof LPipe && neighbor.getAngle()==180) {
                                    stack.add(neighbor);
                                }
                            }
                        }
                    }
                }
            }

        }
        this.wins=-1;
    }

}
