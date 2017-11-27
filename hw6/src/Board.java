import java.util.*;

/**
 * Boggle board class.
 * @author moboa
 */

public class Board {
    private char[][] board;
    private Trie dictionaryTrie = new Trie();
    private List<String> answers = new ArrayList<>();
    private Set<String> answerSet = new HashSet<>();
    private enum Direction{UP, DOWN, LEFT, RIGHT, UPPER_LEFT, UPPER_RIGHT, LOWER_LEFT, LOWER_RIGHT}
    private final static Map<Direction, int[]> DIRECTION_MAP = new HashMap<>();

    public Board(List<String> boardLines, List<String> dictionaryWords) {
        populateBoard(boardLines);
        populateDictionaryTrie(dictionaryWords);
        setDirectionMap();
        solveBoard();
        answers.sort(new AnswerComparator());
    }

    public List<String> getAnswers() {
        return answers;
    }

    private void populateBoard(List<String> boardLines) {
        int width = boardLines.get(0).length();
        int height = boardLines.size();

        board = new char[height][width];
        for (int i = 0; i < height; i++) {
            String line = boardLines.get(i);
            if (line.length() != width) {
                throw new IllegalArgumentException("The input board is not rectangular.");
            }

            for (int j = 0; j < width; j++) {
                board[i][j] = line.charAt(j);
            }
        }

    }

    private void populateDictionaryTrie(List<String> dictionaryWords) {
        for (String word : dictionaryWords) {
            dictionaryTrie.add(word);
        }
    }

    private void setDirectionMap() {
        DIRECTION_MAP.put(Direction.UP, new int[]{1, 0});
        DIRECTION_MAP.put(Direction.DOWN, new int[]{-1, 0});
        DIRECTION_MAP.put(Direction.LEFT, new int[]{0, -1});
        DIRECTION_MAP.put(Direction.RIGHT, new int[]{0, 1});
        DIRECTION_MAP.put(Direction.UPPER_LEFT, new int[]{1, -1});
        DIRECTION_MAP.put(Direction.UPPER_RIGHT, new int[]{1, 1});
        DIRECTION_MAP.put(Direction.LOWER_LEFT, new int[]{-1, -1});
        DIRECTION_MAP.put(Direction.LOWER_RIGHT, new int[]{-1, 1});
    }

    /* Populates the answer list. */
    private void solveBoard(){
        Stack<Map<String, Object>> stack = new Stack<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Map<String, Object> helperMap = new HashMap<>();
                helperMap.put("coordinates", new Coords(i, j));
                helperMap.put("word", "" + board[i][j]);
                helperMap.put("visited", new HashSet<Coords>());
                stack.push(helperMap);
            }
        }

        while (!stack.isEmpty()) {
            Map<String, Object> helperMap = stack.pop();
            String word = (String) helperMap.get("word");
            Coords coordinates = (Coords) helperMap.get("coordinates");
            HashSet<Coords> visited = new HashSet<>((HashSet<Coords>)helperMap.get("visited"));
            visited.add(coordinates);

            if (word.length() >= 3 && dictionaryTrie.containsWord(word)) {
                addAnswer(word);
            }

            if (dictionaryTrie.containsPrefix(word)) {
                for (Direction direction : Direction.values()) {
                    Coords neighbourCoords = getNeighbourCoordinates(coordinates, direction);
                    if (neighbourCoords == null || visited.contains(neighbourCoords)) {
                        continue;
                    }
                    int neighbourRow = neighbourCoords.row;
                    int neighbourCol = neighbourCoords.column;
                    helperMap = new HashMap<>();
                    helperMap.put("coordinates", neighbourCoords);
                    helperMap.put("word", word + board[neighbourRow][neighbourCol]);
                    helperMap.put("visited", visited);
                    stack.push(helperMap);
                }
            }
        }
    }

    /* Returns the coordinates of the neighbour tile in given direction.
       Returns null if there is no such neighbour.
     */
    private Coords getNeighbourCoordinates(Coords coordinates, Direction direction) {
        Coords neighbourCoords = new Coords(0, 0);
        neighbourCoords.row = coordinates.row + DIRECTION_MAP.get(direction)[0];
        neighbourCoords.column = coordinates.column + DIRECTION_MAP.get(direction)[1];

        if (neighbourCoords.row < 0 || neighbourCoords.row >= board.length
                || neighbourCoords.column < 0 || neighbourCoords.column >= board[0].length) {
            return null;
        }
        return neighbourCoords;
    }

    /* Adds word to answer list. */
    private void addAnswer(String word) {
        if (answerSet.contains(word)) {
            return;
        }
        answers.add(word);
        answerSet.add(word);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                sb.append(board[i][j]);
            }
            sb.append('\n');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private class AnswerComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.length() != o2.length() ? o2.length() - o1.length() : o1.compareTo(o2);
        }
    }
}
