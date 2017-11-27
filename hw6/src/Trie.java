import java.util.*;

public class Trie {
    private Node root = new Node(' ');

    private class Node {
        private char character;
        private Map<Character, Node> children = new HashMap<>();
        private boolean isEnd = false;
        private Node parent = null;

        private Node(char character) {
            this.character = character;
        }
    }

    /* Adds a word to the trie. */
    public void add(String word) {
        Node pointer = root;

        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            if (!pointer.children.containsKey(currentChar)) {
                pointer.children.put(currentChar, new Node(currentChar));
                pointer.children.get(currentChar).parent = pointer;
            }

            pointer = pointer.children.get(currentChar);
        }

        pointer.isEnd = true;
    }

    /* Returns true if the trie contains word, false otherwise. */
    public boolean containsWord(String word) {
        Node pointer = root;

        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            if (!pointer.children.containsKey(currentChar)) {
                return false;
            }

            pointer = pointer.children.get(currentChar);
        }

        return pointer.isEnd;
    }

    /* Returns true if the trie contains prefix, false otherwise. */
    public boolean containsPrefix(String prefix) {
        Node pointer = root;

        for (int i = 0; i < prefix.length(); i++) {
            char currentChar = prefix.charAt(i);
            if (!pointer.children.containsKey(currentChar)) {
                return false;
            }

            pointer = pointer.children.get(currentChar);
        }

        return true;
    }

}
