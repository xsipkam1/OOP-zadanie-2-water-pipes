package sk.stuba.fei.uim.oop.board;

import lombok.Getter;

import sk.stuba.fei.uim.oop.board.pipes.*;
import sk.stuba.fei.uim.oop.board.pipes.curved.LPipe;
import sk.stuba.fei.uim.oop.board.pipes.straight.IPipe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.Random;

public class Board extends JPanel {

    @Getter
    private Tile[][] grid;
    @Getter
    private final ArrayList<Tile> path;
    @Getter
    private Tile start;
    @Getter
    private Tile end;
    private BufferedImage background;
    private final int size;
    private final Random generator;

    public Board(int size) {
        this.size=size;
        this.generator = new Random();
        this.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        this.loadBackground();
        this.initializeGrid();
        this.path = generatePath();
        this.drawPath();
    }

    private void loadBackground() {
        background = null;
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
        if (background != null) {
            g.drawImage(background, 0, 0, null);
        }
    }

    private void initializeGrid() {
        this.setLayout(new GridLayout(size, size));
        grid = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Tile tile = new Tile(i, j);
                grid[i][j] = tile;
                this.add(grid[i][j]);
            }
        }
        initializeStartEndTiles();
    }

    private void initializeStartEndTiles() {
        if (generator.nextInt(2) == 1) {
            start = grid[generator.nextInt(size)][0];
            end = grid[generator.nextInt(size)][size - 1];
        } else {
            start = grid[0][generator.nextInt(size)];
            end = grid[size - 1][generator.nextInt(size)];
        }
    }

    private void insertPipe(Pipe pipe, Tile tile) {
        int index = tile.getRow() * size + tile.getColumn();
        this.remove(tile);
        grid[pipe.getRow()][pipe.getColumn()] = pipe;
        this.add(grid[pipe.getRow()][pipe.getColumn()], index);
    }

    private IPipe replaceTileWithIPipe(Tile tile) {
        IPipe pipe = new IPipe(tile.getRow(), tile.getColumn());
        insertPipe(pipe, tile);
        return pipe;
    }

    private LPipe replaceTileWithLPipe(Tile tile) {
        LPipe pipe = new LPipe(tile.getRow(), tile.getColumn());
        insertPipe(pipe, tile);
        return pipe;
    }

    private void drawStart() {
        Tile nextTile = path.get(1);
        if (start.getColumn() == 0 && start.getRow() == 0 ||
                start.getColumn() == 0 && start.getRow() == size - 1 ||
                start.getColumn() == size - 1 && start.getRow() == 0) {
            start = replaceTileWithIPipe(start);
        } else if ((start.getColumn() == 0 && nextTile.getRow() != start.getRow()) ||
                (start.getColumn() != 0 && nextTile.getColumn() != start.getColumn())) {
            start = replaceTileWithLPipe(start);
        } else {
            start = replaceTileWithIPipe(start);
        }
        start.setOpaque(true);
        start.setBackground(Color.GRAY);
    }

    private void drawEnd() {
        Tile previousTile = path.get(path.size() - 2);
        if (end.getColumn() == size - 1 && end.getRow() == size - 1 ||
                end.getColumn() == 0 && end.getRow() == size - 1 ||
                end.getColumn() == size - 1 && end.getRow() == 0) {
            end = replaceTileWithIPipe(end);
        } else if ((end.getColumn() == size - 1 && previousTile.getRow() != end.getRow()) ||
                (end.getColumn() != size - 1 && previousTile.getColumn() != end.getColumn())) {
            end = replaceTileWithLPipe(end);
        } else {
            end = replaceTileWithIPipe(end);
        }
        end.setOpaque(true);
        end.setBackground(Color.LIGHT_GRAY);
    }

    private void drawPath() {
        for (Tile tile : path) {
            int currentTile = path.indexOf(tile);
            if (currentTile > 0 && currentTile < path.size() - 1) {
                Tile previousTile = path.get(currentTile - 1);
                Tile nextTile = path.get(currentTile + 1);
                if (previousTile.getRow() == nextTile.getRow() || previousTile.getColumn() == nextTile.getColumn()) {
                    replaceTileWithIPipe(tile);
                } else {
                    replaceTileWithLPipe(tile);
                }
            }
        }
        drawStart();
        drawEnd();
    }

    private ArrayList<Tile> generatePath() {
        ArrayList<Tile> path = new ArrayList<>();
        ArrayList<Tile> stack = new ArrayList<>();
        stack.add(start);
        start.setVisited(true);
        while (!stack.isEmpty()) {
            Tile current = stack.get(stack.size() - 1);
            if (current.equals(end)) {
                while (current != null) {
                    path.add(current);
                    current = current.getPrevious();
                }
                break;
            }
            ArrayList<Tile> currentNeighbors = current.getNeighbors(grid);
            currentNeighbors.removeIf(Tile::isVisited);
            if (currentNeighbors.isEmpty()) {
                stack.remove(stack.size() - 1);
            } else {
                Tile neighbor = currentNeighbors.get(generator.nextInt(currentNeighbors.size()));
                neighbor.setVisited(true);
                stack.add(neighbor);
                neighbor.setPrevious(current);
            }
        }
        Collections.reverse(path);
        return path;
    }

}
