package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.pipes.IPipe;
import sk.stuba.fei.uim.oop.board.pipes.LPipe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.Random;

public class Board extends JPanel {

    @Getter
    private final ArrayList<Tile> path;
    @Getter
    private Tile[][] grid;
    private BufferedImage background;
    @Getter
    private Tile start;
    @Getter
    private Tile end;
    private final Random generator;

    public Board(int size) {
        this.generator = new Random();
        this.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        this.loadBackground();
        this.initializeGrid(size);
        this.path = generatePath();
        this.drawPath(size);
    }

    private void loadBackground() {
        this.background = null;
        try {
            this.background = ImageIO.read(Board.class.getResourceAsStream("/water.jpg"));
            return;
        } catch (IOException e) {
            System.out.println("Obrazok sa nepodarilo nacitat, nacitavam povodnu farbu pozadia.");
        } catch (IllegalArgumentException e) {
            System.out.println("Obrazok sa nenasiel, nacitavam povodnu farbu pozadia.");
        }
        this.setBackground(Color.CYAN);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.background != null) {
            g.drawImage(this.background, 0, 0, null);
        }
    }

    private void initializeGrid(int size) {
        this.setLayout(new GridLayout(size, size));
        this.grid = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Tile tile = new Tile();
                tile.setRow(i);
                tile.setColumn(j);
                this.grid[i][j] = tile;
                this.add(this.grid[i][j]);
            }
        }
        this.initializeStartEndTiles(size);
    }

    private void initializeStartEndTiles(int size) {
        if (generator.nextInt(2) == 1) {
            this.start = grid[generator.nextInt(size)][0];
            this.end = grid[generator.nextInt(size)][size - 1];
        } else {
            this.start = grid[0][generator.nextInt(size)];
            this.end = grid[size - 1][generator.nextInt(size)];
        }
    }

    private IPipe replaceTileWithIPipe(Tile tile, int size) {
        IPipe pipe = new IPipe();
        pipe.setRow(tile.getRow());
        pipe.setColumn(tile.getColumn());
        pipe.setAngle(this.generator.nextInt(2) * 90);
        int index = tile.getRow() * size + tile.getColumn();
        this.remove(tile);
        this.grid[pipe.getRow()][pipe.getColumn()] = pipe;
        this.add(this.grid[pipe.getRow()][pipe.getColumn()], index);
        return pipe;
    }

    private LPipe replaceTileWithLPipe(Tile tile, int size) {
        LPipe pipe = new LPipe();
        pipe.setRow(tile.getRow());
        pipe.setColumn(tile.getColumn());
        pipe.setAngle(this.generator.nextInt(4) * 90);
        int index = tile.getRow() * size + tile.getColumn();
        this.remove(tile);
        this.grid[pipe.getRow()][pipe.getColumn()] = pipe;
        this.add(this.grid[pipe.getRow()][pipe.getColumn()], index);
        return pipe;
    }

    private void drawPath(int size) {
        for (Tile tile : path) {
            int currentTile = path.indexOf(tile);
            if (currentTile > 0 && currentTile < path.size() - 1) {
                Tile previousTile = path.get(currentTile - 1);
                Tile nextTile = path.get(currentTile + 1);
                if (previousTile.getRow() == nextTile.getRow() || previousTile.getColumn() == nextTile.getColumn()) {
                    replaceTileWithIPipe(tile, size);
                } else {
                    replaceTileWithLPipe(tile, size);
                }
            }
        }

        Tile nextTile = path.get(1);
        if (this.start.getColumn() == 0 && this.start.getRow() == 0
                || this.start.getColumn() == 0 && this.start.getRow() == size - 1
                || this.start.getColumn() == size - 1 && this.start.getRow() == 0) {
            this.start = replaceTileWithIPipe(start, size);
        } else if ((this.start.getColumn() == 0 && nextTile.getRow() != start.getRow())
                || (this.start.getColumn() != 0 && nextTile.getColumn() != start.getColumn())) {
            this.start = replaceTileWithLPipe(start, size);
        } else {
            this.start = replaceTileWithIPipe(start, size);
        }

        Tile previousTile = path.get(path.size() - 2);
        if (this.end.getColumn() == size - 1 && this.end.getRow() == size - 1
                || this.end.getColumn() == 0 && this.end.getRow() == size - 1
                || this.end.getColumn() == size - 1 && this.end.getRow() == 0) {
            this.end = replaceTileWithIPipe(end, size);
        } else if ((this.end.getColumn() == size - 1 && previousTile.getRow() != end.getRow())
                || (this.end.getColumn() != size - 1 && previousTile.getColumn() != end.getColumn())) {
            this.end = replaceTileWithLPipe(end, size);
        } else {
            this.end = replaceTileWithIPipe(end, size);
        }

        this.start.setOpaque(true);
        this.start.setBackground(Color.GRAY);
        this.end.setOpaque(true);
        this.end.setBackground(Color.LIGHT_GRAY);
    }

    private ArrayList<Tile> generatePath() {
        ArrayList<Tile> path = new ArrayList<>();
        ArrayList<Tile> stack = new ArrayList<>();
        stack.add(this.start);
        this.start.setVisited(true);
        while (!stack.isEmpty()) {
            Tile current = stack.get(stack.size() - 1);
            if (current.equals(this.end)) {
                while (current != null) {
                    path.add(current);
                    current = current.getPrevious();
                }
                break;
            }
            ArrayList<Tile> currentNeighbors = current.getNeighbors(this.grid);
            currentNeighbors.removeIf(Tile::isVisited);
            if (currentNeighbors.isEmpty()) {
                stack.remove(stack.size() - 1);
            } else {
                Tile neighbor = currentNeighbors.get(this.generator.nextInt(currentNeighbors.size()));
                neighbor.setVisited(true);
                stack.add(neighbor);
                neighbor.setPrevious(current);
            }
        }
        Collections.reverse(path);
        return path;
    }

}
