/**
 * Comparator class that checks if the 2 characters are exactly off by one.
 * @author moboa
 */
public class OffByOne implements CharacterComparator {
    /* Returns true if the letters are exactly off by one, false otherwise. */
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == 1;
    }
}
