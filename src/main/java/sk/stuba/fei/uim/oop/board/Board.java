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
        background = null;
        try {
            background = ImageIO.read(Board.class.getResourceAsStream("/water.jpg"));
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
            start = grid[generator.nextInt(size)][0];
            end = grid[generator.nextInt(size)][size - 1];
        } else {
            start = grid[0][generator.nextInt(size)];
            end = grid[size - 1][generator.nextInt(size)];
        }
        start.setConstantHighlight(true);
        end.setConstantHighlight(true);
        path = generatePath();
        drawPath(path, size);
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
            if ((nextTile.getRow() != start.getRow())) {
                if (nextTile.getRow() > start.getRow()) {
                    angles.add(0, 0);
                } else {
                    angles.add(0, 90);
                }
            } else {
                angles.add(0, 0);
            }
        } else {
            if ((nextTile.getColumn() != start.getColumn())) {
                if (nextTile.getColumn() > start.getColumn()) {
                    angles.add(0, 180);
                } else {
                    angles.add(0, 90);
                }
            } else {
                angles.add(0, 90);
            }
        }

        Tile previousTile = path.get(path.size() - 2);
        if (end.getColumn() == size - 1) {
            if ((previousTile.getRow() != end.getRow())) {
                if (previousTile.getRow() > end.getRow()) {
                    angles.add(270);
                } else {
                    angles.add(180);
                }
            } else {
                angles.add(0);
            }
        } else {
            if ((previousTile.getColumn() != end.getColumn())) {
                if (previousTile.getColumn() > end.getColumn()) {
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
                    tile.setAngle(generator.nextInt(2) * 90);
                } else {
                    tile.setType(Type.L_PIPE);
                    tile.setAngle(generator.nextInt(4) * 90);
                }
            }
        }
        Tile nextTile = path.get(1);
        if (start.getColumn() == 0) {
            if ((nextTile.getRow() != start.getRow())) {
                start.setType(Type.L_PIPE);
                start.setAngle(generator.nextInt(4) * 90);
            } else {
                start.setType(Type.PIPE);
                start.setAngle(generator.nextInt(2) * 90);
            }
        } else {
            if ((nextTile.getColumn() != start.getColumn())) {
                start.setType(Type.L_PIPE);
                start.setAngle(generator.nextInt(4) * 90);
            } else {
                start.setType(Type.PIPE);
                start.setAngle(generator.nextInt(2) * 90);
            }
        }

        Tile previousTile = path.get(path.size() - 2);
        if (end.getColumn() == size - 1) {
            if ((previousTile.getRow() != end.getRow())) {
                end.setType(Type.L_PIPE);
                end.setAngle(generator.nextInt(4) * 90);
            } else {
                end.setType(Type.PIPE);
                end.setAngle(generator.nextInt(2) * 90);
            }
        } else {
            if ((previousTile.getColumn() != end.getColumn())) {
                end.setType(Type.L_PIPE);
                end.setAngle(generator.nextInt(4) * 90);
            } else {
                end.setType(Type.PIPE);
                end.setAngle(generator.nextInt(2) * 90);
            }
        }
    }

    private ArrayList<Tile> generatePath() {
        ArrayList<Tile> path = new ArrayList<>();
        ArrayList<Tile> stack = new ArrayList<>();
        stack.add(start);
        start.setVisited(true);
        while (!stack.isEmpty()) {
            Tile current = stack.remove(stack.size() - 1);
            if (current.equals(end)) {
                while (current != null) {
                    path.add(current);
                    current = current.getPrevious();
                }
                break;
            }
            ArrayList<Tile> currentNeighbors = current.getNeighbors(grid);
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
