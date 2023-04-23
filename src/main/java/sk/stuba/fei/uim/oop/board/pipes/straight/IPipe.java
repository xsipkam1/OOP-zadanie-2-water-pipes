package sk.stuba.fei.uim.oop.board.pipes.straight;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.pipes.Pipe;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class IPipe extends Pipe {

    @Getter
    private Orientation orientation;

    public IPipe(int row, int column) {
        super(row, column);
        this.orientation = Orientation.values()[generator.nextInt(Orientation.values().length)];
        try {
            this.pipeImage = ImageIO.read(IPipe.class.getResourceAsStream("/pipe.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rotate() {
        switch (this.orientation) {
            case HORIZONTAL:
                this.orientation = Orientation.VERTICAL;
                break;
            case VERTICAL:
                this.orientation = Orientation.HORIZONTAL;
                break;
        }
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ((Graphics2D) g).rotate(Math.toRadians(this.orientation.getAngle()), this.getWidth() / 2.0, this.getHeight() / 2.0);
        if (this.pipeImage == null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, (int) (this.getHeight() * 0.25), this.getWidth(), (int) (this.getHeight() - this.getHeight() * 0.5));
        } else {
            g.drawImage(this.pipeImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }
        ((Graphics2D) g).rotate(Math.toRadians(-this.orientation.getAngle()), this.getWidth() / 2.0, this.getHeight() / 2.0);
    }

}
