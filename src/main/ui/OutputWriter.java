package ui;

// Used to output text to the user, can either be a console terminal our a text box
public interface OutputWriter {
    // EFFECTS: Write text into an output system
    void write(String text);
}
