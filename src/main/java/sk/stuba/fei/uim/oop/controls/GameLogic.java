package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.board.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameLogic {

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
        this.currentBoardSize = 8;
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
        ArrayList<Tile> path = this.currentBoard.getPath();
        for (Tile tile : path) {
            tile.setConstantHighlight(false);
        }
        ArrayList<Integer> correctAngles = this.currentBoard.getCorrectAngles(path, currentBoardSize);
        int i = 0;
        for (Tile tile : path) {
            if (tile.getAngle() != correctAngles.get(i)) {
                this.wins = -1;
                return;
            }
            tile.setConstantHighlight(true);
            i++;
        }
        this.wins++;
        this.restartGame();
    }

}
