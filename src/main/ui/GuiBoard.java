package ui;

import model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GuiBoard extends JPanel {

    private Board board;
    private ArrayList<Button> gridButtons;
    private JPanel grid;

    // EFFECTS: Create a panel with a grid of buttons
    public GuiBoard(Board board, String title) {
        this.board = board;
        this.gridButtons = new ArrayList<>();
        this.grid = new JPanel();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(title);
        add(label);

        GridLayout gridLayout = new GridLayout(board.getBoardSize(), board.getBoardSize());
        grid.setLayout(gridLayout);
        grid.setPreferredSize(new Dimension(400, 400));

        for (int y = 0; y < board.getBoardSize() * board.getBoardSize(); y++) {
            Button b = new Button(Character.toString(Board.EMPTY_SPOT));
            gridButtons.add(b);
            grid.add(b);
        }

        add(grid);
    }

    public Board getBoard() {
        return board;
    }
}
