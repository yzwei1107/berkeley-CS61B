import edu.princeton.cs.algs4.Queue;

/**
 * Class for doing Radix sort
 *
 *
 **/
public class RadixSort
{

    /**
     * Does Radix sort on the passed in array with the following restrictions:
     *  The array can only have ASCII Strings (sequence of 1 byte characters)
     *  The sorting is stable and non-destructive
     *  The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     **/
    public static String[] sort(String[] asciis) {
        int maxStringLength = Integer.MIN_VALUE;

        for (String string : asciis) {
            if (maxStringLength < string.length()) {
                maxStringLength = string.length();
            }
        }

        String[] sortedAsciis = new String[asciis.length];
        System.arraycopy(asciis, 0, sortedAsciis,0, asciis.length);

        for (int i = 0; i < maxStringLength; i++) {
            digitSort(sortedAsciis, i);
        }

        return null;
    }

    /* Performs counting sort using digit at the passed in index */
    private static String[] digitSort(String[] strings, int digitIndex) {
        Queue<String>[] charToStringTable = new Queue[256];

        int[] charAsciis = new int[strings.length];

        for (int i = 0; i < strings.length; i++) {
            int asciiToCompare;
            if (digitIndex >= strings[i].length()) {
                asciiToCompare = 0;
            } else {
                asciiToCompare = (int) strings[i].charAt(strings[i].length() - digitIndex - 1);
            }

            if (charToStringTable[asciiToCompare] == null) {
                charToStringTable[asciiToCompare] = new Queue<>();
            }

            charToStringTable[asciiToCompare].enqueue(strings[i]);
            charAsciis[i] = asciiToCompare;
        }

        charAsciis = CountingSort.betterCountingSort(charAsciis);

        for (int i = 0; i < charAsciis.length; i++) {
            strings[i] = charToStringTable[charAsciis[i]].dequeue();
        }

        return strings;
    }

}
