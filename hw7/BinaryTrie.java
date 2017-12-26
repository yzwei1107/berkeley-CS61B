import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class BinaryTrie implements Serializable {
    private Node root;
    private Map<Character, Node> charNodeMap;

    private class Node implements Comparable<Node>, Serializable {
        private Node leftChild = null; // 0 bit
        private Node rightChild = null; // 1 bit
        private char character;
        private int frequency;

        private Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        @Override
        public int hashCode() {
            return (int) character + 31 * frequency;
        }

        @Override
        public int compareTo(Node oNode) {
            return this.frequency - oNode.frequency;
        }
    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        int uniqueCharCount = frequencyTable.size();
        MinPQ<Node> nodePQ = new MinPQ<>(uniqueCharCount);
        charNodeMap = new HashMap<>(uniqueCharCount);

        for (char character : frequencyTable.keySet()) {
            Node node = new Node(character, frequencyTable.get(character));
            charNodeMap.put(character, node);
            nodePQ.insert(node);
        }

        while (nodePQ.size() > 1) {
            Node firstNode = nodePQ.delMin();
            Node secondNode = nodePQ.delMin();

            int newNodeFrequency = firstNode.frequency + secondNode.frequency;
            Node newNode = new Node(Character.MIN_VALUE, newNodeFrequency);
            newNode.leftChild = firstNode;
            newNode.rightChild = secondNode;

            nodePQ.insert(newNode);
        }
        root = nodePQ.delMin();
    }

    /* Returns the character and the bit sequence (decoding trie) of the longest prefix match. */
    public Match longestPrefixMatch(BitSequence querySequence) {
        StringBuilder currentBitSeq = new StringBuilder();

        Node pointer = root;
        for (int i = 0; i < querySequence.length(); i++) {
            if (querySequence.bitAt(i) == 0 && pointer.leftChild != null) {
                currentBitSeq.append('0');
                pointer = pointer.leftChild;
            } else if (querySequence.bitAt(i) == 1 && pointer.rightChild != null) {
                currentBitSeq.append('1');
                pointer = pointer.rightChild;
            } else {
                break;
            }

        }

        return new Match(new BitSequence(currentBitSeq.toString()), pointer.character);
    }

    /* Returns a table mapping a character to its bit sequence in the decoding trie. */
    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> lookupTable = new HashMap<>();
        Map<Node, String> nodeBitPrefixTable = new HashMap<>();
        nodeBitPrefixTable.put(root, "");

        Stack<Node> nodeStack = new Stack<>();
        nodeStack.push(root);

        while (!nodeStack.isEmpty()) {
            Node pointer = nodeStack.pop();
            String currentBitSeq = nodeBitPrefixTable.get(pointer);

            if (pointer.rightChild != null) {
                nodeStack.push(pointer.rightChild);
                nodeBitPrefixTable.put(pointer.rightChild, currentBitSeq + '1');
            }

            if (pointer.leftChild != null) {
                nodeStack.push(pointer.leftChild);
                nodeBitPrefixTable.put(pointer.leftChild, currentBitSeq + '0');

                nodeBitPrefixTable.remove(pointer);
            }

        }

        for (char character : charNodeMap.keySet()) {
            Node charNode = charNodeMap.get(character);
            String bitSeqString = nodeBitPrefixTable.get(charNode);
            lookupTable.put(character, new BitSequence(bitSeqString));
        }

        return lookupTable;
    }

}
