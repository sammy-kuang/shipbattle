package ui;

import model.Position;

// Represents an event on when a grid tile is clicked
public interface GuiGridClicked {
    // EFFECTS: Event callback for when a grid is clicked
    void onGridClicked(int index, Position position);
}
