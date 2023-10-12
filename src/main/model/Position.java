package model;

// Represents a simple (x,y) coordinate, where
// an increasing y represents moving downwards
// and an increasing x represents moving right
public class Position {
    private int posX;
    private int posY;

    // REQUIRES: x > 0 and y > 0
    public Position(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
