package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Tile extends JPanel {

    @Setter
    private boolean highlight;
    @Setter
    private boolean constantHighlight;
    @Getter @Setter
    private boolean visited;
    @Getter @Setter
    private int row;
    @Getter @Setter
    private int column;
    @Getter @Setter
    private Tile previous;

    public Tile(int row, int column) {
        this.row = row;
        this.column = column;
        this.highlight = false;
        this.constantHighlight = false;
        this.visited = false;
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setOpaque(false);
        this.previous = null;
    }

    public ArrayList<Tile> getNeighbors(Tile[][] grid) {
        ArrayList<Tile> neighbors = new ArrayList<>();
        if (this.row > 0) {
            neighbors.add(grid[this.row - 1][this.column]);
        }
        if (this.row < grid.length - 1) {
            neighbors.add(grid[this.row + 1][this.column]);
        }
        if (this.column > 0) {
            neighbors.add(grid[this.row][this.column - 1]);
        }
        if (this.column < grid[0].length - 1) {
            neighbors.add(grid[this.row][this.column + 1]);
        }
        return neighbors;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.highlight || this.constantHighlight) {
            ((Graphics2D) g).setStroke(new BasicStroke(3));
            if (this.constantHighlight) {
                g.setColor(Color.BLUE);
            }
            if (this.highlight) {
                g.setColor(Color.RED);
            }
            g.drawRect((int) (this.getWidth() * 0.025), (int) (this.getHeight() * 0.025),
                    (int) (this.getWidth() * 0.96), (int) (this.getHeight() * 0.96));
            this.highlight = false;
        }
    }

}
