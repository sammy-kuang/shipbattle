package ui;

import model.*;
import persistence.PersistenceManager;

import java.io.IOException;

// Represent an instance of the game running in the terminal
public class ConsoleGame {
    // EFFECTS: Create and run an instance of the game in the terminal
    public ConsoleGame() {
        this.run();
    }

    // EFFECTS: Print out the board onto the console
    public static void printBoard(Board board) {
        for (int y = 0; y < board.getBoardSize(); y++) {
            for (int x = 0; x < board.getBoardSize(); x++) {
                System.out.print(board.getBoard()[y][x]);
            }
            System.out.println();
        }
    }

    /*
    EFFECTS: Print board out into the console, hiding any pieces that are there,
             showing a hit, miss, or empty spot in the places of pieces.
     */
    public static void printObfuscatedBoard(Board board) {
        for (int y = 0; y < board.getBoardSize(); y++) {
            for (int x = 0; x < board.getBoardSize(); x++) {
                char c = board.getBoard()[y][x];

                if (c != Board.MISS_SPOT && c != Board.HIT_SPOT) {
                    System.out.print(Board.EMPTY_SPOT);
                } else {
                    System.out.print(c);
                }
            }
            System.out.println();
        }
    }

    // EFFECTS: Clear the console by spamming new lines
    public static void clearConsole() {
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
    }

    // EFFECTS: Query the user for a position within boardSize, and return it
    public static Position queryForPosition(int boardSize) {
        int x = ScannerHelper.clampedQuery("x: ", 0, boardSize - 1);
        int y = ScannerHelper.clampedQuery("y: ", 0, boardSize - 1);

        return new Position(x, y);
    }

    // EFFECTS: Query the user for an Orientation, and return it
    public static Orientation queryForOrientation() {
        int o = ScannerHelper.clampedQuery("0 for left-right, 1 for up-down: ", 0, 1);
        return o == 0 ? Orientation.LeftRight : Orientation.UpDown;
    }

    // EFFECTS: Run the main game loop
    // REQUIRES: player != opponent
    void gameLoop(Controller player, Controller opponent) {
        Controller[] players = new Controller[]{player, opponent};
        int turn = 0;
        while (player.getBoard().isAlive() && opponent.getBoard().isAlive()) {
            players[turn].turn();
            turn = (turn + 1) % 2;
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (player.getBoard().isAlive()) {
            System.out.println("Congratulations captain, we won!");
        } else {
            System.out.println("We lost...");
        }
    }

    // EFFECTS: Query the user and opponent to place ships, and run game loop
    void setupGame() {
        String boardSizeMessage = "Please insert a board size where 10 <= size <= 25  (recommended 10): ";
        int boardSize = ScannerHelper.clampedQuery(boardSizeMessage, 10, 25);
        String numShipsMessage = "Please insert the number of points for ships (even & between 10-16): ";
        int numShips;
        do {
            numShips = ScannerHelper.clampedQuery(numShipsMessage, 10, 16);
        } while (numShips % 2 != 0);
        Board playerBoard = new Board(boardSize);
        Board opponentBoard = new Board(boardSize);
        ConsolePlayer p = new ConsolePlayer(playerBoard, opponentBoard);
        RandomController o = new RandomController(opponentBoard, playerBoard, System.out::println);
        p.placeShips(numShips);
        o.placeShips(numShips);
        System.out.println("\nGame is ready to go!");
        gameLoop(p, o);
    }

    // EFFECTS: Run the game in a console window
    void run() {
        System.out.println("Welcome to Shipbattle!");
        boolean needSetup = false;
        if (PersistenceManager.doSavesExist()) {
            needSetup = ScannerHelper.yesNoQuery("We detected a previous game. Would you like to load it?");
        }
        if (needSetup) {
            try {
                Board playerBoard = PersistenceManager.loadBoard(PersistenceManager.PLAYER_SAVE);
                Board opponentBoard = PersistenceManager.loadBoard(PersistenceManager.OPPONENT_SAVE);
                ConsolePlayer player = new ConsolePlayer(playerBoard, opponentBoard);
                RandomController opponent = new RandomController(opponentBoard, playerBoard, System.out::println);
                gameLoop(player, opponent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            setupGame();
        }
        // PRINT OUT ALL EVENTS
        for (Event ev : EventLog.getInstance()) {
            System.out.printf("%s: %s\n", ev.getDate(), ev.getDescription());
        }
    }
}
