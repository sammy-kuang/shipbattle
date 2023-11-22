package ui;

import model.Position;
import model.Ship;

import java.util.Random;

// Represents the player playing on the GUI version of the game
public class GuiPlayer extends Controller {

    private final GuiGameStateTextBox out;
    private Ship shipToFireWith = null;
    private int shots = 0;
    private int hits = 0;
    private final Controller enemyController;

    // EFFECTS: Create a player capable of playing the game through the GUI
    public GuiPlayer(GuiBoard players, GuiBoard opponent, Controller enemyController, GuiGameStateTextBox out) {
        this.setBoard(players);
        this.setOpponentBoard(opponent);
        this.enemyController = enemyController;
        this.out = out;
    }

    // EFFECTS: Nothing, as in the GUI we can always see the board
    @Override
    public void viewBoard() {
    }

    // EFFECTS: Nothing, as we can always see the opponent's board
    @Override
    public void peek() {
    }

    // EFFECTS: Nothing, handled in the GUI automatically, not needed here
    @Override
    public void cheatViewEnemy() {
    }

    // EFFECTS: Query the user for their turn
    @Override
    public void turn() {
        fire();
    }

    // EFFECTS: Query the user to select a ship to fire with
    // MODIFIES: this
    @Override
    public void fire() {
        out.write("Captain, it's time to select a ship to fire.");
        enableShipSelection();
    }

    // EFFECTS: Query the user to place down ships
    // MODIFIES: this
    @Override
    public void placeShips(int number) {
        out.write("Let's place our ships down!");
        out.write("At the bottom of this panel, set your desired ship configuration, then press 'Create ship'");
        out.write("Then click any tile on our board when you're ready to place it down");
    }

    // EFFECTS: Enables clicking on the player board to select a ship
    // MODIFIES: this
    private void enableShipSelection() {
        GuiBoard g = (GuiBoard) this.getBoard();
        g.setGridClicked((index, position) -> {
            shipToFireWith = g.positionToShip(position);
            if (shipToFireWith != null) {
                out.write(String.format("We have selected ship '%c' to fire with!", shipToFireWith.getIdentifier()));
                enableFiringOnEnemy();
                g.setGridClicked(null); // Disable further ship selection
            }
        });
    }

    // EFFECTS: Allow the user to shoot on the enemy's board
    // MODIFIES: this
    // REQUIRES: shipToFireWith is not null
    private void enableFiringOnEnemy() {
        // Decide whether scattershot or multi-fire
        GuiGridClicked event;
        if (shipToFireWith.getLength() <= Ship.SCATTER_SHOT_LENGTH) {
            event = enableScatterShot();
        } else {
            event = enableMultipleShot();
        }
        hits = 0;
        GuiBoard opp = (GuiBoard) getOpponentBoard();
        opp.setGridClicked(event);
    }

    // EFFECTS: Allow the user to fire on the enemy's board with scattershot
    //          Returns the appropriate event when the enemy's board is clicked
    // MODIFIES: this
    // REQUIRES: shipToFireWith is not null
    private GuiGridClicked enableScatterShot() {
        GuiBoard opp = (GuiBoard) getOpponentBoard();
        return (index, position) -> {
            out.write(String.format("Firing on %s!", position.toString()));
            Position scatter = opp.generateRandomPosition(position, new Random());
            hits += opp.fireOnPosition(position) ? 1 : 0;
            hits += opp.fireOnPosition(scatter) ? 1 : 0;
            out.write(String.format("Shell scattered, landing on %s.", scatter.toString()));
            opp.updateGrid();
            onTurnEnd();
        };
    }

    // EFFECTS: Allow the user to fire on the enemy's board with multiple shots
    //          Returns the appropriate event when the enemy's board is clicked
    // MODIFIES: this
    // REQUIRES: shipToFireWith is not null
    private GuiGridClicked enableMultipleShot() {
        shots = shipToFireWith.getLength() / 2;
        GuiBoard opp = (GuiBoard) getOpponentBoard();
        return (index, position) -> {
            out.write(String.format("Firing on %s!", position.toString()));
            boolean hit = opp.fireOnPosition(position);
            hits += hit ? 1 : 0;
            shots--;
            out.write(String.format("%d shots remaining.", shots));
            opp.updateGrid();
            if (shots <= 0) {
                onTurnEnd();
            }
        };
    }

    // EFFECTS: Call the opponent's turn after ours is finished
    // MODIFIES: this
    private void onTurnEnd() {
        GuiBoard opp = (GuiBoard) getOpponentBoard();
        GuiBoard us = (GuiBoard) getBoard();
        opp.updateGrid();
        out.write(String.format("We hit the enemy a total of %d times.\n\n", hits));
        hits = 0;

        if (!opp.isAlive()) {
            endGame();
            return;
        }

        opp.setGridClicked(null);
        enemyController.turn();
        out.write("\n\n");
        us.updateGrid();
        if (!getBoard().isAlive()) {
            endGame();
        } else {
            turn();
        }
    }

    // EFFECTS: Ends the game
    // MODIFIES: this
    private void endGame() {
        out.write("Game over!");
        if (!getOpponentBoard().isAlive()) {
            out.write("Congratulations, you won!");
        } else if (!getBoard().isAlive()) {
            out.write("We lost!");
        }

        GuiBoard opp = (GuiBoard) getOpponentBoard();
        GuiBoard us = (GuiBoard) getBoard();

        us.setGridClicked(null);
        opp.setGridClicked(null);
    }
}
