package ui;

import java.util.InputMismatchException;
import java.util.Scanner;

// A class that allows us to manipulate terminal input
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

    // EFFECTS: Query the user for a Yes or No response
    public static boolean yesNoQuery(String message) {
        System.out.print(message + " (Y/N): ");
        String o = in.nextLine();
        if (o.length() < 1) {
            System.out.println("That's not quite right, try again.");
            return yesNoQuery(message);
        }
        return o.toLowerCase().charAt(0) == 'y';
    }
}
