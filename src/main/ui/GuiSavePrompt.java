package ui;

import model.EventLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

// A prompt that opens when the user closes the game, which queries the user on whether they would like to save or not
public class GuiSavePrompt extends JFrame implements WindowListener {
    // EFFECTS: Create a save prompt
    public GuiSavePrompt(ActionListener callback) {
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        JLabel label = new JLabel("Would you like to save?");
        JButton yes = new JButton("Yes");
        ActionListener close = e -> {
            setVisible(false);
            dispose();
        };

        yes.addActionListener(callback);
        yes.addActionListener(close);
        JButton no = new JButton("No");
        no.addActionListener(close);
        add(label);
        add(yes);
        add(no);
        pack();
    }

    // EFFECTS: Nothing
    @Override
    public void windowOpened(WindowEvent e) {

    }

    // EFFECTS: Show the prompt
    @Override
    public void windowClosing(WindowEvent e) {
        this.setVisible(true);

        // Print out events
        for (model.Event ev : EventLog.getInstance()) {
            System.out.printf("%s: %s\n", ev.getDate(), ev.getDescription());
        }
    }

    // EFFECTS: Nothing
    @Override
    public void windowClosed(WindowEvent e) {
    }

    // EFFECTS: Nothing
    @Override
    public void windowIconified(WindowEvent e) {

    }

    // EFFECTS: Nothing
    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    // EFFECTS: Nothing
    @Override
    public void windowActivated(WindowEvent e) {

    }

    // EFFECTS: Nothing
    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
