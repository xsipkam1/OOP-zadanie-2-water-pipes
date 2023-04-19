package sk.stuba.fei.uim.oop.board.pipes;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.Tile;

import java.awt.image.BufferedImage;

public abstract class Pipe extends Tile {

    @Getter
    protected int angle;
    protected BufferedImage pipeImage;

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public abstract void setAngle();

}
