package model;

public class Utils {
    /*
    EFFECTS:
        - If value is less than low, return low
        - If value is greater than high, return high
        - Else return value
    REQUIRES:
        - low < high
     */
    public static int clamp(int value, int low, int high) {
        if (value < low) {
            return low;
        } else if (value > high) {
            return high;
        } else {
            return value;
        }
    }
}
