package sk.stuba.fei.uim.oop.controls;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SizeSlider extends JSlider implements ChangeListener {
    private final GameLogic game;

    public SizeSlider(GameLogic gameLogic) {
        super(JSlider.HORIZONTAL, 8, 10, 8);
        this.setMajorTickSpacing(1);
        this.setMinorTickSpacing(1);
        this.setSnapToTicks(true);
        this.setPaintTicks(true);
        this.setPaintLabels(true);
        this.addChangeListener(this);
        this.game = gameLogic;
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
