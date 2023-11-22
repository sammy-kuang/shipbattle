package ui;

import model.Orientation;

// Represents an event when the button is clicked on the setup menu
public interface GuiSetupButtonClicked {
    // EFFECTS: Handle the event when the button is clicked on the setup menu
    void onClicked(int newValue, Orientation orientation);
}
