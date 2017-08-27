package hw2;

/**
 * A percolation system modeled using an N-by-N grid of sites.
 * @author moboa
 */
public class Percolation {
    private boolean[][] sites;
    private WeightedQuickUnionUF unionFind;
    private WeightedQuickUnionUF unionFindNoBottom; // Prevents backwash
    private int numberOfOpenSites;
    private int N;
    private int VIRTUAL_TOP_SITE_INDEX;
    private int VIRTUAL_BOTTOM_SITE_INDEX;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }

        this.N = N;
        this.VIRTUAL_TOP_SITE_INDEX = N * N;
        this.VIRTUAL_BOTTOM_SITE_INDEX = N * N + 1;
        this.sites = new boolean[N][N];
        this.unionFind = new WeightedQuickUnionUF(N * N + 2);
        this.unionFindNoBottom = new WeightedQuickUnionUF(N * N + 1);
        this.numberOfOpenSites = 0;
    }

    /* Open the site at (row, col) */
    public void open(int row, int col) {
        if (row < 0 || col < 0 || row >= N || col >= N) {
            throw new IndexOutOfBoundsException();
        }

        if (isOpen(row, col)) {
            return;
        }

        sites[row][col] = true;
        numberOfOpenSites++;
        connectToOpenNeighbours(row, col);

        /*
         * If the site is in the top row, connect to the virtual top site.
         * Else if the site in the bottom row, connect it to the bottom virtual site.
         */
        if (row == 0) {
            unionFind.union(VIRTUAL_TOP_SITE_INDEX, xyTo1DIndex(row, col));
            unionFindNoBottom.union(VIRTUAL_TOP_SITE_INDEX, xyTo1DIndex(row, col));
        }

        if (row == N - 1) {
            unionFind.union(VIRTUAL_BOTTOM_SITE_INDEX, xyTo1DIndex(row, col));
        }
    }

    /* Connects site to neighbouring sites if they are open */
    private void connectToOpenNeighbours(int row, int col) {
        int currentSite = xyTo1DIndex(row, col);
        if (row - 1 >= 0 && isOpen(row - 1, col)) {
            int topNeighbour = xyTo1DIndex(row - 1, col);
            unionFind.union(currentSite, topNeighbour);
            unionFindNoBottom.union(currentSite, topNeighbour);
        }

        if (row + 1 < N && isOpen(row + 1, col)) {
            int bottomNeighbour = xyTo1DIndex(row + 1, col);
            unionFind.union(currentSite, bottomNeighbour);
            unionFindNoBottom.union(currentSite, bottomNeighbour);
        }

        if (col - 1 >= 0 && isOpen(row, col - 1)) {
            int leftNeightbour = xyTo1DIndex(row, col - 1);
            unionFind.union(currentSite, leftNeightbour);
            unionFindNoBottom.union(currentSite, leftNeightbour);
        }

        if (col + 1 < N && isOpen(row, col + 1)) {
            int rightNeighbour = xyTo1DIndex(row, col + 1);
            unionFind.union(currentSite, rightNeighbour);
            unionFindNoBottom.union(currentSite, rightNeighbour);
        }
    }

    /* Converts row and col to equivalent 1-dimensional index */
    private int xyTo1DIndex(int row, int column) {
        return row * N + column;
    }

    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0 || row >= N || col >= N) {
            throw new IndexOutOfBoundsException();
        }
        return sites[row][col];
    }

    public boolean isFull(int row, int col) {
        if (row < 0 || col < 0 || row >= N || col >= N) {
            throw new IndexOutOfBoundsException();
        }

        int index = xyTo1DIndex(row, col);
        return unionFind.connected(VIRTUAL_TOP_SITE_INDEX, index)
                && unionFindNoBottom.connected(VIRTUAL_TOP_SITE_INDEX, index);
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        return unionFind.connected(VIRTUAL_TOP_SITE_INDEX, VIRTUAL_BOTTOM_SITE_INDEX);
    }
}
