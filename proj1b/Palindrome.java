/**
 * Class for palindrome
 *
 * @author moboa
 */
public class Palindrome {

    /* Converts a string into an ordered deque of its characters and returns the deque. */
    public static Deque<Character> wordToDeque(String word) {
        Deque<Character> wordDeque = new ArrayDequeSolution<>();

        for (int i = 0; i < word.length(); i++) {
            wordDeque.addLast(word.charAt(i));
        }

        return wordDeque;
    }

    /* Returns true if the word is a palindrome, false otherwise. */
    public static boolean isPalindrome(String word) {
        return palindromeCharacterChecker(word, 0);
    }

    /* Helper method for isPalindrome() */
    private static boolean palindromeCharacterChecker(String word, int charIndex) {
        int charIndexInReversedWord = word.length() - 1 - charIndex;

        if (word.charAt(charIndex) != word.charAt(charIndexInReversedWord)) {
            return false;
        }

        if (charIndex >= word.length() - 1) {
            return  true;
        }

        return palindromeCharacterChecker(word, charIndex + 1);
    }


    /* Returns true if the word is an OffByN palindrome, false otherwise. */
    public static boolean isPalindrome(String word, CharacterComparator cc) {
        return palindromeCharacterChecker(word, 0, cc);
    }

    /* Helper method for the OffByN version of isPalindrome() */
    private static boolean palindromeCharacterChecker(String word, int i, CharacterComparator cc) {
        int indexInReversedWord = word.length() - 1 - i;

        /* We don't check the middle character to allow odd-length palindromes */
        boolean isWordOfOddLength = word.length() % 2  == 1;
        boolean isMiddleChar = i == Math.floor(word.length() / 2);

        if (isWordOfOddLength && isMiddleChar) {
            /* The style checker doesn't allow me to use empty statements */
            assert true;
        } else if (!cc.equalChars(word.charAt(i), word.charAt(indexInReversedWord))) {
            return false;
        }

        if (i >= word.length() - 1) {
            return  true;
        }

        return palindromeCharacterChecker(word, i + 1, cc);
    }

}
