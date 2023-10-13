package model;

// A set of static utilities that we can use, not necessarily
// belonging to a single concept
public final class Utils {

    // EFFECTS: No instantiating
    private Utils() {

    }

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
