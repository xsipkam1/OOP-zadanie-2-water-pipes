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
    @Getter
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

    public void setType(Type type) {
        this.type = type;
        if (this.type.equals(Type.PIPE)) {
            try {
                this.pipeImage = ImageIO.read(Board.class.getResourceAsStream("/pipe.png"));
            } catch (IOException | IllegalArgumentException ignored) {
            }
        } else if (this.type.equals(Type.L_PIPE)) {
            try {
                this.lPipeImage = ImageIO.read(Board.class.getResourceAsStream("/l_pipe.png"));
            } catch (IOException | IllegalArgumentException ignored) {
            }
        }
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void setAngle() {
        this.angle += 90;
        if (this.type.equals(Type.PIPE)) {
            if (this.angle > 90) {
                this.angle = 0;
            }
        } else {
            if (this.angle > 270) {
                this.angle = 0;
            }
        }
        this.repaint();
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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

            if (this.pipeImage == null) {
                g.setColor(Color.BLACK);
                g.fillRect(0, (int) (this.getHeight() * 0.25), this.getWidth(), (int) (this.getHeight() - this.getHeight() * 0.5));
            } else {
                g.drawImage(this.pipeImage, 0, 0, this.getWidth(), this.getHeight(), null);
            }

            g2d.rotate(Math.toRadians(-this.angle), this.getWidth() / 2.0, this.getHeight() / 2.0);

        }

        if (this.type.equals(Type.L_PIPE)) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(Math.toRadians(this.angle), this.getWidth() / 2.0, this.getHeight() / 2.0);

            if (this.lPipeImage == null) {
                g.setColor(Color.BLACK);
                g.fillRect(0, (int) (this.getHeight() * 0.25), (int) (this.getWidth() * 0.75), (int) (this.getHeight() - this.getHeight() * 0.5));
                g.fillRect((int) (this.getWidth() * 0.25), (int) (this.getHeight() * 0.25), (int) (this.getWidth() * 0.5), this.getHeight());
            } else {
                g.drawImage(this.lPipeImage, 0, 0, this.getWidth(), this.getHeight(), null);
            }

            g2d.rotate(Math.toRadians(-this.angle), this.getWidth() / 2.0, this.getHeight() / 2.0);
        }
    }

}
