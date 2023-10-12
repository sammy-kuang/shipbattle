package ui;

public class Main {
    public static void main(String[] args) {
        try {
            new ConsoleGame();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
