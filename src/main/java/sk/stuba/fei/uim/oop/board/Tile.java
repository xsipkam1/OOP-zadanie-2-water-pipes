package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Tile extends JPanel {

    @Setter
    private boolean highlight;
    @Setter
    private boolean constantHighlight;
    @Getter @Setter
    private boolean visited;
    @Getter @Setter
    private Type type;
    @Getter @Setter
    private int row;
    @Getter @Setter
    private int column;
    @Getter @Setter
    private Tile previous;
    @Getter
    private int angle;
    private BufferedImage pipeImage;
    private BufferedImage lPipeImage;

    public Tile() {
        this.highlight = false;
        this.constantHighlight = false;
        this.visited = false;
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setType(Type.EMPTY);
        this.setOpaque(false);
        this.previous = null;
        this.pipeImage = null;
        this.lPipeImage = null;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void setAngle() {
        angle += 90;
        if (this.type.equals(Type.PIPE)) {
            if (angle > 90) {
                angle = 0;
            }
        } else {
            if (angle > 270) {
                angle = 0;
            }
        }
        repaint();
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public ArrayList<Tile> getNeighbors(Tile[][] grid) {
        ArrayList<Tile> neighbors = new ArrayList<>();
        if (row > 0) {
            neighbors.add(grid[row - 1][column]);
        }
        if (row < grid.length - 1) {
            neighbors.add(grid[row + 1][column]);
        }
        if (column > 0) {
            neighbors.add(grid[row][column - 1]);
        }
        if (column < grid[0].length - 1) {
            neighbors.add(grid[row][column + 1]);
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
            } else {
                g.setColor(Color.RED);
            }
            g.drawRect((int) (this.getWidth() * 0.025), (int) (this.getHeight() * 0.025),
                    (int) (this.getWidth() * 0.96), (int) (this.getHeight() * 0.96));
            if (!this.constantHighlight) {
                this.highlight = false;
            }

        }

        if (this.type.equals(Type.PIPE)) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(Math.toRadians(angle), this.getWidth() / 2.0, this.getHeight() / 2.0);

            try {
                pipeImage = ImageIO.read(Board.class.getResourceAsStream("/pipe.png"));
            } catch (IOException | IllegalArgumentException ignored) {
            }

            if (pipeImage == null) {
                g.setColor(Color.BLACK);
                g.fillRect(0, (int) (this.getHeight() * 0.25), this.getWidth(), (int) (this.getHeight() - this.getHeight() * 0.5));
            } else {
                g.drawImage(pipeImage, 0, 0, this.getWidth(), this.getHeight(), null);
            }
            g2d.rotate(Math.toRadians(-angle), this.getWidth() / 2.0, this.getHeight() / 2.0);

        }

        if (this.type.equals(Type.L_PIPE)) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(Math.toRadians(angle), this.getWidth() / 2.0, this.getHeight() / 2.0);

            try {
                lPipeImage = ImageIO.read(Board.class.getResourceAsStream("/l_pipe.png"));
            } catch (IOException | IllegalArgumentException ignored) {
            }

            if (lPipeImage == null) {
                g.setColor(Color.BLACK);
                g.fillRect(0, (int) (this.getHeight() * 0.25), (int) (this.getWidth() * 0.75), (int) (this.getHeight() - this.getHeight() * 0.5));
                g.fillRect((int) (this.getWidth() * 0.25), (int) (this.getHeight() * 0.25), (int) (this.getWidth() * 0.5), this.getHeight());
            } else {
                g.drawImage(lPipeImage, 0, 0, this.getWidth(), this.getHeight(), null);
            }
            g2d.rotate(Math.toRadians(-angle), this.getWidth() / 2.0, this.getHeight() / 2.0);
        }
    }

}
