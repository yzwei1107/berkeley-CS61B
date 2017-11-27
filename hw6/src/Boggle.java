import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Boggle {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No flags set");
            return;
        }

        int numberOfAnswers = 1;
        String dictionaryPath = "words";

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-k":
                    if (!args[++i].matches("\\d+(\\.\\d+)?")) {
                        System.out.println("-k was not set properly");
                        return;
                    }
                    numberOfAnswers = Integer.parseInt(args[i]);

                    if (numberOfAnswers < 1) {
                        System.out.println("Number of words must be a positive number.");
                        return;
                    }
                    break;

                case "-d":
                    dictionaryPath = args[++i];
                    break;
            }
        }

        List<String> dictionaryWords;
        try {
            dictionaryWords = Files.readAllLines(Paths.get(dictionaryPath));
        } catch (IOException e) {
            throw new IllegalArgumentException("The dictionary file does not exist.");
        }

        Scanner scanner = new Scanner(System.in);
        List<String> boardLines = new ArrayList<>();

        while (scanner.hasNextLine()) {
            boardLines.add(scanner.nextLine());
        }

        Board board = new Board(boardLines, dictionaryWords);
        List<String> answers = board.getAnswers();

        System.out.println(answerString(answers, numberOfAnswers));
    }

    /* Returns a maximum of the asked number of answers from the list. */
    public static String answerString(List<String> answers, int numberOfAnswers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < answers.size() && i < numberOfAnswers; i++) {
            sb.append(answers.get(i)).append('\n');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

}
