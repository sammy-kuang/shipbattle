package ui;

import model.Board;

// Represents an abstract Controller that can view(!!) and play the game.
// This can either be a player or an "AI."
public abstract class Controller {
    private Board board;
    private Board opponentBoard;

    // EFFECTS: Visualize our board
    public abstract void viewBoard();

    // EFFECTS: Visualize what tiles we've fired on
    public abstract void peek();

    // EFFECTS: Visualize the opponents board
    public abstract void cheatViewEnemy();

    // EFFECTS: Query the controller to make a move, depending on context (whether player or AI)
    public abstract void turn();

    // MODIFIES: this
    // EFFECTS: Query the controller the fire on the opponents board
    public abstract void fire();

    // MODIFIES: this
    // EFFECTS: Query the controller to place their ships
    public abstract void placeShips(int number);

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getOpponentBoard() {
        return opponentBoard;
    }

    public void setOpponentBoard(Board opponentBoard) {
        this.opponentBoard = opponentBoard;
    }

}
