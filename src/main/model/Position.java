package model;

import org.json.JSONObject;
import persistence.Persistable;

// Represents a simple (x,y) coordinate, where
// an increasing y represents moving downwards
// and an increasing x represents moving right
public class Position implements Persistable {
    private final int posX;
    private final int posY;

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


    // EFFECTS: Save the current Position into a JSONObject
    @Override
    public JSONObject save() {
        JSONObject out = new JSONObject();
        out.put("posX", posX);
        out.put("posY", posY);
        return out;
    }
}
