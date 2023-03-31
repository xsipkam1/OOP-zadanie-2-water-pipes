package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public class Tile extends JPanel {

    @Setter
    private boolean highlight;
    @Getter @Setter
    private boolean playable;
    @Getter @Setter
    private Type type;
    private int angle;

    public Tile() {
        this.highlight = false;
        this.setBackground(Color.GRAY);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.highlight) {
            ((Graphics2D) g).setStroke(new BasicStroke(3));
            g.setColor(Color.RED);
            g.drawRect((int) (this.getWidth() * 0.025), (int) (this.getHeight() * 0.025),
                    (int) (this.getWidth() * 0.96), (int) (this.getHeight() * 0.96));
            this.highlight = false;
        }
        if (this.type.equals(Type.PIPE)) {
            g.setColor(Color.BLACK);
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(Math.toRadians(angle), this.getWidth() / 2.0, this.getHeight() / 2.0);
            g.fillRect(0, (int) (this.getHeight() * 0.25),
                    (int) (this.getWidth()), (int) (this.getHeight() - (this.getHeight() * 0.5)));
        }
        if (this.type.equals(Type.L_PIPE)) {
            g.setColor(Color.BLACK);
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(Math.toRadians(angle), this.getWidth() / 2.0, this.getHeight() / 2.0);
            g.fillRect((int) (this.getWidth() * 0.35), -30,
                    (int) (this.getHeight() * 0.5), (int) (this.getWidth() * 0.6));
            g.fillRect((int) (this.getWidth() * 0.35), (int) (this.getHeight() * 0.28),
                    this.getWidth(), (int) (this.getHeight() * 0.46));
        }
    }

    public void setAngle() {
        angle += 90;
        repaint();
    }

}
