package sk.stuba.fei.uim.oop.board.pipes;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.Tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Pipe extends Tile {

    @Getter
    protected int angle;
    protected BufferedImage pipeImage;

    public Pipe(int row, int column) {
        super(row, column);
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public abstract void setAngle();

    public ArrayList<Pipe> getPipeNeighbors(Tile[][] grid) {
        ArrayList<Pipe> pipeNeighbors = new ArrayList<>();
        ArrayList<Tile> neighbors = this.getNeighbors(grid);
        for (Tile tile : neighbors) {
            if (tile instanceof Pipe) {
                pipeNeighbors.add((Pipe) tile);
            }
        }
        return pipeNeighbors;
    }

}
