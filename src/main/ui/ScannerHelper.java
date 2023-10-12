package ui;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ScannerHelper {
    static Scanner in = new Scanner(System.in);

    // EFFECTS: return a value from the input scanner low <= value <= high
    public static int clampedQuery(String message, int low, int high) {
        try {
            System.out.print(message);
            int r = in.nextInt();
            in.nextLine();

            if (r > high || r < low) {
                System.out.println("That's not quite right, try again.");
                return clampedQuery(message, low, high);
            }

            return r;
        } catch (InputMismatchException e) {
            in.nextLine(); // clear
            System.out.println("That's not quite right, try again.");
            return clampedQuery(message, low, high);
        }
    }

}
