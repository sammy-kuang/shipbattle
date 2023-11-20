package ui;

import javax.swing.*;
import java.awt.*;

public class GuiGameStateTextBox {
    private final JTextArea gameState;
    private final JScrollPane scrollPane;

    public GuiGameStateTextBox() {
        gameState = new JTextArea();
        gameState.setColumns(36);
        gameState.setRows(40);
        gameState.setLineWrap(true);
        gameState.setEditable(false);
        this.scrollPane = new JScrollPane(gameState);
    }

    public void write(String text) {
        this.gameState.setText(gameState.getText() + "\n" + text);
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }
}
