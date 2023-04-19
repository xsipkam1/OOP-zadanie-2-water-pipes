package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.board.*;
import sk.stuba.fei.uim.oop.board.pipes.*;
import sk.stuba.fei.uim.oop.gui.GameFrame;

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
    private final GameFrame game;
    @Getter
    private Board currentBoard;
    @Getter @Setter
    private int currentBoardSize;
    @Setter
    private int wins;
    private final BoardInput userBoardInputManager;

    public GameLogic(GameFrame gameFrame) {
        this.game = gameFrame;
        this.userBoardInputManager = new BoardInput(this);
        this.currentBoardSize = INITIAL_BOARD_SIZE;
        this.wins = 0;
        this.initializeBoard();
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
        this.currentBoard.addMouseMotionListener(userBoardInputManager);
        this.currentBoard.addMouseListener(userBoardInputManager);
        this.game.add(currentBoard);
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

    private boolean checkStartCorrectness(Pipe start) {
        if ((start.getRow() != 0 || start.getColumn() != 0)
                && (start.getColumn() != 0 || start.getRow() != currentBoardSize - 1)
                && (start.getColumn() != currentBoardSize - 1 || start.getRow() != 0)) {
            if (start.getRow() == 0) {
                if (start instanceof LPipe) {
                    return start.getAngle() != 270 && start.getAngle() != 0;
                } else if (start instanceof IPipe) {
                    return start.getAngle() == 90;
                }
            } else if (start.getColumn() == 0) {
                if (start instanceof LPipe) {
                    return start.getAngle() != 180 && start.getAngle() != 270;
                } else if (start instanceof IPipe) {
                    return start.getAngle() == 0;
                }
            }
        }
        return true;
    }

    private boolean checkEndCorrectness(Pipe end) {
        if ((end.getRow() != currentBoardSize - 1 || end.getColumn() != currentBoardSize - 1)
                && (end.getColumn() != 0 || end.getRow() != currentBoardSize - 1)
                && (end.getColumn() != currentBoardSize - 1 || end.getRow() != 0)) {
            if (end.getRow() == currentBoardSize - 1) {
                if (end instanceof LPipe) {
                    return end.getAngle() != 90 && end.getAngle() != 180;
                } else if (end instanceof IPipe) {
                    return end.getAngle() == 90;
                }
            } else if (end.getColumn() == currentBoardSize - 1) {
                if (end instanceof LPipe) {
                    return end.getAngle() != 0 && end.getAngle() != 90;
                } else if (end instanceof IPipe) {
                    return end.getAngle() == 0;
                }
            }
        }
        return true;
    }

    private boolean checkVerticalConnection(Pipe current, Pipe neighbor) {
        if (current.getRow() < neighbor.getRow()) {
            if (current instanceof IPipe && current.getAngle() == 90 ||
                    current instanceof LPipe && current.getAngle() == 270 ||
                    current instanceof LPipe && current.getAngle() == 0) {
                return neighbor instanceof IPipe && neighbor.getAngle() == 90 ||
                        neighbor instanceof LPipe && neighbor.getAngle() == 90 ||
                        neighbor instanceof LPipe && neighbor.getAngle() == 180;
            }
        } else if (current.getRow() > neighbor.getRow()) {
            if (current instanceof IPipe && current.getAngle() == 90 ||
                    current instanceof LPipe && current.getAngle() == 90 ||
                    current instanceof LPipe && current.getAngle() == 180) {
                return neighbor instanceof IPipe && neighbor.getAngle() == 90 ||
                        neighbor instanceof LPipe && neighbor.getAngle() == 270 ||
                        neighbor instanceof LPipe && neighbor.getAngle() == 0;
            }
        }
        return false;
    }

    private boolean checkHorizontalConnection(Pipe current, Pipe neighbor) {
        if (current.getColumn() < neighbor.getColumn()) {
            if (current instanceof IPipe && current.getAngle() == 0 ||
                    current instanceof LPipe && current.getAngle() == 270 ||
                    current instanceof LPipe && current.getAngle() == 180) {
                return neighbor instanceof IPipe && neighbor.getAngle() == 0 ||
                        neighbor instanceof LPipe && neighbor.getAngle() == 0 ||
                        neighbor instanceof LPipe && neighbor.getAngle() == 90;
            }
        } else if (current.getColumn() > neighbor.getColumn()) {
            if (current instanceof IPipe && current.getAngle() == 0 ||
                    current instanceof LPipe && current.getAngle() == 0 ||
                    current instanceof LPipe && current.getAngle() == 90) {
                return neighbor instanceof IPipe && neighbor.getAngle() == 0 ||
                        neighbor instanceof LPipe && neighbor.getAngle() == 270 ||
                        neighbor instanceof LPipe && neighbor.getAngle() == 180;
            }
        }
        return false;
    }

    private boolean checkConnection(Pipe current, Pipe neighbor) {
        if (current.getColumn() == neighbor.getColumn()) {
            return checkVerticalConnection(current, neighbor);
        } else if (current.getRow() == neighbor.getRow()) {
            return checkHorizontalConnection(current, neighbor);
        }
        return false;
    }

    protected void unhighlightTiles() {
        for (Tile[] tiles : this.currentBoard.getGrid()) {
            for (Tile tile : tiles) {
                tile.setConstantHighlight(false);
            }
        }
    }

    protected void checkWin() {
        ArrayList<Pipe> stack = new ArrayList<>();
        ArrayList<Pipe> visited = new ArrayList<>();
        unhighlightTiles();
        Pipe current = (Pipe) currentBoard.getStart();
        stack.add(current);
        while (!stack.isEmpty()) {
            current = stack.remove(stack.size() - 1);
            visited.add(current);
            if (current.equals(currentBoard.getStart())) {
                if (!checkStartCorrectness(current)) {
                    break;
                }
            } else if (current.equals(currentBoard.getEnd())) {
                if (!checkEndCorrectness(current)) {
                    break;
                }
                this.wins++;
                this.restartGame();
                return;
            }
            if (stack.isEmpty()) {
                current.setConstantHighlight(true);
            }
            ArrayList<Pipe> pipeNeighbors = current.getPipeNeighbors(currentBoard.getGrid());
            for (Pipe neighbor : pipeNeighbors) {
                if (!visited.contains(neighbor)) {
                    if (checkConnection(current, neighbor)) {
                        stack.add(neighbor);
                    }
                }
            }
        }
        this.wins = -1;
    }

}
