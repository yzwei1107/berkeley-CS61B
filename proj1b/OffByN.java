/**
 * Comparator class that checks if the 2 characters are exactly off by N letters.
 * @author moboa
 */
public class OffByN implements CharacterComparator {
    private int N;

    public OffByN(int N) {
        this.N = N;
    }

    /* Returns true if the letters are exactly off by N letters, false otherwise. */
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == N;
    }
}
