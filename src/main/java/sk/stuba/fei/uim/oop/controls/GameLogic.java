package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.board.*;
import sk.stuba.fei.uim.oop.board.pipes.*;
import sk.stuba.fei.uim.oop.board.pipes.curved.*;
import sk.stuba.fei.uim.oop.board.pipes.straight.*;
import sk.stuba.fei.uim.oop.gui.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameLogic {

    public static final int INITIAL_BOARD_SIZE = 8;
    @Getter
    private final JLabel boardSizeLabel;
    @Getter
    private final JLabel levelLabel;
    @Getter
    private final GameFrame game;
    @Getter
    private Board currentBoard;
    @Getter @Setter
    private int currentBoardSize;
    @Setter
    private int level;
    private final BoardInput userBoardInputManager;

    public GameLogic(GameFrame gameFrame) {
        this.game = gameFrame;
        this.userBoardInputManager = new BoardInput(this);
        this.currentBoardSize = INITIAL_BOARD_SIZE;
        this.level = 1;
        this.initializeBoard();
        this.boardSizeLabel = new JLabel("BOARD SIZE: " + currentBoardSize);
        this.setupLabel(this.boardSizeLabel);
        this.levelLabel = new JLabel("LEVEL: " + level);
        this.setupLabel(this.levelLabel);
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
        this.levelLabel.setText("LEVEL: " + level);
        this.game.setFocusable(true);
        this.game.requestFocus();
        this.game.revalidate();
        this.game.repaint();
    }

    private void unhighlightTiles() {
        for (Tile[] row : this.currentBoard.getGrid()) {
            for (Tile tile : row) {
                if (tile.isConstantHighlight()) {
                    tile.setConstantHighlight(false);
                }
            }
        }
    }

    private boolean isStartCorrect(Pipe start) {
        if (!start.isInCorner(currentBoardSize)) {
            if (start.getRow() == 0) {
                if (start instanceof LPipe) {
                    return !((LPipe) start).getDirection().isFacingDown();
                } else if (start instanceof IPipe) {
                    return ((IPipe) start).getOrientation().isVertical();
                }
            } else if (start.getColumn() == 0) {
                if (start instanceof LPipe) {
                    return !((LPipe) start).getDirection().isFacingRight();
                } else if (start instanceof IPipe) {
                    return ((IPipe) start).getOrientation().isHorizontal();
                }
            }
        }
        return true;
    }

    private boolean isEndCorrect(Pipe end) {
        if (!end.isInCorner(currentBoardSize)) {
            if (end.getRow() == currentBoardSize - 1) {
                if (end instanceof LPipe) {
                    return !((LPipe) end).getDirection().isFacingUp();
                } else if (end instanceof IPipe) {
                    return ((IPipe) end).getOrientation().isVertical();
                }
            } else if (end.getColumn() == currentBoardSize - 1) {
                if (end instanceof LPipe) {
                    return !((LPipe) end).getDirection().isFacingLeft();
                } else if (end instanceof IPipe) {
                    return ((IPipe) end).getOrientation().isHorizontal();
                }
            }
        }
        return true;
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
                if (!isStartCorrect(current)) {
                    break;
                }
            } else if (current.equals(currentBoard.getEnd())) {
                if (!isEndCorrect(current)) {
                    break;
                }
                this.level++;
                this.restartGame();
                return;
            }
            if (stack.isEmpty()) {
                current.setConstantHighlight(true);
            }
            ArrayList<Pipe> pipeNeighbors = current.getPipeNeighbors(currentBoard.getGrid());
            for (Pipe neighbor : pipeNeighbors) {
                if (!visited.contains(neighbor)) {
                    if (current.isConnected(neighbor)) {
                        stack.add(neighbor);
                    }
                }
            }
        }
    }

}
