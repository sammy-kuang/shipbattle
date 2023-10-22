package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Persistable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Represents the board for a single player. This is where a player would place their own ships.
public class Board implements Persistable {

    public static final char EMPTY_SPOT = '~';
    public static final char HIT_SPOT = 'X';
    public static final char MISS_SPOT = 'O';

    private final ArrayList<Ship> ships;
    private final int boardSize;



    private char[][] board; // where the first index represents Y and second index represents x

    // REQUIRES: 10 >= boardSize > 4
    // EFFECTS: Creates a game board of the designated size
    public Board(int boardSize) {
        ships = new ArrayList<>();
        this.boardSize = boardSize;
        board = new char[boardSize][boardSize];

        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                board[y][x] = EMPTY_SPOT;
            }
        }
    }

    /*
    REQUIRES:
        - ship.getPosition() is within the board's specified size ( (10,10) would not be in boardSize == 10 )
          but (9,9) is within boardSize == 10.
        - ship.getIdentifier() is not found on another ship in ships
    EFFECTS:
        - returns false if we are unable to put down the ship
        - returns false if board already contains ship
        - returns true if ship is put down at its requested position

    */
    public boolean addShip(Ship ship) {
        if (ships.contains(ship)) {
            return false;
        }

        char[][] copy = new char[boardSize][boardSize];

        // let's clone the current board, so we can fall back onto it
        for (int y = 0; y < boardSize; y++) {
            System.arraycopy(board[y], 0, copy[y], 0, boardSize);
        }

        try {
            for (int i = 0; i < ship.getLength(); i++) {
                if (ship.getOrientation() == Orientation.UpDown) {
                    int newY = ship.getPosition().getPosY() + i;
                    placeIdentifier(newY, ship.getPosition().getPosX(), ship.getIdentifier());
                } else {
                    int newX = ship.getPosition().getPosX() + i;
                    placeIdentifier(ship.getPosition().getPosY(), newX, ship.getIdentifier());
                }
            }
        } catch (Exception e) {
            // Restore initial state
            this.board = copy;
            return false;
        }
        ships.add(ship);
        return true;
    }

    /*
    EFFECTS:
        - Return a ship if one is found at ship on this.board
        - Return null if none are found
     */
    public Ship positionToShip(Position position) {
        char c = board[position.getPosY()][position.getPosX()];
        if (c == EMPTY_SPOT) {
            return null;
        }
        for (Ship s : ships) {
            if (s.getIdentifier() == c) {
                return s;
            }
        }
        return null;
    }

    /*
    EFFECTS:
        - Return true if all ships in this.ships are alive
        - false if not
     */
    public boolean isAlive() {
        for (Ship s : ships) {
            if (s.isAlive()) {
                return true;
            }
        }
        return false;
    }

    /*
    REQUIRES: position is within the bounds of the board
    MODIFIES: this
    EFFECTS:
        - Return true if a ship was hit at position
        - Return false if a ship was already hit at that position
        - Return false if otherwise
     */
    public boolean fireOnPosition(Position position) {
        Ship s = positionToShip(position);
        char spot = board[position.getPosY()][position.getPosX()];

        if (spot == Board.HIT_SPOT || spot == Board.MISS_SPOT) {
            return false;
        }

        if (s == null) {
            board[position.getPosY()][position.getPosX()] = Board.MISS_SPOT;
            return false;
        }
        // Get the respective index on the ship's health
        int x = position.getPosX() - s.getPosition().getPosX();
        int y = position.getPosY() - s.getPosition().getPosY();
        int ind = Math.abs(x + y);

        board[position.getPosY()][position.getPosX()] = Board.HIT_SPOT;

        s.getHealth()[ind] = Ship.DEAD;
        return true;
    }

    // MODIFIES: this
    // EFFECTS: Place an identifier on this.board at (x,y), given that it is EMPTY_SPOT there
    private void placeIdentifier(int y, int x, char identifier) throws Exception {
        if (board[y][x] != EMPTY_SPOT) {
            throw new Exception(String.format("Occupied on (%d, %d)", x, y));
        }
        board[y][x] = identifier;
    }

    // EFFECTS: Generate a single character identifier for a ship that is not already used
    // REQUIRES: ships.size() < 57 (allows for a variety of alphanumeric characters + special chars)
    public char generateIdentifier() {
        return (char)(65 + ships.size());
    }

    // EFFECTS: Generate a random position within the board
    public Position generateRandomPosition() {
        Random r = new Random();
        int x = r.nextInt(getBoardSize());
        int y = r.nextInt(getBoardSize());
        return new Position(x,y);
    }

    // EFFECTS: Generate a random position on the board around or on position using random
    // REQUIRES: position is within the board
    public Position generateRandomPosition(Position position, Random random) {
        int n;
        n = random.nextInt() % 2 == 0 ? -1 : 1;
        int x = Utils.clamp(position.getPosX() + n, 0, getBoardSize() - 1);
        int y = Utils.clamp(position.getPosY() + n, 0, getBoardSize() - 1);
        return new Position(x, y);
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public List<Ship> getShips() {
        return ships;
    }

    @Override
    public JSONObject save() {
        JSONObject out = new JSONObject();
        JSONArray outShips = new JSONArray();
        for (Ship s : ships) {
            outShips.put(s.save());
        }
        out.put("ships", outShips);
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                sb.append(board[y][x]);
            }
        }
        out.put("boardSize", boardSize);
        out.put("board", sb.toString());
        return out;
    }
}
