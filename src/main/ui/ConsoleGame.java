package ui;

import model.*;

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

    void playGame(int boardSize, int numShips) {
        try {
            Board playerBoard = new Board(boardSize);
            Board opponentBoard = new Board(boardSize);
            int turn = 0;
            ConsolePlayer p = new ConsolePlayer(playerBoard, opponentBoard);
            RandomController o = new RandomController(opponentBoard, playerBoard);
            Controller[] players = new Controller[]{p, o};
            p.placeShips(numShips);
            o.placeShips(numShips);
            System.out.println("\nGame is ready to go!");
            while (playerBoard.isAlive() && opponentBoard.isAlive()) {
                players[turn].turn();
                turn = (turn + 1) % 2;
                Thread.sleep(2000);
            }
            if (playerBoard.isAlive()) {
                System.out.println("Congratulations captain, we won!");
            } else {
                System.out.println("We lost...");
            }
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    // EFFECTS: Run the game in a console window
    void run() {
        System.out.println("Welcome to 𝕊𝕙𝕚𝕡𝕓𝕒𝕥𝕥𝕝𝕖!");
        String boardSizeMessage = "Please insert a board size where 10 <= size <= 25  (recommended 10): ";
        int boardSize = ScannerHelper.clampedQuery(boardSizeMessage, 10, 25);
        String numShipsMessage = "Please insert the number of points for ships (even & between 10-16): ";
        int numShips;
        do {
            numShips = ScannerHelper.clampedQuery(numShipsMessage, 10, 16);
        } while (numShips % 2 != 0);
        playGame(boardSize, numShips);
    }
}
