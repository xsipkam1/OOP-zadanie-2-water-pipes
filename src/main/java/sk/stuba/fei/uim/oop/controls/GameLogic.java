package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameLogic extends UniversalAdapter {

    @Getter
    private final JLabel boardSizeLabel;
    @Getter
    private final JLabel winsLabel;
    private final JFrame game;
    private Board currentBoard;
    private int currentBoardSize;
    private int wins;

    public GameLogic(JFrame gameFrame) {
        game = gameFrame;
        currentBoardSize = 8;
        wins = 0;
        initializeBoard();
        game.add(currentBoard);
        boardSizeLabel = new JLabel("CURRENT BOARD SIZE: " + currentBoardSize);
        boardSizeLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        winsLabel = new JLabel("WIN STREAK: " + wins);
        winsLabel.setFont(new Font("Calibri", Font.BOLD, 20));
    }

    private void initializeBoard() {
        currentBoard = new Board(currentBoardSize);
        currentBoard.addMouseMotionListener(this);
        currentBoard.addMouseListener(this);
    }

    private void restartGame() {
        game.remove(currentBoard);
        initializeBoard();
        game.add(currentBoard);
        winsLabel.setText("WIN STREAK: " + wins);
        game.setFocusable(true);
        game.requestFocus();
        game.revalidate();
        game.repaint();
    }

    private void checkWin() {
        ArrayList<Tile> path = currentBoard.getPath();
        ArrayList<Integer> correctAngles = currentBoard.getCorrectAngles(path, currentBoardSize);
        int i = 0;
        for (Tile tile : path) {
            if (tile.getAngle() != correctAngles.get(i)) {
                wins=-1;
                return;
            }
            tile.setConstantHighlight(true);
            i++;
        }
        wins++;
        restartGame();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            currentBoardSize = source.getValue();
            boardSizeLabel.setText("CURRENT BOARD SIZE: " + currentBoardSize);
            wins=0;
            restartGame();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                game.dispose();
            case KeyEvent.VK_R:
                wins=0;
                restartGame();
                break;
            case KeyEvent.VK_ENTER:
                checkWin();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        wins=0;
        restartGame();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Component current = currentBoard.getComponentAt(e.getX(), e.getY());
        if (current instanceof Tile) {
            ((Tile) current).setHighlight(true);
            currentBoard.repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Component current = currentBoard.getComponentAt(e.getX(), e.getY());
        if (current instanceof Tile) {
            if (((Tile) current).getType().equals(Type.PIPE) || ((Tile) current).getType().equals(Type.L_PIPE)) {
                ((Tile) current).setAngle();
            }
            currentBoard.repaint();
        }
    }

}
