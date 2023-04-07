package sk.stuba.fei.uim.oop.board;

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
    private ArrayList<Tile> path;
    private final Random generator;

    public Board(int size) {
        this.generator=new Random();
        this.path=new ArrayList<>();
        this.initializeGrid(size);
        this.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        this.loadBackground();
        //this.setBackground(Color.CYAN);
    }

    private void loadBackground() {
        background = null;
        try{
            background=ImageIO.read(Objects.requireNonNull(Board.class.getResourceAsStream("/water.jpg")));
        } catch (IOException e) {
            System.out.println("Obrazok sa nenasiel, nacitavam povodnu farbu pozadia.");
        }
        if (background == null) {
            this.setBackground(Color.CYAN);
        }
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
        if (generator.nextInt(2)==1){
            start=grid[generator.nextInt(size)][0];
            end=grid[generator.nextInt(size)][size-1];
        } else {
            start=grid[0][generator.nextInt(size)];
            end=grid[size-1][generator.nextInt(size)];
        }
        start.setConstantHighlight(true);
        end.setConstantHighlight(true);
        path = generatePath();
        Collections.reverse(path);
        drawPath(path, size);
    }

    private void drawPath(ArrayList<Tile> path, int size) {
        for (Tile tile : path) {
            int currentTile = path.indexOf(tile);
            if (currentTile > 0 && currentTile < path.size() - 1) {
                Tile previousTile = path.get(currentTile - 1);
                Tile nextTile = path.get(currentTile + 1);
                if (previousTile.getRow() == nextTile.getRow() || previousTile.getColumn() == nextTile.getColumn()) {
                    tile.setType(Type.PIPE);
                } else {
                    tile.setType(Type.L_PIPE);
                }
            } else {
                Tile nextTile = path.get(1);
                if (start.getColumn() == 0) {
                    if ((nextTile.getRow()!=start.getRow())) {
                        start.setType(Type.L_PIPE);
                    } else {
                        start.setType(Type.PIPE);
                    }
                } else {
                    if ((nextTile.getColumn()!=start.getColumn())) {
                        start.setType(Type.L_PIPE);
                    } else {
                        start.setType(Type.PIPE);
                    }
                }

                Tile previousTile = path.get(path.size() - 2);
                if (end.getColumn() == size-1) {
                    if ((previousTile.getRow()!=end.getRow())) {
                        end.setType(Type.L_PIPE);
                    } else {
                        end.setType(Type.PIPE);
                    }
                } else {
                    if ((previousTile.getColumn()!=end.getColumn())) {
                        end.setType(Type.L_PIPE);
                    } else {
                        end.setType(Type.PIPE);
                    }
                }
            }
        }
    }

    private ArrayList<Tile> generatePath() {
        ArrayList<Tile> path = new ArrayList<>();
        ArrayList<Tile> stack = new ArrayList<>();
        Map<Tile, Tile> parent = new HashMap<>();
        stack.add(start);
        start.setVisited(true);
        while (!stack.isEmpty()) {
            Tile current = stack.remove(stack.size() - 1);
            if(current.equals(end)){
                while (!current.equals(start)) {
                    path.add(current);
                    current = parent.get(current);
                }
                path.add(start);
                return path;
            }
            ArrayList<Tile> currentNeighbors = current.getNeighbors(grid);
            Collections.shuffle(currentNeighbors);
            for (Tile neighbor : currentNeighbors) {
                if(!neighbor.isVisited()){
                    neighbor.setVisited(true);
                    stack.add(neighbor);
                    parent.put(neighbor, current);
                }
            }
        }
        return null;
    }

}
