package ui;

import model.Board;
import model.Position;
import model.Ship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Represents a Board on the GUI
public class GuiBoard extends Board {

    private final ArrayList<Button> gridButtons;
    private final JPanel content;
    private boolean visibility; // should the grid show ships too?

    // EFFECTS: Create a panel with a grid of buttons
    public GuiBoard(int boardSize, String title, boolean visibility) {
        super(boardSize);
        this.content = new JPanel();
        this.gridButtons = new ArrayList<>();
        this.visibility = visibility;
        JPanel grid = new JPanel();

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(title);
        content.add(label);

        GridLayout gridLayout = new GridLayout(getBoardSize(), getBoardSize());
        grid.setLayout(gridLayout);
        grid.setPreferredSize(new Dimension(GuiGame.WINDOW_WIDTH / 2, GuiGame.WINDOW_HEIGHT / 2));

        for (int y = 0; y < getBoardSize() * getBoardSize(); y++) {
            Button b = new Button(Character.toString(Board.EMPTY_SPOT));
            gridButtons.add(b);
            grid.add(b);
        }

        content.add(grid);
    }

    // EFFECTS: Create a GUI board based on another board
    public GuiBoard(Board b, String title, boolean visibility) {
        this(b.getBoardSize(), title, visibility);
        for (Ship s : b.getShips()) {
            addShip(s);
        }
        setBoard(b.getBoard());
    }

    // EFFECTS: Set the event for when a button is clicked on the grid
    //          If onClicked is null, clicking a button will do nothing
    // MODIFIES: this
    public void setGridClicked(GuiGridClicked onClicked) {
        for (Button b : gridButtons) {
            // clear previous action listeners
            for (ActionListener a : b.getActionListeners()) {
                b.removeActionListener(a);
            }

            if (onClicked == null) {
                continue;
            }

            b.addActionListener(e -> {
                int ind = gridButtons.indexOf(b);
                onClicked.onGridClicked(ind, new Position(ind % getBoardSize(), ind / getBoardSize()));
            });
        }
    }

    // EFFECTS: Update the grid to show the current state of the board based on visibility
    // MODIFIES: this
    public void updateGrid() {
        if (visibility) {
            showAllShips();
        } else {
            showHitLocations();
        }
    }

    // EFFECTS: Update the grid to show all the ships
    // MODIFIES: this
    public void showAllShips() {
        for (int y = 0; y < getBoardSize(); y++) {
            for (int x = 0; x < getBoardSize(); x++) {
                int ind = y * getBoardSize() + x;
                gridButtons.get(ind).setLabel(Character.toString(getBoard()[y][x]));
            }
        }
    }

    // EFFECTS: Update the grid to show all shot locations only
    // MODIFIES: this
    public void showHitLocations() {
        for (int y = 0; y < getBoardSize(); y++) {
            for (int x = 0; x < getBoardSize(); x++) {
                int ind = y * getBoardSize() + x;
                char c = getBoard()[y][x];
                if (c == Board.MISS_SPOT || c == Board.HIT_SPOT) {
                    gridButtons.get(ind).setLabel(Character.toString(c));
                } else {
                    gridButtons.get(ind).setLabel(Character.toString(Board.EMPTY_SPOT));
                }
            }
        }
    }

    public JPanel getContent() {
        return content;
    }

    public boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

}
