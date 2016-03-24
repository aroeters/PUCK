/*
 *Author: Arne Roeters
 */
package tools;

/**
 *
 * @author arne
 */
public class StringSearch {

    /**
     * The bad character skip array.
     */
    private int[] right;
    /**
     * The radix.
     */
    private final int R;
    /**
     * The pattern in String.
     */
    private final String pattern;

    /**
     * Constructor of the class, sets all used and preprocesses the pattern
     * string.
     *
     * @param pat The string to search for
     */
    public StringSearch(final String pat) {
        this.R = 256; // set the radix
        this.pattern = pat; // set the pattern
        right = new int[R]; // create the bad character skip array
        // sets all to -1
        for (int c = 0; c < R; c++) {
            right[c] = -1;
        }
        // sets the position of the rightmost occurrence of a character in the pattern
        for (int j = 0; j < pat.length(); j++) {
            right[pat.charAt(j)] = j;
        }
    }

    /**
     * Searches for the pattern in the given string.
     * @param stringIn the string to search in
     * @return true if the pattern is found in the string else false
     */
    public final boolean search(final String stringIn) {
        int patLen = pattern.length();
        int inLen = stringIn.length();
        int skip;
        for (int i = 0; i <= inLen - patLen; i += skip) {
            skip = 0;
            for (int j = patLen - 1; j >= 0; j--) {
                if (pattern.charAt(j) != stringIn.charAt(i + j)) {
                    skip = Math.max(1, j - right[stringIn.charAt(i + j)]);
                    break;
                }
            }
            if (skip == 0) return true; // found
        }
        return false; // not found
    }
}
