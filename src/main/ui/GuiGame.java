package ui;

import model.Board;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class GuiGame {

    GuiBoard player;
    GuiBoard enemy;

    private int boardSize = 10;
    private int numShips = 10;

    public GuiGame() {
        // tmp
        player = new GuiBoard(new Board(10), "Player");
        enemy = new GuiBoard(new Board(10), "Enemy");

        JFrame f = createSetupMenu();
        f.setVisible(true);
    }

    // EFFECTS: Create a window that displays the game
    private void createMainGame() {
        JFrame window = new JFrame();
        JPanel content = new JPanel();
        JPanel grids = new JPanel();

        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        grids.setLayout(new BoxLayout(grids, BoxLayout.Y_AXIS));

        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(900, 850));

        grids.add(enemy);
        grids.add(player);

        content.add(grids);
        window.add(content);

        window.pack();
        window.setVisible(true);
    }

    private void setupGame() {
        player = new GuiBoard(new Board(getBoardSize()), "Player");
        enemy = new GuiBoard(new Board(getBoardSize()), "Enemy");
        createMainGame();
    }

    private JFrame createSetupMenu() {
        JFrame window = new JFrame();
        JPanel content = new JPanel();
        SpinnerNumberModel boardSizeModel = new SpinnerNumberModel(10, 10, 25, 1);
        SpinnerNumberModel numShipsModel = new SpinnerNumberModel(10, 10, 16, 2);

        JSpinner boardSize = new JSpinner(boardSizeModel);
        JSpinner numShips = new JSpinner(numShipsModel);

        boardSizeModel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setBoardSize((int)boardSizeModel.getValue());
            }
        });

        numShips.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setNumShips((int)numShipsModel.getValue());
            }
        });

        content.add(new JLabel("Board size: "));
        content.add(boardSize);
        content.add(new JLabel("Number of ships: "));
        content.add(numShips);

        Button done = new Button("Start");
        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.setVisible(false);
                System.out.println(getBoardSize());
                System.out.println(getNumShips());
                window.dispose();
                setupGame();
            }
        });
        content.add(done);

        window.add(content);
        window.pack();

        return window;
    }

    // EFFECTS: Create a menu that displays the current state of the game
    public JPanel createGameMenu() {
        JPanel panel = new JPanel();

        return panel;
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
