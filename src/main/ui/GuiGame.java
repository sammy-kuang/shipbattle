package ui;

import model.Ship;

import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class GuiGame {
    private GuiPlayer player;
    private RandomController enemy;

    private GuiBoard playerBoard;
    private GuiBoard enemyBoard;

    private int boardSize = 10;
    private int numShips = 10;

    private GuiSetupMenu setupMenu;
    private GuiGameStateTextBox gameState;

    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 850;

    // Placing down ships
    private Ship shipToBePlaced;

    // EFFECTS: Create an instance of the GUI version of the game
    public GuiGame() {
        JFrame f = createSettingsMenu();
        f.setVisible(true);
    }


    // EFFECTS: Set up the game given the options from the setup menu
    // MODIFIES: this
    private void setupGame() {
        playerBoard = new GuiBoard(getBoardSize(), "Player");
        playerBoard.showAllShips();
        enemyBoard = new GuiBoard(getBoardSize(), "Enemy");
        enemyBoard.showHitLocations();

        createSetupMenu();
        createMainGame();

        player = new GuiPlayer(playerBoard, enemyBoard, gameState);
        enemy = new RandomController(enemyBoard, playerBoard, (text) -> gameState.write(text));

        player.placeShips(getNumShips());
    }


    // EFFECTS: Create a window that displays the game
    // REQUIRES: Called after setupGame()
    private void createMainGame() {
        JFrame window = new JFrame();
        JPanel content = new JPanel();
        JPanel rhs = new JPanel();
        JPanel grids = new JPanel();

        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        grids.setLayout(new BoxLayout(grids, BoxLayout.Y_AXIS));

        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        grids.add(enemyBoard.getContent());
        grids.add(playerBoard.getContent());
        grids.setMinimumSize(new Dimension(WINDOW_WIDTH / 2, WINDOW_HEIGHT));
        grids.setMaximumSize(new Dimension(WINDOW_WIDTH / 2, WINDOW_HEIGHT));
        grids.setSize(new Dimension(WINDOW_WIDTH / 2, WINDOW_HEIGHT));

        content.add(grids);
        rhs.add(setupMenu);
        gameState = new GuiGameStateTextBox();
        rhs.add(gameState.getScrollPane());
        content.setSize(new Dimension(WINDOW_WIDTH / 2, WINDOW_HEIGHT));
        content.add(rhs);
        window.add(content);

        window.pack();
        window.setResizable(false);
        window.setVisible(true);
    }

    // EFFECTS: Create menu to prompt the user for the settings of the game
    // MODIFIES: this
    private JFrame createSettingsMenu() {
        JFrame window = new JFrame();
        JPanel content = new JPanel();
        SpinnerNumberModel boardSizeModel = new SpinnerNumberModel(10, 10, 25, 1);
        SpinnerNumberModel numShipsModel = new SpinnerNumberModel(10, 10, 16, 2);

        JSpinner boardSize = new JSpinner(boardSizeModel);
        JSpinner numShips = new JSpinner(numShipsModel);

        boardSizeModel.addChangeListener(e -> setBoardSize((int)boardSizeModel.getValue()));

        numShips.addChangeListener(e -> setNumShips((int)numShipsModel.getValue()));

        content.add(new JLabel("Board size: "));
        content.add(boardSize);
        content.add(new JLabel("Number of ships: "));
        content.add(numShips);

        Button done = new Button("Start");
        done.addActionListener(e -> {
            window.setVisible(false);
            window.dispose();
            setupGame();
        });
        content.add(done);
        window.add(content);
        window.pack();

        return window;
    }

    // EFFECTS: Create the setup menu that allows the user to configure and place their ships
    // MODIFIES: this
    public void createSetupMenu() {
        this.setupMenu = new GuiSetupMenu(getNumShips(), null);

        // When we click on the "Create ship" button
        this.setupMenu.setOnClick((newValue, orientation) -> {
            if (shipToBePlaced != null) {
                return; // We don't want to create a new ship if we are placing something down
            }
            String s = String.format("Creating ship of size %d of orientation %s", newValue, orientation.toString());
            gameState.write(s);
            shipToBePlaced = new Ship(newValue, playerBoard.generateIdentifier(), null, orientation);
            playerBoard.setGridClicked((i, position) -> {
                shipToBePlaced.setPosition(position);
                if (playerBoard.addShip(shipToBePlaced)) {
                    shipToBePlaced = null;
                    playerBoard.showAllShips();
                } else {
                    gameState.write("Failed to put a ship there, try again.");
                }
            });
            setupMenu.setRemainingPoints(setupMenu.getRemainingPoints() - newValue);
            setupMenu.setOnClick((newValue1, orientation1) -> {

            });
        });
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getNumShips() {
        return numShips;
    }

    public void setNumShips(int numShips) {
        this.numShips = numShips;
    }
}
