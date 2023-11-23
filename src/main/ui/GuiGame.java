package ui;

import model.Board;
import model.Orientation;
import model.Ship;
import persistence.PersistenceManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GuiGame {
    private GuiPlayer player;
    private RandomController enemy;

    private GuiBoard playerBoard;
    private GuiBoard enemyBoard;

    private int boardSize = 10;
    private int numShips = 10;

    private GuiSetupMenu setupMenu;
    private GuiGameStateTextBox gameState;
    private JFrame window;

    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 900;

    // Placing down ships
    private Ship shipToBePlaced;

    // EFFECTS: Create an instance of the GUI version of the game
    public GuiGame() {
        JFrame f = createSettingsMenu();
        f.setLocationRelativeTo(null);
        addLoadingToSettings(f);
        f.setVisible(true);
    }

    // EFFECTS: Set up a game based on previous game data
    // REQUIRES: A game was loaded into the state (playerBoard and enemyBoard initialized)
    // MODIFIES: this
    private void setupLoadedGame() {
        playerBoard.updateGrid();
        enemyBoard.updateGrid();
        createMainGame();
        enemy = new RandomController(enemyBoard, playerBoard, (text) -> gameState.write(text));
        player = new GuiPlayer(playerBoard, enemyBoard, enemy, gameState);
        player.turn();
    }

    // EFFECTS: Set up a new game given the options from the setup menu
    // MODIFIES: this
    private void setupNewGame() {
        playerBoard = new GuiBoard(getBoardSize(), "Player", true);
        enemyBoard = new GuiBoard(getBoardSize(), "Enemy", false);

        playerBoard.updateGrid();
        enemyBoard.updateGrid();

        createSetupMenu();
        createMainGame();

        enemy = new RandomController(enemyBoard, playerBoard, (text) -> gameState.write(text));
        player = new GuiPlayer(playerBoard, enemyBoard, enemy, gameState);

        player.placeShips(getNumShips()); // Query the user to place their ships
        playerBoard.updateGrid();
        enemy.placeShips(getNumShips());
        enemyBoard.updateGrid();
    }


    // EFFECTS: Create a window that displays the game
    // REQUIRES: Called after either setupNewGame() or setupLoadedGame()
    private void createMainGame() {
        window = new JFrame();
        addCloseSavePrompt();
        JPanel content = new JPanel();
        JPanel grids = new JPanel();
        JPanel rhs = createGameStatePanel();
        if (setupMenu != null) {
            rhs.add(setupMenu);
        }
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        grids.setLayout(new BoxLayout(grids, BoxLayout.Y_AXIS));
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        grids.add(enemyBoard.getContent());
        grids.add(playerBoard.getContent());
        grids.setMinimumSize(new Dimension(WINDOW_WIDTH / 2, WINDOW_HEIGHT));
        grids.setMaximumSize(new Dimension(WINDOW_WIDTH / 2, WINDOW_HEIGHT));
        grids.setSize(new Dimension(WINDOW_WIDTH / 2, WINDOW_HEIGHT));
        content.add(grids);
        content.add(rhs);
        window.add(content);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    // EFFECTS: Create a panel to provide information and settings to the user
    private JPanel createGameStatePanel() {
        JPanel rhs = new JPanel();
        rhs.setLayout(new BoxLayout(rhs, BoxLayout.Y_AXIS));
        rhs.add(new JLabel("Info Panel"));

        gameState = new GuiGameStateTextBox();
        rhs.add(gameState.getScrollPane());

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                PersistenceManager.saveGame(playerBoard, enemyBoard);
                gameState.write("Saved the game.");
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton helpButton = new JButton("Print Help");
        helpButton.addActionListener(e -> {
            ConsolePlayer.printHelp(o -> {
                gameState.write(o);
            });
        });

        rhs.add(saveButton);
        rhs.add(helpButton);

        return rhs;
    }

    // EFFECTS: Create menu to prompt the user for the settings of the game
    // MODIFIES: this
    private JFrame createSettingsMenu() {
        JFrame window = new JFrame();
        window.setLayout(new FlowLayout());
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
        JButton done = new JButton("Start");
        done.addActionListener(e -> {
            window.setVisible(false);
            window.dispose();
            setupNewGame();
        });
        content.add(done);
        window.add(content);
        window.pack();
        return window;
    }

    // EFFECTS: Add a load previous game button to the settings menu, if the data exists
    // MODIFIES: settingsMenu
    private void addLoadingToSettings(JFrame settingsMenu) {
        if (PersistenceManager.doSavesExist()) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("We detected a previous save! Load?");
            JButton button = new JButton("Load");
            panel.add(label);
            panel.add(button);
            button.addActionListener(e -> {
                try {
                    Board p = PersistenceManager.loadBoard(PersistenceManager.PLAYER_SAVE);
                    Board o = PersistenceManager.loadBoard(PersistenceManager.OPPONENT_SAVE);
                    playerBoard = new GuiBoard(p, "Player", true);
                    enemyBoard = new GuiBoard(o, "Enemy", false);
                    settingsMenu.setVisible(false);
                    settingsMenu.dispose();
                    setupLoadedGame();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            settingsMenu.add(panel);
            settingsMenu.pack();
        }
    }

    // EFFECTS: Create the setup menu that allows the user to configure and place their ships
    // MODIFIES: this
    public void createSetupMenu() {
        this.setupMenu = new GuiSetupMenu(getNumShips(), null);

        // When we click on the "Create ship" button
        this.setupMenu.setOnClick(this::createShipButton);
    }

    // EFFECTS: Switch the player's board into a ship placing mode when the createShip button is clicked
    // REQUIRES: shipSize > 0 && shipSize is even
    // MODIFIES: this
    private void createShipButton(int shipSize, Orientation orientation) {
        if (shipToBePlaced != null) {
            return; // We don't want to create a new ship if we are placing something down
        }
        String s = String.format("Creating ship of size %d of orientation %s", shipSize, orientation.toString());
        gameState.write(s);
        shipToBePlaced = new Ship(shipSize, playerBoard.generateIdentifier(), null, orientation);
        playerBoard.setGridClicked((i, position) -> {
            if (shipToBePlaced == null) {
                return;
            }

            shipToBePlaced = new Ship(shipSize, shipToBePlaced.getIdentifier(), position, orientation);
            if (playerBoard.addShip(shipToBePlaced)) {
                shipToBePlaced = null;
                playerBoard.showAllShips();

                if (setupMenu.getRemainingPoints() <= 0) {
                    // start the game!
                    player.turn();
                }
            } else {
                gameState.write("Failed to put a ship there, try again.");
            }
        });
        setupMenu.setRemainingPoints(setupMenu.getRemainingPoints() - shipSize);
    }

    // EFFECTS: Add a prompt to save the game when the user closes the game
    // MODIFIES: this
    void addCloseSavePrompt() {
        window.addWindowListener(new GuiSavePrompt(e -> {
            try {
                PersistenceManager.saveGame(playerBoard, enemyBoard);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }));
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
