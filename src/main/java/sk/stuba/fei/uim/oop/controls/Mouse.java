package sk.stuba.fei.uim.oop.controls;

import sk.stuba.fei.uim.oop.board.Tile;
import sk.stuba.fei.uim.oop.board.pipes.Pipe;
import sk.stuba.fei.uim.oop.controls.adapters.MouseAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {
    private final GameLogic game;

    public Mouse(GameLogic baseLogic) {
        this.game = baseLogic;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Component current = this.game.getCurrentBoard().getComponentAt(e.getX(), e.getY());
        if (current instanceof Tile) {
            ((Tile) current).setHighlight(true);
            this.game.getCurrentBoard().repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Component current = this.game.getCurrentBoard().getComponentAt(e.getX(), e.getY());
        if (current instanceof Pipe) {
            ((Pipe) current).setAngle();
            ((Pipe) current).setHighlight(true);
            this.game.getCurrentBoard().repaint();
        }
    }

}
