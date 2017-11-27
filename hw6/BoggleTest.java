import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BoggleTest {
    @Test
    public void populateBoardTest() {
        List<String> boardLines = null;
        List<String> dictionaryWords = null;

        try {
            boardLines = Files.readAllLines(Paths.get("testBoggle"));
            dictionaryWords = Files.readAllLines(Paths.get("words"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Board board = new Board(boardLines, dictionaryWords);

        String expected = "ness\n" +
                "tack\n" +
                "bmuh\n" +
                "esft";
        assertEquals(expected, board.toString());

        try {
            boardLines = Files.readAllLines(Paths.get("testBoggle2"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        board = new Board(boardLines, dictionaryWords);

        expected = "baa\n" +
                "aba\n" +
                "aab\n" +
                "baa";
        assertEquals(expected, board.toString());
    }

    @Test
    public void solveBoard() {
        List<String> boardLines = null;
        List<String> dictionaryWords = null;

        try {
            boardLines = Files.readAllLines(Paths.get("testBoggle"));
            dictionaryWords = Files.readAllLines(Paths.get("words"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Board board = new Board(boardLines, dictionaryWords);
        String expected = "thumbtacks\n" +
                "thumbtack\n" +
                "setbacks\n" +
                "setback\n" +
                "ascent\n" +
                "humane\n" +
                "smacks";
        assertEquals(expected, Boggle.answerString(board.getAnswers(), 7));


        try {
            boardLines = Files.readAllLines(Paths.get("testBoggle2"));
            dictionaryWords = Files.readAllLines(Paths.get("testDict"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        board = new Board(boardLines, dictionaryWords);
        expected = "aaaaa\n" +
                "aaaa";
        assertEquals(expected, Boggle.answerString(board.getAnswers(), 20));
    }
}
