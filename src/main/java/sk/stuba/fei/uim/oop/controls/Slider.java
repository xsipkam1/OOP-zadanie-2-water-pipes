package sk.stuba.fei.uim.oop.controls;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Slider implements ChangeListener {
    private final GameLogic game;

    public Slider(GameLogic baseLogic) {
        this.game = baseLogic;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            this.game.setCurrentBoardSize(source.getValue());
            this.game.getBoardSizeLabel().setText("BOARD SIZE: " + this.game.getCurrentBoardSize());
            this.game.setWins(0);
            this.game.restartGame();
        }
    }

}
