package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class GameLogic extends UniversalAdapter {

    @Getter
    private final JLabel boardSizeLabel;
    private final JFrame game;
    private Board currentBoard;
    private int currentBoardSize;

    public GameLogic(JFrame gameFrame) {
        game = gameFrame;
        currentBoardSize = 8;
        initializeBoard();
        game.add(currentBoard);
        boardSizeLabel = new JLabel("CURRENT BOARD SIZE: " + currentBoardSize);
        boardSizeLabel.setFont(new Font("Arial", Font.BOLD, 20));
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
        game.setFocusable(true);
        game.requestFocus();
        game.revalidate();
        game.repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            currentBoardSize = source.getValue();
            boardSizeLabel.setText("CURRENT BOARD SIZE: " + currentBoardSize);
            restartGame();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                game.dispose();
            case KeyEvent.VK_R:
                restartGame();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        restartGame();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Component current = currentBoard.getComponentAt(e.getX(), e.getY());
        if (current instanceof Tile) {
            ((Tile) current).setHighlight(true);
        }
        currentBoard.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Component current = currentBoard.getComponentAt(e.getX(), e.getY());
        if(((Tile) current).getType().equals(Type.PIPE) || ((Tile) current).getType().equals(Type.L_PIPE)){
            ((Tile) current).setAngle();
        }
        currentBoard.repaint();
    }

}
