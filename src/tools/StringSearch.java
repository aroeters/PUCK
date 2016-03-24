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
     * The faulty character skip array.
     */
    private int[] shifts;
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
     * @param patternIn The string to searchPattern for
     */
    public StringSearch(final String patternIn) {
        this.R = 256; // set the radix
        this.pattern = patternIn; // set the pattern
        shifts = new int[R]; // create the bad character skip array
        // sets all to -1
        for (int i = 0; i < R; i++) {
            shifts[i] = -1;
        }
        // sets the position of the rightmost occurrence of a character in the pattern
        for (int j = 0; j < patternIn.length(); j++) {
            shifts[patternIn.charAt(j)] = j;
        }
    }

    /**
     * Searches for the pattern in the given string.
     * @param stringIn the string to searchPattern in
     * @return true if the pattern is found in the string else false
     */
    public final boolean searchPattern(final String stringIn) {
        int patLen = pattern.length();
        int inLen = stringIn.length();
        int skip;
        for (int i = 0; i <= inLen - patLen; i += skip) {
            skip = 0;
            for (int j = patLen - 1; j >= 0; j--) {
                if (pattern.charAt(j) != stringIn.charAt(i + j)) {
                    skip = Math.max(1, j - shifts[stringIn.charAt(i + j)]);
                    break;
                }
            }
            if (skip == 0) return true; // found
        }
        return false; // not found
    }
}
