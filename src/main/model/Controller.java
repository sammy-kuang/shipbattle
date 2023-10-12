package model;

// Represents an abstract Controller that can view and play the game.
// This can either be a player or an "AI."
public abstract class Controller {
    private Board ourBoard;
    private Board opponentBoard;

    // EFFECTS: Visualize our board
    public abstract void viewBoard();

    // EFFECTS: Visualize what tiles we've fired on
    public abstract void peek();

    // EFFECTS: Visualize the opponents board
    public abstract void cheatViewEnemy();

    // MODIFIES: this
    // EFFECTS: Query the user the fire on the opponents board
    public abstract Position fire();
}
