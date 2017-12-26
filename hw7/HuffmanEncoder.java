import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {

    /* Returns a table of a char in the file matched to its number of occurrences. */
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequencyTable = new HashMap<>();

        for (char symbol : inputSymbols) {
            if (!frequencyTable.containsKey(symbol)) {
                frequencyTable.put(symbol, 0);
            }

            frequencyTable.put(symbol, frequencyTable.get(symbol) + 1);
        }

        return frequencyTable;
    }

    public static void main(String[] args) {
        char[] inputSymbols = FileUtils.readFile(args[0]);
        Map<Character, Integer> frequencyTable = buildFrequencyTable(inputSymbols);

        ObjectWriter objWriter = new ObjectWriter(args[0] + ".huf");
        BinaryTrie decodingTrie = new BinaryTrie(frequencyTable);
        objWriter.writeObject(decodingTrie);

        Integer fileSymbolCount = inputSymbols.length;
        objWriter.writeObject(fileSymbolCount);

        Map<Character, BitSequence> lookupTable = decodingTrie.buildLookupTable();

        List<BitSequence> fileCharsBitSequences = new ArrayList<>();

        for (char symbol : inputSymbols) {
            fileCharsBitSequences.add(lookupTable.get(symbol));
        }

        BitSequence fileFinalBitSequence = BitSequence.assemble(fileCharsBitSequences);
        objWriter.writeObject(fileFinalBitSequence);
    }
}
