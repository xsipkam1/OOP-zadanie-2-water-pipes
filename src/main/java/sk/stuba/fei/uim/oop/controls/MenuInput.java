package sk.stuba.fei.uim.oop.controls;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuInput implements ActionListener, ChangeListener {

    private final GameLogic game;
    private final JButton buttonRestart;
    private final JButton buttonCheck;

    public MenuInput(GameLogic baseLogic, JButton buttonRestart, JButton buttonCheck) {
        this.game = baseLogic;
        this.buttonRestart = buttonRestart;
        this.buttonCheck = buttonCheck;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonRestart) {
            this.game.setLevel(1);
            this.game.restartGame();
        } else if (e.getSource() == buttonCheck) {
            this.game.checkWin();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            this.game.setCurrentBoardSize(source.getValue());
            this.game.getBoardSizeLabel().setText("BOARD SIZE: " + this.game.getCurrentBoardSize());
            this.game.setLevel(1);
            this.game.restartGame();
        }
    }

}
