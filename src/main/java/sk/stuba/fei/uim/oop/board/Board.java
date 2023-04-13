package sk.stuba.fei.uim.oop.board;

import lombok.Getter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.Random;

public class Board extends JPanel {

    private Tile[][] grid;
    private BufferedImage background;
    private Tile start;
    private Tile end;
    @Getter
    private ArrayList<Tile> path;
    private final Random generator;

    public Board(int size) {
        this.generator = new Random();
        this.path = new ArrayList<>();
        this.initializeGrid(size);
        this.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        this.loadBackground();
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
        if (generator.nextInt(2) == 1) {
            this.start = grid[generator.nextInt(size)][0];
            this.end = grid[generator.nextInt(size)][size - 1];
        } else {
            this.start = grid[0][generator.nextInt(size)];
            this.end = grid[size - 1][generator.nextInt(size)];
        }
        this.start.setOpaque(true);
        this.start.setBackground(Color.GRAY);
        this.end.setOpaque(true);
        this.end.setBackground(Color.GRAY);
        this.path = generatePath();
        this.drawPath(path, size);
    }

    public ArrayList<Integer> getCorrectAngles(ArrayList<Tile> path, int size) {
        ArrayList<Integer> angles = new ArrayList<>();
        for (Tile currentTile : path) {
            int currentTileIndex = path.indexOf(currentTile);
            if (currentTileIndex > 0 && currentTileIndex < path.size() - 1) {
                Tile previousTile = path.get(currentTileIndex - 1);
                Tile nextTile = path.get(currentTileIndex + 1);
                if ((previousTile.getColumn() < currentTile.getColumn() && nextTile.getRow() > currentTile.getRow())
                        || (previousTile.getRow() > currentTile.getRow() && nextTile.getColumn() < currentTile.getColumn())) {
                    angles.add(0);
                } else if ((previousTile.getColumn() < currentTile.getColumn() && nextTile.getRow() < currentTile.getRow())
                        || (previousTile.getRow() < currentTile.getRow() && nextTile.getColumn() < currentTile.getColumn())) {
                    angles.add(90);
                } else if ((previousTile.getRow() < currentTile.getRow() && nextTile.getColumn() > currentTile.getColumn())
                        || (previousTile.getColumn() > currentTile.getColumn() && nextTile.getRow() < currentTile.getRow())) {
                    angles.add(180);
                } else if ((previousTile.getColumn() > currentTile.getColumn() && nextTile.getRow() > currentTile.getRow())
                        || (previousTile.getRow() > currentTile.getRow() && nextTile.getColumn() > currentTile.getColumn())) {
                    angles.add(270);
                } else if (previousTile.getRow() == nextTile.getRow()) {
                    angles.add(0);
                } else if (previousTile.getColumn() == nextTile.getColumn()) {
                    angles.add(90);
                }
            }
        }
        Tile nextTile = path.get(1);
        if (start.getColumn() == 0) {
            if ((nextTile.getRow() != this.start.getRow())) {
                if (nextTile.getRow() > this.start.getRow()) {
                    angles.add(0, 0);
                } else {
                    angles.add(0, 90);
                }
            } else {
                angles.add(0, 0);
            }
        } else {
            if ((nextTile.getColumn() != this.start.getColumn())) {
                if (nextTile.getColumn() > this.start.getColumn()) {
                    angles.add(0, 180);
                } else {
                    angles.add(0, 90);
                }
            } else {
                angles.add(0, 90);
            }
        }

        Tile previousTile = path.get(path.size() - 2);
        if (this.end.getColumn() == size - 1) {
            if ((previousTile.getRow() != this.end.getRow())) {
                if (previousTile.getRow() > this.end.getRow()) {
                    angles.add(270);
                } else {
                    angles.add(180);
                }
            } else {
                angles.add(0);
            }
        } else {
            if ((previousTile.getColumn() != this.end.getColumn())) {
                if (previousTile.getColumn() > this.end.getColumn()) {
                    angles.add(270);
                } else {
                    angles.add(0);
                }
            } else {
                angles.add(90);
            }
        }
        return angles;
    }

    private void drawPath(ArrayList<Tile> path, int size) {
        for (Tile tile : path) {
            int currentTile = path.indexOf(tile);
            if (currentTile > 0 && currentTile < path.size() - 1) {
                Tile previousTile = path.get(currentTile - 1);
                Tile nextTile = path.get(currentTile + 1);
                if (previousTile.getRow() == nextTile.getRow() || previousTile.getColumn() == nextTile.getColumn()) {
                    tile.setType(Type.PIPE);
                    tile.setAngle(this.generator.nextInt(2) * 90);
                } else {
                    tile.setType(Type.L_PIPE);
                    tile.setAngle(this.generator.nextInt(4) * 90);
                }
            }
        }
        Tile nextTile = path.get(1);
        if ((this.start.getColumn() == 0 && nextTile.getRow() != start.getRow()) || (this.start.getColumn() != 0 && nextTile.getColumn() != start.getColumn())) {
            this.start.setType(Type.L_PIPE);
            this.start.setAngle(this.generator.nextInt(4) * 90);
        } else {
            this.start.setType(Type.PIPE);
            this.start.setAngle(this.generator.nextInt(2) * 90);
        }

        Tile previousTile = path.get(path.size() - 2);
        if ((this.end.getColumn() == size - 1 && previousTile.getRow() != end.getRow()) || (this.end.getColumn() != size - 1 && previousTile.getColumn() != end.getColumn())) {
            this.end.setType(Type.L_PIPE);
            this.end.setAngle(this.generator.nextInt(4) * 90);
        } else {
            this.end.setType(Type.PIPE);
            this.end.setAngle(this.generator.nextInt(2) * 90);
        }
    }

    private ArrayList<Tile> generatePath() {
        ArrayList<Tile> path = new ArrayList<>();
        ArrayList<Tile> stack = new ArrayList<>();
        stack.add(this.start);
        this.start.setVisited(true);
        while (!stack.isEmpty()) {
            Tile current = stack.remove(stack.size() - 1);
            if (current.equals(this.end)) {
                while (current != null) {
                    path.add(current);
                    current = current.getPrevious();
                }
                break;
            }
            ArrayList<Tile> currentNeighbors = current.getNeighbors(this.grid);
            Collections.shuffle(currentNeighbors);
            for (Tile neighbor : currentNeighbors) {
                if (!neighbor.isVisited()) {
                    neighbor.setVisited(true);
                    stack.add(neighbor);
                    neighbor.setPrevious(current);
                }
            }
        }
        Collections.reverse(path);
        return path;
    }

}
