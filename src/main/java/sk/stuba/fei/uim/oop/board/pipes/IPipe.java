package sk.stuba.fei.uim.oop.board.pipes;

import sk.stuba.fei.uim.oop.board.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class IPipe extends Pipe {

    public IPipe(int row, int column) {
        super(row, column);
        try {
            this.pipeImage = ImageIO.read(Board.class.getResourceAsStream("/pipe.png"));
        } catch (IOException | IllegalArgumentException ignored) {
        }
    }

    @Override
    public void setAngle() {
        this.angle += 90;
        if (this.angle > 90) {
            this.angle = 0;
        }
        this.repaint();
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ((Graphics2D) g).rotate(Math.toRadians(angle), this.getWidth() / 2.0, this.getHeight() / 2.0);
        if (this.pipeImage == null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, (int) (this.getHeight() * 0.25), this.getWidth(), (int) (this.getHeight() - this.getHeight() * 0.5));
        } else {
            g.drawImage(this.pipeImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }
        ((Graphics2D) g).rotate(Math.toRadians(-this.angle), this.getWidth() / 2.0, this.getHeight() / 2.0);
    }

}
