package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.board.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameLogic {

    private static final int INITIAL_BOARD_SIZE = 8;
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
        ArrayList<Tile> stack = new ArrayList<>();
        ArrayList<Tile> visited = new ArrayList<>();

        ArrayList<Tile> path = this.currentBoard.getPath();
        for (Tile tile : path) {
            tile.setConstantHighlight(false);
        }

        Tile current = currentBoard.getStart();
        stack.add(current);

        while (!stack.isEmpty()) {
            current = stack.remove(stack.size() - 1);
            if (current.equals(currentBoard.getStart())) {
                if ((current.getRow() != 0 || current.getColumn() != 0) && (current.getColumn() != 0 || current.getRow() != currentBoardSize - 1) && (current.getColumn() != currentBoardSize - 1 || current.getRow() != 0)) {
                    if (current.getRow() == 0) {
                        if (current.getType().equals(Type.L_PIPE)) {
                            if (current.getAngle() == 270 || current.getAngle() == 0) {
                                return;
                            }
                        } else if (current.getType().equals(Type.PIPE)) {
                            if (current.getAngle() != 90) {
                                return;
                            }
                        }
                    } else if (current.getColumn() == 0) {
                        if (current.getType().equals(Type.L_PIPE)) {
                            if (current.getAngle() == 180 || current.getAngle() == 270) {
                                return;
                            }
                        } else if (current.getType().equals(Type.PIPE)) {
                            if (current.getAngle() != 0) {
                                return;
                            }
                        }
                    }
                }
            }
            visited.add(current);

            if (current.equals(currentBoard.getEnd())) {
                if ((current.getRow() != currentBoardSize - 1 || current.getColumn() != currentBoardSize - 1) && (current.getColumn() != 0 || current.getRow() != currentBoardSize - 1) && (current.getColumn() != currentBoardSize - 1 || current.getRow() != 0)) {
                    if (current.getRow() == currentBoardSize - 1) {
                        if (current.getType().equals(Type.L_PIPE)) {
                            if (current.getAngle() == 90 || current.getAngle() == 180) {
                                return;
                            }
                        } else if (current.getType().equals(Type.PIPE)) {
                            if (current.getAngle() != 90) {
                                return;
                            }
                        }
                    } else if (current.getColumn() == currentBoardSize - 1) {
                        if (current.getType().equals(Type.L_PIPE)) {
                            if (current.getAngle() == 0 || current.getAngle() == 90) {
                                return;
                            }
                        } else if (current.getType().equals(Type.PIPE)) {
                            if (current.getAngle() != 0) {
                                return;
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

            ArrayList<Tile> neighbors = current.findAllAdjacentTiles(currentBoard.getPath());

            for (Tile neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    Type currentType = current.getType();
                    Type neighborType = neighbor.getType();
                    if (current.getColumn() == neighbor.getColumn()) {
                        if (current.getRow() < neighbor.getRow()) {
                            if (currentType.equals(Type.PIPE) && current.getAngle()==90 || currentType.equals(Type.L_PIPE) && current.getAngle()==270 || currentType.equals(Type.L_PIPE) && current.getAngle()==0) {
                                if (neighborType.equals(Type.PIPE) && neighbor.getAngle()==90 || neighborType.equals(Type.L_PIPE) && neighbor.getAngle()==90 || neighborType.equals(Type.L_PIPE) && neighbor.getAngle()==180) {
                                    stack.add(neighbor);
                                }
                            }
                        }
                        if (current.getRow() > neighbor.getRow()) {
                            if (currentType.equals(Type.PIPE) && current.getAngle()==90 || currentType.equals(Type.L_PIPE) && current.getAngle()==90 || currentType.equals(Type.L_PIPE) && current.getAngle()==180) {
                                if (neighborType.equals(Type.PIPE) && neighbor.getAngle()==90 || neighborType.equals(Type.L_PIPE) && neighbor.getAngle()==270 || neighborType.equals(Type.L_PIPE) && neighbor.getAngle()==0) {
                                    stack.add(neighbor);
                                }
                            }
                        }
                    } else if (current.getRow() == neighbor.getRow()) {
                        if (current.getColumn() < neighbor.getColumn()) {
                            if (currentType.equals(Type.PIPE) && current.getAngle()==0 || currentType.equals(Type.L_PIPE) && current.getAngle()==270 || currentType.equals(Type.L_PIPE) && current.getAngle()==180) {
                                if (neighborType.equals(Type.PIPE) && neighbor.getAngle()==0 || neighborType.equals(Type.L_PIPE) && neighbor.getAngle()==0 || neighborType.equals(Type.L_PIPE) && neighbor.getAngle()==90) {
                                    stack.add(neighbor);
                                }
                            }
                        }
                        if (current.getColumn() > neighbor.getColumn()) {
                            if (currentType.equals(Type.PIPE) && current.getAngle()==0 || currentType.equals(Type.L_PIPE) && current.getAngle()==0 || currentType.equals(Type.L_PIPE) && current.getAngle()==90) {
                                if (neighborType.equals(Type.PIPE) && neighbor.getAngle()==0 || neighborType.equals(Type.L_PIPE) && neighbor.getAngle()==270 || neighborType.equals(Type.L_PIPE) && neighbor.getAngle()==180) {
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
