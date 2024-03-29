package model;

import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BoardTests {
    Board board;

    @BeforeEach
    void init() {
        board = new Board(10);
    }

    @Test
    void testEmptyBoard() {
        assertTrue(board.getShips().isEmpty());

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                assertEquals(Board.EMPTY_SPOT, board.getBoard()[y][x]);
            }
        }
    }

    @Test
    void testBoardSize() {
        assertTrue(board.getShips().isEmpty());
        assertEquals(10, board.getBoardSize());
    }

    @Test
    void testIdentifiers() {
        assertEquals('A', board.generateIdentifier());
    }

    @Test
    void testAddDuplicateShip() {
        Ship a = new Ship(4, 'A', new Position(0, 0), Orientation.LeftRight);
        assertTrue(board.addShip(a));
        assertFalse(board.addShip(a));
    }

    @Test
    void testAddShipsNoBlock() {
        Ship a = new Ship(4, 'A', new Position(0, 0), Orientation.LeftRight);
        assertTrue(board.addShip(a));
        assertEquals(1, board.getShips().size());
        for (int i = 0; i < 4; i++) {
            assertEquals('A', board.getBoard()[0][i]);
        }

        Ship b = new Ship(4, 'B', new Position(0, 1), Orientation.LeftRight);
        assertTrue(board.addShip(b));
        assertEquals(2, board.getShips().size());

        for (int i = 0; i < 4; i++) {
            assertEquals('B', board.getBoard()[1][i]);
        }

        Ship c = new Ship(4, 'C', new Position(0, 2), Orientation.UpDown);
        assertTrue(board.addShip(c));
        assertEquals(3, board.getShips().size());

        for (int i = 0; i < 4; i++) {
            assertEquals('C', board.getBoard()[2+i][0]);
        }

    }

    @Test
    void testSingleBlocking() {
        Ship a = new Ship(4, 'A', new Position(0, 0), Orientation.LeftRight);
        Ship b = new Ship(4, 'B', new Position(3, 0), Orientation.LeftRight);

        assertTrue(board.addShip(a));
        assertEquals(1, board.getShips().size());
        assertFalse(board.addShip(b));
        assertEquals(1, board.getShips().size());
    }

    @Test
    void testVerticalSingleBlocking() {
        Ship a = new Ship(4, 'A', new Position(0, 0), Orientation.LeftRight);
        Ship b = new Ship(4, 'B', new Position(0, 0), Orientation.UpDown);

        assertTrue(board.addShip(a));
        assertEquals(1, board.getShips().size());
        assertFalse(board.addShip(b));
        assertEquals(1, board.getShips().size());

        for (int i = 0; i < 4; i++) {
            assertEquals('A', board.getBoard()[0][i]);
            assertEquals(Board.EMPTY_SPOT, board.getBoard()[i+1][0]);
        }
    }

    @Test
    void testVerticalSingleBlockingBottom() {
        Ship a = new Ship(4, 'A', new Position(0, 3), Orientation.LeftRight);
        Ship b = new Ship(4, 'B', new Position(0, 0), Orientation.UpDown);

        assertTrue(board.addShip(a));
        assertEquals(1, board.getShips().size());
        assertFalse(board.addShip(b));
        assertEquals(1, board.getShips().size());
        assertEquals(Board.EMPTY_SPOT, board.getBoard()[0][0]);
    }

    @Test
    void testShipOutOfBounds() {
        Ship a = new Ship(4, 'A', new Position(9, 0), Orientation.LeftRight);

        assertFalse(board.addShip(a));
        assertEquals(0, board.getShips().size());
    }

    @Test
    void testVerticalShipOutOfBounds() {
        Ship a = new Ship(4, 'A', new Position(0, 9), Orientation.UpDown);
        assertFalse(board.addShip(a));
        assertEquals(0, board.getShips().size());
    }

    @Test
    void testPositionToShip() {
        Ship a = new Ship(4, 'A', new Position(0, 0), Orientation.LeftRight);
        assertTrue(board.addShip(a));

        for (int i = 0; i < 4; i++) {
            Ship s = board.positionToShip(new Position(i, 0));
            assertNotNull(s);
            assertEquals(a, s);
        }
        assertNull(board.positionToShip(new Position(4, 0)));
        Ship b = new Ship(4, 'B', new Position(4, 0), Orientation.LeftRight);
        assertTrue(board.addShip(b));
        assertNotNull(board.positionToShip(new Position(4, 0)));
        for (int i = 4; i < 8; i++) {
            Ship s = board.positionToShip(new Position(i, 0));
            assertNotNull(s);
            assertEquals(b, s);
        }
    }
    @Test
    void testPositionToShipBothDirs() {
        Ship a = new Ship(4, 'A', new Position(0, 0), Orientation.LeftRight);
        assertTrue(board.addShip(a));
        Ship b = new Ship(4, 'B', new Position(0, 1), Orientation.UpDown);
        assertTrue(board.addShip(b));
        assertNull(board.positionToShip(new Position(1,1)));

        for (int i = 0; i < 4; i++) {
            assertEquals(a, board.positionToShip(new Position(i, 0)));
        }

        for (int i = 1; i < 5; i++) {
            assertEquals(b, board.positionToShip(new Position(0, i)));
        }
    }

    @Test
    void testFireOnShipLeft() {
        Ship a = new Ship(4, 'A', new Position(0, 0), Orientation.LeftRight);
        assertTrue(board.addShip(a));
        assertTrue(board.fireOnPosition(new Position(0, 0)));
        assertFalse(board.fireOnPosition(new Position(0, 0)));
        assertEquals(Ship.DEAD, a.getHealth()[0]);
        assertEquals(Board.HIT_SPOT, board.getBoard()[0][0]);

        assertFalse(board.fireOnPosition(new Position(0, 1)));
        assertEquals(Board.MISS_SPOT, board.getBoard()[1][0]);
    }

    @Test
    void testFireOnMultipleShips() {
        Ship a = new Ship(4, 'A', new Position(3, 0), Orientation.LeftRight);
        Ship b = new Ship(4, 'B', new Position(0, 1), Orientation.UpDown);
        Ship c = new Ship(4, 'C', new Position(2, 2), Orientation.LeftRight);

        assertTrue(board.addShip(a));
        assertTrue(board.addShip(b));
        assertTrue(board.addShip(c));

        assertTrue(board.fireOnPosition(new Position(4, 0)));
        assertTrue(board.fireOnPosition(new Position(0, 1)));
        assertTrue(board.fireOnPosition(new Position(0, 2)));
        assertTrue(board.fireOnPosition(new Position(3, 2)));
        assertFalse(board.fireOnPosition(new Position(6, 2)));
        assertFalse(board.fireOnPosition(new Position(6, 2)));
        assertFalse(board.fireOnPosition(new Position(1, 2)));

        assertTrue(a.isAlive());
        assertTrue(b.isAlive());
        assertTrue(c.isAlive());
        assertTrue(board.isAlive());


        assertTrue(board.fireOnPosition(new Position(3, 0)));
        assertFalse(board.fireOnPosition(new Position(4, 0)));
        assertTrue(board.fireOnPosition(new Position(5, 0)));
        assertTrue(board.fireOnPosition(new Position(6, 0)));
        assertFalse(a.isAlive());
    }

    @Test
    void testDestroyAllShips() {
        Ship a = new Ship(4, 'A', new Position(0, 0), Orientation.LeftRight);
        assertTrue(board.addShip(a));
        assertTrue(board.fireOnPosition(new Position(0, 0)));
        assertTrue(board.fireOnPosition(new Position(1, 0)));
        assertTrue(board.fireOnPosition(new Position(2, 0)));
        assertTrue(board.fireOnPosition(new Position(3, 0)));
        assertFalse(board.fireOnPosition(new Position(4, 0)));

        assertFalse(a.isAlive());
        assertFalse(board.isAlive());
    }

    @Test
    void testRandomPosition() {
        Position p = board.generateRandomPosition();
        assertNotNull(p);
        assertTrue(p.getPosX() >= 0);
        assertTrue(p.getPosX() < board.getBoardSize());
        assertTrue(p.getPosY() >= 0);
        assertTrue(p.getPosY() < board.getBoardSize());
    }

    @Test
    void testRandomPositionAroundPos() {
        Position i = new Position(3, 3);
        // TESTING RANDOM SO AWESOME!!!
        Random odd = new Random() {
            @Override
            public int nextInt() {
                return 1;
            }
        };

        Random even = new Random() {
            @Override
            public int nextInt() {
                return 2;
            }
        };

        Position a = board.generateRandomPosition(i, odd);
        int a_x = Math.abs(i.getPosX() - a.getPosX());
        int a_y = Math.abs(i.getPosY() - a.getPosY());
        assertTrue(a_x == 1 && a_y == 1);

        Position b = board.generateRandomPosition(i, even);
        int b_x = Math.abs(i.getPosX() - b.getPosX());
        int b_y = Math.abs(i.getPosY() - b.getPosY());
        assertTrue(b_x == 1 && b_y == 1);
    }

    @Test
    void testGetShipByIdentifier() {
        Ship s = new Ship(6, 'A', new Position(0, 0), Orientation.LeftRight);
        Ship m = new Ship(6, 'B', new Position(1, 1), Orientation.LeftRight);

        assertTrue(board.addShip(s));
        assertTrue(board.addShip(m));

        assertEquals(s, board.getShipByIdentifier('A'));
        assertEquals(m, board.getShipByIdentifier('B'));
        assertNull(board.getShipByIdentifier('C'));
    }

    @Test
    void testPositionString() {
        assertEquals("(1,2)", new Position(1,2).toString());
        assertEquals("(2,1)", new Position(2,1).toString());
        assertEquals("(0,0)", new Position(0,0).toString());
    }
}