package persistence;

import model.Board;
import model.Orientation;
import model.Position;
import model.Ship;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTests {

    Board board;
    Ship a;
    Ship b;

    @BeforeEach
    void init() {
        board = new Board(10);
        a = new Ship(6, 'A', new Position(0, 0), Orientation.LeftRight);
        b = new Ship(4, 'B', new Position(1, 1), Orientation.UpDown);
        board.addShip(a);
        board.addShip(b);
    }

    @Test
    void testPositionSaving() {
        JSONObject pos = a.getPosition().save();
        assertEquals(0, pos.getInt("posX"));
        assertEquals(0, pos.getInt("posY"));
    }

    @Test
    void testShipSavingNoDamage() {
        JSONObject aSave = a.save();
        assertEquals(6, aSave.getInt("length"));
        assertEquals('A', (char)aSave.getInt("identifier"));
        JSONObject aPosition = aSave.getJSONObject("position");
        assertEquals(0, aPosition.getInt("posX"));
        assertEquals(0, aPosition.getInt("posY"));
        JSONArray aHealth = aSave.getJSONArray("health");
        for (Object h : aHealth) {
            assertEquals(Ship.ALIVE, (int)h);
        }
        assertEquals(Orientation.LeftRight, aSave.get("orientation"));
    }

    @Test
    void testShipSavingDamage() {
        board.fireOnPosition(new Position(0, 0));
        JSONObject aSave = a.save();
        assertEquals(6, aSave.getInt("length"));
        assertEquals('A', (char)aSave.getInt("identifier"));
        JSONObject aPosition = aSave.getJSONObject("position");
        assertEquals(0, aPosition.getInt("posX"));
        assertEquals(0, aPosition.getInt("posY"));
        JSONArray aHealth = aSave.getJSONArray("health");
        assertEquals(Ship.DEAD, aHealth.getInt(0));
        for (int i = 1; i < aHealth.length(); i++) {
            assertEquals(Ship.ALIVE, aHealth.getInt(i));
        }
        assertEquals(Orientation.LeftRight, aSave.get("orientation"));
    }

    @Test
    void testShipSavingDead() {
        for (int i = 0; i < a.getLength(); i++) {
            board.fireOnPosition(new Position(i, 0));
        }
        assertFalse(a.isAlive());
        JSONObject aSave = a.save();
        assertEquals(6, aSave.getInt("length"));
        assertEquals('A', (char)aSave.getInt("identifier"));
        JSONObject aPosition = aSave.getJSONObject("position");
        assertEquals(0, aPosition.getInt("posX"));
        assertEquals(0, aPosition.getInt("posY"));
        JSONArray aHealth = aSave.getJSONArray("health");
        for (int i = 0; i < aHealth.length(); i++) {
            assertEquals(Ship.DEAD, aHealth.getInt(i));
        }
        assertEquals(Orientation.LeftRight, aSave.get("orientation"));
    }

    @Test
    void testBoardSavingNoShots() {
        JSONObject boardJson = board.save();
        JSONArray ships = boardJson.getJSONArray("ships");
        int boardSize = boardJson.getInt("boardSize");
        String boardString = boardJson.getString("board");

        assertTrue(boardString.contains("AAAAAA"));

        for (int y = 0; y < b.getLength(); y++) {
            assertEquals('B', boardString.charAt(11+(y*10)));
        }

        assertEquals(10, boardSize);
        assertEquals(2, ships.length());

        // test A ship
        JSONObject aSave = ships.getJSONObject(0);
        assertEquals(6, aSave.getInt("length"));
        assertEquals('A', (char)aSave.getInt("identifier"));
        JSONObject aPosition = aSave.getJSONObject("position");
        assertEquals(0, aPosition.getInt("posX"));
        assertEquals(0, aPosition.getInt("posY"));
        JSONArray aHealth = aSave.getJSONArray("health");
        for (Object h : aHealth) {
            assertEquals(Ship.ALIVE, (int)h);
        }
        assertEquals(Orientation.LeftRight, aSave.get("orientation"));

        // test B ship
        JSONObject bSave = ships.getJSONObject(1);
        assertEquals(4, bSave.getInt("length"));
        assertEquals('B', (char)bSave.getInt("identifier"));
        JSONObject bPosition = bSave.getJSONObject("position");
        assertEquals(1, bPosition.getInt("posX"));
        assertEquals(1, bPosition.getInt("posY"));
        JSONArray bHealth = bSave.getJSONArray("health");
        for (Object h : bHealth) {
            assertEquals(Ship.ALIVE, (int)h);
        }
        assertEquals(Orientation.UpDown, bSave.get("orientation"));
    }

    @Test
    void testBoardSavingShots() {
        board.fireOnPosition(new Position(0, 0));
        JSONObject boardJson = board.save();
        JSONArray ships = boardJson.getJSONArray("ships");
        int boardSize = boardJson.getInt("boardSize");
        String boardString = boardJson.getString("board");

        assertFalse(boardString.contains("AAAAAA"));
        assertTrue(boardString.contains(Board.HIT_SPOT+"AAAAA"));

        for (int y = 0; y < b.getLength(); y++) {
            assertEquals('B', boardString.charAt(11+(y*10)));
        }

        assertEquals(10, boardSize);
        assertEquals(2, ships.length());

        // test A ship
        JSONObject aSave = ships.getJSONObject(0);
        assertEquals(6, aSave.getInt("length"));
        assertEquals('A', (char)aSave.getInt("identifier"));
        JSONObject aPosition = aSave.getJSONObject("position");
        assertEquals(0, aPosition.getInt("posX"));
        assertEquals(0, aPosition.getInt("posY"));
        JSONArray aHealth = aSave.getJSONArray("health");
        assertEquals(Ship.DEAD, aHealth.getInt(0));
        for (int i = 1; i < aHealth.length(); i++) {
            assertEquals(Ship.ALIVE, aHealth.getInt(i));
        }
        assertEquals(Orientation.LeftRight, aSave.get("orientation"));

        // test B ship
        JSONObject bSave = ships.getJSONObject(1);
        assertEquals(4, bSave.getInt("length"));
        assertEquals('B', (char)bSave.getInt("identifier"));
        JSONObject bPosition = bSave.getJSONObject("position");
        assertEquals(1, bPosition.getInt("posX"));
        assertEquals(1, bPosition.getInt("posY"));
        JSONArray bHealth = bSave.getJSONArray("health");
        for (Object h : bHealth) {
            assertEquals(Ship.ALIVE, (int)h);
        }
        assertEquals(Orientation.UpDown, bSave.get("orientation"));
    }

    @Test
    void testFileReadWrite() {
        try {
            PersistenceManager.writeToFile(board.save(), "data/test1.json");
            Board newBoard = PersistenceManager.loadBoard("data/test1.json");
            assertEquals(board.getBoardSize(), newBoard.getBoardSize());
            assertEquals(board.getShips().size(), newBoard.getShips().size());

            for (int i = 0; i < board.getShips().size(); i++) {
                Ship orig = board.getShips().get(i);
                Ship compare = newBoard.getShips().get(i);
                assertEquals(orig.isAlive(), compare.isAlive());
                assertEquals(orig.getIdentifier(), compare.getIdentifier());
                for (int h = 0; h < orig.getHealth().length; h++) {
                    assertEquals(orig.getHealth()[h], compare.getHealth()[h]);
                }
            }

            File tf = new File("data/test1.json");
            tf.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSaveGame() {
        try {
            if (PersistenceManager.doSavesExist()) {
                new File(PersistenceManager.PLAYER_SAVE).delete();
                new File(PersistenceManager.OPPONENT_SAVE).delete();
                assertFalse(PersistenceManager.doSavesExist());
            }
            PersistenceManager.saveGame(board, board);
            assertTrue(PersistenceManager.doSavesExist());
            new File(PersistenceManager.PLAYER_SAVE).deleteOnExit();
            new File(PersistenceManager.OPPONENT_SAVE).deleteOnExit();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
