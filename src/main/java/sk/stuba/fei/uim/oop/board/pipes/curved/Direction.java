package sk.stuba.fei.uim.oop.board.pipes.curved;

public enum Direction {
    DOWNLEFT(0),
    UPLEFT(90),
    UPRIGHT(180),
    DOWNRIGHT(270);

    private final int angle;

    Direction(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public boolean isFacingUp() {
        return this == Direction.UPLEFT || this == Direction.UPRIGHT;
    }

    public boolean isFacingDown() {
        return this == Direction.DOWNLEFT || this == Direction.DOWNRIGHT;
    }

    public boolean isFacingRight() {
        return this == Direction.UPRIGHT || this == Direction.DOWNRIGHT;
    }

    public boolean isFacingLeft() {
        return this == Direction.UPLEFT || this == Direction.DOWNLEFT;
    }
}
