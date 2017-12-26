import org.junit.Test;
import static org.junit.Assert.*;

public class TestDecoding {
    @Test
    public void encodeAndDecode() {
        String testFilename = "watermelonsugar.txt";
        String decodedFilename = "decoded.txt";

        HuffmanEncoder.main(new String[]{testFilename});
        HuffmanDecoder.main(new String[]{testFilename + ".huf", decodedFilename});

        char[] testFileSymbols = FileUtils.readFile(testFilename);
        char[] decodedFileSymbols = FileUtils.readFile(decodedFilename);

        assertArrayEquals(testFileSymbols, decodedFileSymbols);
    }

}
