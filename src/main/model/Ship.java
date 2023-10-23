package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Persistable;

// Represents a single ship on the game board
public class Ship implements Persistable {
    public static final int ALIVE = 0;
    public static final int DEAD = 1;
    public static final int SCATTER_SHOT_LENGTH = 4;

    private final int length;
    private int[] health;
    private final char identifier;
    private final Orientation orientation;
    private final Position position;

    // REQUIRES: length > 0
    public Ship(int length, char identifier, Position position, Orientation orientation) {
        this.length = length;
        this.health = new int[length];
        this.identifier = identifier;
        this.position = position;
        this.orientation = orientation;
    }

    // EFFECTS: Return whether this ship is alive or not
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

    public void setHealth(int[] newHealth) {
        this.health = newHealth;
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

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        JSONArray healthArray = new JSONArray();
        json.put("length", length);

        for (int h : health) {
            healthArray.put(h);
        }

        json.put("health", healthArray);
        json.put("identifier", identifier);
        json.put("orientation", orientation);
        json.put("position", position.save());
        return json;
    }
}
