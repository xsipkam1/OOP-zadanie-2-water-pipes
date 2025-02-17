package sk.stuba.fei.uim.oop.controls;

import sk.stuba.fei.uim.oop.controls.adapters.KeyboardAdapter;

import java.awt.event.KeyEvent;

public class FrameInput extends KeyboardAdapter {
    private final GameLogic game;

    public FrameInput(GameLogic baseLogic) {
        this.game = baseLogic;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                this.game.getGame().dispose();
                System.exit(0);
            case KeyEvent.VK_R:
                this.game.setLevel(1);
                this.game.restartGame();
                break;
            case KeyEvent.VK_ENTER:
                this.game.checkWin();
                break;
        }
    }
}
