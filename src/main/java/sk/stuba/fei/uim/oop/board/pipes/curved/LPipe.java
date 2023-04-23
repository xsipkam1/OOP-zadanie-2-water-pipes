package sk.stuba.fei.uim.oop.board.pipes.curved;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.pipes.Pipe;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class LPipe extends Pipe {

    @Getter
    private Direction direction;

    public LPipe(int row, int column) {
        super(row, column);
        this.direction = Direction.values()[generator.nextInt(Direction.values().length)];
        try {
            this.pipeImage = ImageIO.read(LPipe.class.getResourceAsStream("/l_pipe.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rotate() {
        switch (this.direction) {
            case DOWNLEFT:
                this.direction = Direction.UPLEFT;
                break;
            case UPLEFT:
                this.direction = Direction.UPRIGHT;
                break;
            case UPRIGHT:
                this.direction = Direction.DOWNRIGHT;
                break;
            case DOWNRIGHT:
                this.direction = Direction.DOWNLEFT;
                break;
        }
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ((Graphics2D) g).rotate(Math.toRadians(this.direction.getAngle()), this.getWidth() / 2.0, this.getHeight() / 2.0);
        if (this.pipeImage == null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, (int) (this.getHeight() * 0.25), (int) (this.getWidth() * 0.75), (int) (this.getHeight() - this.getHeight() * 0.5));
            g.fillRect((int) (this.getWidth() * 0.25), (int) (this.getHeight() * 0.25), (int) (this.getWidth() * 0.5), this.getHeight());
        } else {
            g.drawImage(this.pipeImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }
        ((Graphics2D) g).rotate(Math.toRadians(-this.direction.getAngle()), this.getWidth() / 2.0, this.getHeight() / 2.0);
    }

}
