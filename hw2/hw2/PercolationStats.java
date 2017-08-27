package hw2;

/**
 *  Perform T independent experiments on an N-by-N grid (Monte Carlo simulation).
 *  @author moboa
 */

public class PercolationStats {
    private double mean;
    private double stddev;
    private double confidenceLow;
    private double confidenceHigh;

    public PercolationStats(int N, int T) {
       if (N <= 0 || T <= 0) {
           throw new IllegalArgumentException();
       }

       int[] sampleValues = new int[T];
       for (int i = 0; i < T; i++) {
           Percolation percolationSim = new Percolation(N);
           while (!percolationSim.percolates()) {
               openRandomSite(percolationSim, N);
           }
           sampleValues[i] = percolationSim.numberOfOpenSites();
           mean += percolationSim.numberOfOpenSites();
       }
       mean /= T;
       calculateStdDev(sampleValues, T);
       confidenceLow = mean - 1.96 * stddev / Math.sqrt(T);
       confidenceHigh = mean + 1.96 * stddev / Math.sqrt(T);

    }

    /* Open random site in percolation grid */
    private static void openRandomSite(Percolation percolation, int N) {
        int row, col;
        do {
            row = StdRandom.uniform(N);
            col = StdRandom.uniform(N);
        } while (percolation.isOpen(row, col));

        percolation.open(row, col);
    }

    private void calculateStdDev(int[] sampleValues, int T) {
        for (int i = 0; i < T; i++) {
            stddev += Math.pow(sampleValues[i] - mean, 2);
        }

        stddev /= (T - 1);
        stddev = Math.sqrt(stddev);
    }

    /* Returns the sample mean of percolation threshold */
    public double mean() {
        return mean;
    }

    /* Returns sample standard deviation of percolation threshold */
    public double stddev() {
        return stddev;
    }

    /* Returns low  endpoint of 95% confidence interval */
    public double confidenceLow() {
        return confidenceLow;
    }

    /* Returns high endpoint of 95% confidence interval */
    public double confidenceHigh() {
        return confidenceHigh;
    }
}
