package sk.stuba.fei.uim.oop.controls;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Buttons implements ActionListener {
    private final GameLogic game;
    private final JButton buttonRestart;
    private final JButton buttonCheck;

    public Buttons(GameLogic baseLogic, JButton buttonRestart, JButton buttonCheck) {
        this.game = baseLogic;
        this.buttonRestart = buttonRestart;
        this.buttonCheck = buttonCheck;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonRestart) {
            this.game.setWins(0);
            this.game.restartGame();
        } else if (e.getSource() == buttonCheck) {
            this.game.checkWin();
        }
    }

}
