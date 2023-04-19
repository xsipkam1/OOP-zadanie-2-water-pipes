package sk.stuba.fei.uim.oop.board.pipes;

import sk.stuba.fei.uim.oop.board.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LPipe extends Pipe{

    public LPipe(int row, int column) {
        super(row, column);
        try {
            this.pipeImage = ImageIO.read(Board.class.getResourceAsStream("/l_pipe.png"));
        } catch (IOException | IllegalArgumentException ignored) {
        }
    }

    @Override
    public void setAngle() {
        this.angle += 90;
        if (this.angle > 270) {
            this.angle = 0;
        }
        this.repaint();
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ((Graphics2D) g).rotate(Math.toRadians(this.angle), this.getWidth() / 2.0, this.getHeight() / 2.0);
        if (this.pipeImage == null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, (int) (this.getHeight() * 0.25), (int) (this.getWidth() * 0.75), (int) (this.getHeight() - this.getHeight() * 0.5));
            g.fillRect((int) (this.getWidth() * 0.25), (int) (this.getHeight() * 0.25), (int) (this.getWidth() * 0.5), this.getHeight());
        } else {
            g.drawImage(this.pipeImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }
        ((Graphics2D) g).rotate(Math.toRadians(-this.angle), this.getWidth() / 2.0, this.getHeight() / 2.0);

    }

}
