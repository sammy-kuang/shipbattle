package persistence;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

// In charge of loading and saving the entire state of the game
// Modelled after JsonWriter and JsonReader provided in course
public final class PersistenceManager {

    public static final int TAB = 4;
    public static final String PLAYER_SAVE = "data/player.json";
    public static final String OPPONENT_SAVE = "data/opponent.json";

    // EFFECTS: Cannot be instantiated
    private PersistenceManager() {

    }

    // EFFECTS: Write a JSONObject to destination
    // REQUIRES: destination is a valid path
    public static void writeToFile(JSONObject json, String destination) throws FileNotFoundException {
        PrintWriter w = new PrintWriter(destination);
        w.write(json.toString(TAB));
        w.close();
    }

    // EFFECTS: Save the player and opponent board to respective paths
    public static void saveGame(Board player, Board opponent) throws FileNotFoundException {
        writeToFile(player.save(), PLAYER_SAVE);
        writeToFile(opponent.save(), OPPONENT_SAVE);
    }

    // EFFECTS: Return whether a game has been saved previously or not
    public static boolean doSavesExist() {
        return (new File(PLAYER_SAVE).exists()) && (new File(OPPONENT_SAVE).exists());
    }

    // EFFECTS: Return a Board based on a JSON loaded from path, modelled after JsonReader from example
    public static Board loadBoard(String path) throws IOException {
        StringBuilder content = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
            stream.forEach(content::append);
        }
        JSONObject json = new JSONObject(content.toString());
        JSONArray ships = json.getJSONArray("ships");
        int boardSize = json.getInt("boardSize");
        String boardArr = json.getString("board");
        Board board = new Board(boardSize);
        char[][] b = new char[boardSize][boardSize];
        for (Object o : ships) {
            JSONObject js = (JSONObject)o;
            JSONObject jp = (JSONObject) js.get("position");
            Position p = new Position(jp.getInt("posX"), jp.getInt("posY"));
            Orientation orient = Orientation.valueOf(js.getString("orientation"));
            Ship s = new Ship(js.getInt("length"), (char)js.getInt("identifier"), p, orient);
            board.addShip(s);
        }
        for (int a = 0; a < boardSize * boardSize; a++) {
            b[a / boardSize][a % boardSize] = boardArr.charAt(a);
        }
        board.setBoard(b);
        return board;
    }

}
