public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader objReader = new ObjectReader(args[0]);
        BinaryTrie decodingTrie = (BinaryTrie) objReader.readObject();
        Integer fileSymbolCount = (Integer) objReader.readObject();
        BitSequence fileBitSequence = (BitSequence) objReader.readObject();

        char[] fileSymbols = new char[fileSymbolCount];

        for (int i = 0; i < fileSymbolCount; i++) {
            Match match = decodingTrie.longestPrefixMatch(fileBitSequence);
            fileSymbols[i] = match.getSymbol();
            fileBitSequence = fileBitSequence.allButFirstNBits(match.getSequence().length());
        }

        FileUtils.writeCharArray(args[1], fileSymbols);
    }
}
