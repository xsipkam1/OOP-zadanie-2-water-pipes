package sk.stuba.fei.uim.oop.board.pipes.straight;

public enum Orientation {
    HORIZONTAL(0),
    VERTICAL(90);

    private final int angle;

    Orientation(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public boolean isHorizontal() {
        return this == Orientation.HORIZONTAL;
    }

    public boolean isVertical() {
        return this == Orientation.VERTICAL;
    }
}
