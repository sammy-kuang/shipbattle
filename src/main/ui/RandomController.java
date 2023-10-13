package ui;

import model.*;

import java.util.Random;

// Represents an "AI" playing the game on a console version of the game
public class RandomController extends Controller {
    // EFFECTS: Create an AI capable of playing the game in the terminal
    public RandomController(Board ourBoard, Board opponent) {
        this.setBoard(ourBoard);
        this.setOpponentBoard(opponent);
    }

    // EFFECTS: Visualize our board - in the context of the AI, does nothing.
    @Override
    public void viewBoard() {
    }

    // EFFECTS: Peek at the battlefield - in the context of the AI, does nothing.
    @Override
    public void peek() {
    }

    // EFFECTS: View the opponent's game board - in the context of the AI, does nothing.
    @Override
    public void cheatViewEnemy() {
    }

    // EFFECTS: Run the AI's turn
    @Override
    public void turn() {
        System.out.println("[AI] My turn!");
        fire();
    }

    // EFFECTS: Fire on the opponent's board randomly
    // MODIFIES: this
    @Override
    public void fire() {
        try {
            // get random position to fire on
            Random r = new Random();
            int x = Math.abs(r.nextInt() % getOpponentBoard().getBoardSize());
            int y = Math.abs(r.nextInt() % getOpponentBoard().getBoardSize());
            Position p = new Position(x, y);
            boolean hit = getOpponentBoard().fireOnPosition(p);
            System.out.printf("[AI] Firing on (%d, %d)!!\n", x, y);
            Thread.sleep(1000);
            System.out.println(hit ? "[AI] Ha! Hit you!" : "[AI] Damn... you'll live to see another day...");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // EFFECTS: Place ships throughout the board randomly for the AI
    // MODIFIES: this
    @Override
    public void placeShips(int number) {
        int ships = 2;
        int shipSizes = number / ships;

        while (ships > 0) {
            Random r = new Random();
            Orientation orientation = r.nextInt() % 2 == 0 ? Orientation.LeftRight : Orientation.UpDown;
            int x = Math.abs(r.nextInt() % getOpponentBoard().getBoardSize());
            int y = Math.abs(r.nextInt() % getOpponentBoard().getBoardSize());
            Ship s = new Ship(shipSizes, getBoard().generateIdentifier(), new Position(x, y), orientation);
            if (getBoard().addShip(s)) {
                ships--;
            }
        }
    }
}
