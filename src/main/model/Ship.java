package model;

// Represents a single ship on the game board
public class Ship {
    public static final int ALIVE = 0;
    public static final int DEAD = 1;

    private int length;
    private int[] health;
    private char identifier;
    private Orientation orientation;
    private Position position;

    // REQUIRES: length > 0
    public Ship(int length, char identifier, Position position, Orientation orientation) {
        this.length = length;
        this.health = new int[length];
        this.identifier = identifier;
        this.position = position;
        this.orientation = orientation;
    }

    public boolean isAlive() {
        for (int i = 0; i < length; i++) {
            if (health[i] == ALIVE) {
                return true;
            }
        }
        return false;
    }

    public int[] getHealth() {
        return health;
    }

    public char getIdentifier() {
        return identifier;
    }

    public int getLength() {
        return length;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public Position getPosition() {
        return position;
    }
}
