package sk.stuba.fei.uim.oop.board.pipes;

import sk.stuba.fei.uim.oop.board.Tile;
import sk.stuba.fei.uim.oop.board.pipes.curved.LPipe;
import sk.stuba.fei.uim.oop.board.pipes.straight.IPipe;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public abstract class Pipe extends Tile {

    protected final Random generator;
    protected BufferedImage pipeImage;

    public Pipe(int row, int column) {
        super(row, column);
        this.generator = new Random();
    }

    public abstract void rotate();

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

    private boolean isVerticallyConnected(Pipe neighbor) {
        if (this.getRow() < neighbor.getRow()) {
            if (this instanceof IPipe && ((IPipe) this).getOrientation().isVertical() ||
                    this instanceof LPipe && ((LPipe) this).getDirection().isFacingDown()) {
                return neighbor instanceof IPipe && ((IPipe) neighbor).getOrientation().isVertical() ||
                        neighbor instanceof LPipe && ((LPipe) neighbor).getDirection().isFacingUp();
            }
        } else if (this.getRow() > neighbor.getRow()) {
            if (this instanceof IPipe && ((IPipe) this).getOrientation().isVertical() ||
                    this instanceof LPipe && ((LPipe) this).getDirection().isFacingUp()) {
                return neighbor instanceof IPipe && ((IPipe) neighbor).getOrientation().isVertical() ||
                        neighbor instanceof LPipe && ((LPipe) neighbor).getDirection().isFacingDown();
            }
        }
        return false;
    }

    private boolean isHorizontallyConnected(Pipe neighbor) {
        if (this.getColumn() < neighbor.getColumn()) {
            if (this instanceof IPipe && ((IPipe) this).getOrientation().isHorizontal() ||
                    this instanceof LPipe && ((LPipe) this).getDirection().isFacingRight()) {
                return neighbor instanceof IPipe && ((IPipe) neighbor).getOrientation().isHorizontal() ||
                        neighbor instanceof LPipe && ((LPipe) neighbor).getDirection().isFacingLeft();
            }
        } else if (this.getColumn() > neighbor.getColumn()) {
            if (this instanceof IPipe && ((IPipe) this).getOrientation().isHorizontal() ||
                    this instanceof LPipe && ((LPipe) this).getDirection().isFacingLeft()) {
                return neighbor instanceof IPipe && ((IPipe) neighbor).getOrientation().isHorizontal() ||
                        neighbor instanceof LPipe && ((LPipe) neighbor).getDirection().isFacingRight();
            }
        }
        return false;
    }

    public boolean isConnected(Pipe neighbor) {
        if (this.getColumn() == neighbor.getColumn()) {
            return isVerticallyConnected(neighbor);
        } else if (this.getRow() == neighbor.getRow()) {
            return isHorizontallyConnected(neighbor);
        }
        return false;
    }

}
