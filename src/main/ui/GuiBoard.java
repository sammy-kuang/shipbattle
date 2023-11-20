package ui;

import model.Board;
import model.Position;
import model.Ship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GuiBoard extends Board {

    private ArrayList<Button> gridButtons;

    private JPanel content;
    private JPanel grid;

    // EFFECTS: Create a panel with a grid of buttons
    public GuiBoard(int boardSize, String title) {
        super(boardSize);
        this.content = new JPanel();
        this.gridButtons = new ArrayList<>();
        this.grid = new JPanel();

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(title);
        content.add(label);

        GridLayout gridLayout = new GridLayout(getBoardSize(), getBoardSize());
        grid.setLayout(gridLayout);
        grid.setPreferredSize(new Dimension(400, 400));

        for (int y = 0; y < getBoardSize() * getBoardSize(); y++) {
            Button b = new Button(Character.toString(Board.EMPTY_SPOT));
            gridButtons.add(b);
            grid.add(b);
        }

        content.add(grid);
    }

    // EFFECTS: Set the event for when a button is clicked on the grid
    // MODIFIES: this
    public void setGridClicked(GuiGridClicked onClicked) {
        for (Button b : gridButtons) {
            // clear previous action listeners
            for (ActionListener a : b.getActionListeners()) {
                b.removeActionListener(a);
            }

            b.addActionListener(e -> {
                int ind = gridButtons.indexOf(b);
                onClicked.onGridClicked(ind, new Position(ind % getBoardSize(), ind / getBoardSize()));
            });
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

    public void setContent(JPanel content) {
        this.content = content;
    }

    public JPanel getGrid() {
        return grid;
    }

    public void setGrid(JPanel grid) {
        this.grid = grid;
    }

}
