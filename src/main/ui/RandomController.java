package ui;

import model.*;

import java.util.Random;

public class RandomController extends Controller {

    public RandomController(Board ourBoard, Board opponent) {
        this.setBoard(ourBoard);
        this.setOpponentBoard(opponent);
    }

    @Override
    public void viewBoard() {
    }

    @Override
    public void peek() {
    }

    @Override
    public void cheatViewEnemy() {
    }

    @Override
    public void turn() {
        System.out.println("[AI] My turn!");
        fire();
    }

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
