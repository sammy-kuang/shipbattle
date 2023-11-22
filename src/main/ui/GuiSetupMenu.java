package ui;

import model.Orientation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a GUI menu that allows the user to setup their board
public class GuiSetupMenu extends JPanel {
    private final JLabel pointsLabel;
    private final JSpinner spinner;
    private final JComboBox<Orientation> orientationBox;
    private int remainingPoints;
    private int usePoints = 2;
    private GuiSetupButtonClicked onClick;

    // EFFECTS: Create a menu that represents the board setup process
    public GuiSetupMenu(int points, GuiSetupButtonClicked onClick) {
        this.remainingPoints = points;
        this.onClick = onClick;
        this.pointsLabel = new JLabel(generateLabelText());
        this.orientationBox = new JComboBox<>(Orientation.values());
        JButton button = new JButton("Create ship");
        this.spinner = new JSpinner();

        button.addActionListener(e -> {
            if (remainingPoints > 0) {
                getOnClick().onClicked(getUsePoints(), (Orientation)orientationBox.getSelectedItem());
            }
            update();
        });

        updateSpinner();

        add(pointsLabel);
        add(spinner);
        add(orientationBox);
        add(button);
    }

    // EFFECTS: Update all UI elements related to the setup menu
    // MODIFIES: this
    public void update() {
        updateSpinner();
        pointsLabel.setText(generateLabelText());
    }

    // EFFECTS: Update the spinner to allow the user to select the number of points for a ship
    // REQUIRES: remainingPoints is even and greater than 0
    // MODIFIES: this
    public void updateSpinner() {
        try {
            spinner.setModel(new SpinnerNumberModel(2, 2, remainingPoints, 2));
            spinner.addChangeListener(e -> setUsePoints((int) spinner.getValue()));
            setUsePoints(2);
        } catch (IllegalArgumentException e) {
            spinner.setModel(new SpinnerNumberModel(0,0,0,0));
            for (ChangeListener c : spinner.getChangeListeners()) {
                spinner.removeChangeListener(c);
            }
        }
    }

    // EFFECTS: Return a formatted String to go into the setup label
    private String generateLabelText() {
        return String.format("You have %2d points remaining.", this.remainingPoints);
    }

    public int getRemainingPoints() {
        return this.remainingPoints;
    }

    public void setRemainingPoints(int i) {
        this.remainingPoints = i;
    }

    private int getUsePoints() {
        return this.usePoints;
    }

    private void setUsePoints(int usePoints) {
        this.usePoints = usePoints;
    }

    private GuiSetupButtonClicked getOnClick() {
        return this.onClick;
    }

    public void setOnClick(GuiSetupButtonClicked g) {
        this.onClick = g;
    }
}
