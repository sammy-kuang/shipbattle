package ui;

import model.Ship;

import javax.swing.*;

public class GuiPlayer extends Controller {

    private GuiGameStateTextBox out;
    private Ship shipToFireWith = null;

    // EFFECTS: Create a player capable of playing the game through the GUI
    public GuiPlayer(GuiBoard players, GuiBoard opponent, GuiGameStateTextBox out) {
        this.setBoard(players);
        this.setOpponentBoard(opponent);
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

    }

    // EFFECTS: Query the user to place down ships
    // MODIFIES: this
    @Override
    public void placeShips(int number) {
        out.write("Let's place our ships down!");
        out.write("At the top of this panel, set your desired ship configuration, then press 'Create ship'");
        out.write("Then click any tile on our board when you're ready to place it down");
    }

}
