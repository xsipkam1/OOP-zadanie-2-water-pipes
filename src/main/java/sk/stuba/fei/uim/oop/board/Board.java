package sk.stuba.fei.uim.oop.board;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class Board extends JPanel {

    private Tile[][] grid;

    public Board(int size) {
        this.initializeGrid(size);
        this.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        this.setBackground(Color.CYAN);
    }

    private void initializeGrid(int size) {
        this.setLayout(new GridLayout(size, size));
        this.grid = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Tile tile = new Tile();
                if (new Random().nextInt(5) == 0) {
                    tile.setType(Type.PIPE);
                } else {
                    if (new Random().nextInt(5) == 0) {
                        tile.setType(Type.L_PIPE);
                    } else {
                        tile.setType(Type.EMPTY);
                    }
                }
                this.grid[i][j] = tile;
                this.add(this.grid[i][j]);
            }
        }
    }

}
