package sk.stuba.fei.uim.oop.controls;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RestartButton extends JButton implements ActionListener {
    private final GameLogic game;

    public RestartButton(GameLogic gameLogic) {
        super("RESTART");
        this.game=gameLogic;
        this.setFocusable(false);
        this.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.game.setWins(0);
        this.game.restartGame();
    }

}
