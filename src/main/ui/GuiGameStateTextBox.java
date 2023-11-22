package ui;

import javax.swing.*;

// A text box that provides information to the user on the GUI
public class GuiGameStateTextBox {
    private final JTextArea gameState;
    private final JScrollPane scrollPane;

    // EFFECTS: Create a new text box to display info to the user
    public GuiGameStateTextBox() {
        gameState = new JTextArea();
        gameState.setColumns(32);
        gameState.setRows(40);
        gameState.setLineWrap(true);
        gameState.setEditable(false);
        this.scrollPane = new JScrollPane(gameState);
    }

    // EFFECTS: Write information into the text box
    // MODIFIES: this
    public void write(String text) {
        this.gameState.setText(gameState.getText() + "\n" + text);
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }
}
