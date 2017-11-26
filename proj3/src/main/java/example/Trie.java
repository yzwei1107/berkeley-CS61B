package example;

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

    /* Returns all the words that contain the prefix. */
    public List<String> getWordsContainingPrefix(String prefix) {
        LinkedList<String> words = new LinkedList<>();

        Node pointer = root;
        for (int i = 0; i < prefix.length(); i++) {
            char currentChar = prefix.charAt(i);
            if (!pointer.children.containsKey(currentChar)) {
                return words;
            }

            pointer = pointer.children.get(currentChar);
        }

        Node prefixEndNode = pointer;

        List<Node> endNodes = getAllEndNodesContainingPrefix(prefixEndNode);

        for (Node node : endNodes) {
            StringBuilder sb = new StringBuilder();
            pointer = node;

            // The nodes are compared by address.
            while (pointer != prefixEndNode) {
                sb.append(pointer.character);
                pointer = pointer.parent;
            }

            words.add(prefix + sb.reverse().toString());
        }

        return words;
    }

    /* Gets all the end nodes of the words containing the prefix. */
    private List<Node> getAllEndNodesContainingPrefix(Node prefixEndNode) {
        LinkedList<Node> endNodes = new LinkedList<>();

        Stack<Node> nodeStack = new Stack<>();
        nodeStack.add(prefixEndNode);

        while (!nodeStack.isEmpty()) {
            Node node = nodeStack.pop();
            if (node.isEnd) {
                endNodes.add(node);
            }

            for (char character : node.children.keySet()) {
                nodeStack.add(node.children.get(character));
            }

        }

        return endNodes;
    }
}
