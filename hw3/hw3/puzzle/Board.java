package hw3.puzzle;

/**
 * Board implemented using an N-by-N array of tiles where tiles[i][j] = tile at row i, column j
 * @author moboa
 */


import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class Board implements WorldState {
    private int[][] tiles;
    private int[][] goal;
    private int N;

    public Board(int[][] tiles) {
        this.N = tiles.length;
        this.tiles = new int[N][N];
        this.goal = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = tiles[i][j];
                this.goal[i][j] = N * i + j;
            }
        }

        this.goal[N - 1][N - 1] = 0;
    }

    /* Returns value of tile at row i, column j (or 0 if blank). */
    public int tileAt(int i, int j) {
        if (i < 0 || i >= N || j < 0 || j >= N) {
            throw new IndexOutOfBoundsException("Invalid row and/or column");
        }
        return tiles[i][j];
    }

    /* Returns the board size N. */
    public int size() {
        return N;
    }

    /* Returns the number of tiles in the wrong position. */
    public int hamming() {
        int hammingEstimate = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != goal[i][j]) {
                    hammingEstimate++;
                }
            }
        }
        return hammingEstimate;
    }

    /**
     *  Returns the sum of the Manhattan distances (sum of the vertical and horizontal distance)
     *  from the tiles to their goal positions.
     **/
    public int manhattan() {
        int manhattanEstimate = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int currentTile = tiles[i][j];
                int row = currentTile / N;
                int column = currentTile % N;

                manhattanEstimate += Math.abs(row - i);
                manhattanEstimate += Math.abs(column - j);
            }
        }
        return manhattanEstimate;
    }


    public int hashCode() {
        return tiles == null ? 0 : tiles.hashCode();
    }

    /* Returns true if is this board the goal board. */
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }

        if (y == null || y.getClass() != getClass()) {
            return false;
        }
        Board otherBoard = (Board) y;

        if (Arrays.deepEquals(tiles, otherBoard.tiles)) {
            return true;
        }

        return false;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return hamming();
    }

    /* Authot of this function : Josh Hug */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }

    @Override
    public boolean isGoal() {
        return hamming() == 0;
    }

    /* Returns the string representation of the board. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}
