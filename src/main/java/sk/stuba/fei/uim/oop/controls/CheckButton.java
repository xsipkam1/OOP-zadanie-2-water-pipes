package sk.stuba.fei.uim.oop.controls;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckButton extends JButton implements ActionListener {
    private static final String buttonName = "CHECK";
    private final GameLogic game;

    public CheckButton(GameLogic gameLogic) {
        super(buttonName);
        this.game = gameLogic;
        this.setFocusable(false);
        this.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.game.checkWin();
    }

}
