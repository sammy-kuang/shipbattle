package ui;

import model.Orientation;

// Represents an event when the button is clicked on the setup menu
public interface GuiSetupButtonClicked {
    public void onClicked(int newValue, Orientation orientation);
}
