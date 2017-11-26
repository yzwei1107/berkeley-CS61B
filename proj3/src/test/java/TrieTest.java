import example.Trie;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TrieTest {
    @Test
    public void getWordsContainingPrefix() {
        Trie trie = new Trie();

        trie.add("app");
        trie.add("appearance");
        trie.add("apple");
        trie.add("appliance");
        trie.add("application");
        trie.add("montclair");
        trie.add("mountain");

        List<String> wordsContainingPrefix = trie.getWordsContainingPrefix("foster");
        assertEquals(0, wordsContainingPrefix.size());

        wordsContainingPrefix = trie.getWordsContainingPrefix("mont");
        List<String> expected = Arrays.asList("montclair");
        assertEquals(expected, wordsContainingPrefix);

        wordsContainingPrefix = trie.getWordsContainingPrefix("app");
        expected = Arrays.asList("app", "application", "appliance", "apple", "appearance");
        assertEquals(expected, wordsContainingPrefix);

    }
}
