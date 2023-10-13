package ui;

import model.*;

import java.util.Random;

// Represents a Player that is playing through a console
public class ConsolePlayer extends Controller {
    // EFFECTS: Create a player capable of playing the game through the terminal
    public ConsolePlayer(Board players, Board opponent) {
        this.setBoard(players);
        this.setOpponentBoard(opponent);
    }

    // EFFECTS: Print out the board for the player to view
    @Override
    public void viewBoard() {
        System.out.println("Captain, this is our fleet right now:");
        ConsoleGame.printBoard(this.getBoard());
    }

    // EFFECTS: Print out the battlefield for the player to view
    @Override
    public void peek() {
        System.out.println("Captain, this is what we've shot at..");
        ConsoleGame.printObfuscatedBoard(this.getOpponentBoard());
    }

    // EFFECTS: Print out the opponent's board for the player to view
    @Override
    public void cheatViewEnemy() {
        ConsoleGame.printBoard(this.getOpponentBoard());
    }

    // EFFECTS: Run the player's turn in the game
    // MODIFIES: this
    @Override
    public void turn() {
        System.out.println("\nOur turn Captain. What would you like to do?");
        String options = "(0) Fire\n(1) View our ships\n(2) View the battlefield\n";
        options += "(3) Help/Instructions\n(4) [Debug Only] View enemy fleet\n";
        int choice = -1;

        while (choice != 0) {
            choice = ScannerHelper.clampedQuery(options, 0, 4);
            ConsoleGame.clearConsole();
            if (choice == 0) {
                fire();
            } else if (choice == 1) {
                viewBoard();
            } else if (choice == 2) {
                peek();
            } else if (choice == 3) {
                printHelp();
            } else {
                cheatViewEnemy();
            }
            System.out.println();
        }
    }

    /*
    EFFECTS:
        - Repeatedly query the player to select a ship based on an identifier
        - Returns a Ship when a valid ship is selected
     */
    public Ship selectShip() {
        System.out.print("Pick a valid ship identifier: ");
        String i;
        do {
            i = ScannerHelper.in.nextLine();
            if (i.length() == 0) {
                System.out.println("Invalid, try again.");
            }
        } while (i.length() == 0);

        for (Ship sh : getBoard().getShips()) {
            if (sh.getIdentifier() == i.charAt(0) && sh.isAlive()) {
                return sh;
            }
        }
        System.out.println("Invalid identifier, or ship is dead, try again...");
        return selectShip();
    }

    // EFFECTS: Print the user a help screen
    public void printHelp() {
        System.out.println("When prompted for a coordinate (x,y), (0, 0) is relative to the top left.");
        System.out.println("Positive in the x-axis is right, and positive in the y-axis is down.");
        System.out.println("This means (1,1) is 1 unit to the right, and 1 down.");
        System.out.println("\nIn terms of firing: ");
        printFireHelp();
        System.out.println("\nIn terms of interpreting the map: ");
        printMapHelp();
    }

    // EFFECTS: Show the user a help message on how to interpret the "map" of the game
    public void printMapHelp() {
        System.out.println("When you view the your own fleet, you may see a variety of symbols");
        System.out.printf("%c represents an empty spot on the board, unoccupied.\n", Board.EMPTY_SPOT);
        System.out.printf("%c represents an where a shell was fired on the board, but missed.\n", Board.MISS_SPOT);
        System.out.printf("%c represents an where a shell was fired on the board, and hit!.\n", Board.HIT_SPOT);
        System.out.println("All other sequences of characters (a-Z), symbols, etc, represent a ship.\n");
        System.out.println("When you view the battlefield, you do not see the enemy ships! Just what you've fired at.");
    }

    // EFFECTS: Show the user a help message on the benefits of firing with different ships
    public void printFireHelp() {
        System.out.printf("If we use a smaller ship (size <= %d)", Ship.SCATTER_SHOT_LENGTH);
        System.out.println(", we can load a scattershot shell -\na shell that fires a smaller shell nearby.");
        System.out.printf("If we use a bigger ship (size > %d),", Ship.SCATTER_SHOT_LENGTH);
        System.out.println(" we can load multiple shells -\nso we can fire multiple times.");
    }

    // EFFECTS: Query the user to fire a scattershot at a position, returning number of hits based on scatter shell
    // MODIFIES: this
    public int fireScatterShot() {
        peek();
        System.out.println("We picked a scattershot firing ship.");
        System.out.println("Where shall we fire?");
        Position p = ConsoleGame.queryForPosition(getOpponentBoard().getBoardSize());
        int boardSize = getOpponentBoard().getBoardSize();
        Position scatter = getOpponentBoard().generateRandomPosition(p, new Random());
        int a = getOpponentBoard().fireOnPosition(p) ? 1 : 0;
        System.out.printf("Shell scattered, and landed on (%d, %d)!\n", scatter.getPosX(), scatter.getPosY());
        int b = getOpponentBoard().fireOnPosition(scatter) ? 1 : 0;
        return a + b;
    }

    // EFFECTS: Query the user to fire multiple shots based on number
    // MODIFIES: this
    public int fireMultipleShots(int number) {
        System.out.printf("We picked a multiple shell rapid fire ship, capable of firing %d shots\n", number);
        int num = 0;
        for (int i = 0; i < number; i++) {
            peek();
            System.out.printf("[%d] Where shall we fire?\n", i + 1);
            Position p = ConsoleGame.queryForPosition(getOpponentBoard().getBoardSize());
            if (getOpponentBoard().fireOnPosition(p)) {
                num++;
            }
        }
        return num;
    }

    // EFFECTS: Query the user to select a ship to fire with
    // MODIFIES: this
    @Override
    public void fire() {
        try {
            viewBoard();
            System.out.println("Which ship should we use to fire?");
            Ship sh = selectShip();
            int hits;
            if (sh.getLength() <= Ship.SCATTER_SHOT_LENGTH) {
                hits = fireScatterShot();
            } else {
                hits = fireMultipleShots(sh.getLength() / 2);
            }
            Thread.sleep(1000);
            peek();
            System.out.printf("Captain, we hit the enemy %d time(s).\n", hits);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    /*
    EFFECTS:
        - Query the user to create a ship through the command line, and return it
        - The max size of the ship is bound by pointsRemaining
    REQUIRES:
        - pointsRemaining > 0
     */
    public Ship createShip(int pointsRemaining) {
        System.out.printf("We have a total of %d points remaining.\n", pointsRemaining);
        int length;

        do {
            length = ScannerHelper.clampedQuery("Length: ", 2, pointsRemaining);
            if (length % 2 != 0 || length < 0) {
                System.out.println("Invalid length, try again.");
            }
        } while (length % 2 != 0 || length < 0);

        System.out.printf("Okay, we have a ship of %d length. Where should we put it?\n", length);
        System.out.println("Remember, (0, 0) is the top left, so (1,1) is one unit right and one unit down.");
        Position p = ConsoleGame.queryForPosition(getOpponentBoard().getBoardSize());

        System.out.println("Okay, we know where to put it. Now which orientation?");
        Orientation o = ConsoleGame.queryForOrientation();

        return new Ship(length, getBoard().generateIdentifier(), p, o);
    }

    // EFFECTS: Query the player to place down their ships
    // MODIFIES: this
    // REQUIRES: number > 0
    @Override
    public void placeShips(int number) {
        int points = number;
        System.out.println("Captain, it's time to lay out our navy...");
        System.out.printf("We have %d points to spend on our ships - ", number);
        System.out.println("where 1 point = 1 unit length on a ship. A ship have an even size and > 0.");

        while (points > 0) {
            Ship s = createShip(points);
            if (getBoard().addShip(s)) {
                points -= s.getLength();

                System.out.println("We placed down the specified ship captain!");
                viewBoard();
            } else {
                System.out.println("We can't put a boat there captain, let's try again..");
            }
        }
        System.out.println("We are ready captain!");
    }
}
