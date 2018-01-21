import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * @author Laszlo Bogacsi
 * @since 20/01/2018
 *
 * The PercolationStats class implements statistical methods to compute
 * an estimated percolation threshold (trialsm time on an n*n grid)
 */
public class PercolationStats {
    /* array of doubles to store the percolation results
        the number of open sites when the system percolates.
     */
    private final double[] results;
    private static final double CONFIDENCE_95 = 1.96;

    /**
     * The constructor performs the Percolation experiment 'trials' times on an
     * n * n grid and pushes the result in to an array of doubles.
     * @param n grid size, positive integer
     * @param trials number of time to run the experiment
     * @exception IllegalArgumentException on invald grid size or invalid trial
     * number
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(
                    "n and trials should be a positive integer");
        }
        results = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            results[i] = percolation.numberOfOpenSites() / (n * n * 1.0);
        }
    }

    /**
     * sample mean of percolation threshold
     * @return double mean of the results array
     */
    public double mean() {
        return StdStats.mean(results);
    }

    /**
     * sample standard deviation of percolation threshold
     * @return double standard deviation of the results array
     */
    public double stddev() {
        return StdStats.stddev(results);
    }


    /**
     * low  endpoint of 95% confidence interval
     * @return double of lower 95% confidence level
     */
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(results.length));

    }


    /**
     * high endpoint of 95% confidence interval
     * @return double of higher 95% confidence level
     */
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(results.length));

    }

    /**
     * main method to test the PercolationStats class
     * and print the mean, standard deviation and the 95% high and low
     * confidence levels of the percolation threshold to the standard output.
     * @param args first argument is the gridsize,
     *             the second argument is the number of times
     *             to repeat the experiment.
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        StdOut.printf("mean:                    = %s\n", percolationStats.mean());
        StdOut.printf("stddev:                  = %s\n", percolationStats.stddev());
        StdOut.printf("95%% confidence interval: = [%s, %s]", percolationStats.confidenceLo(), percolationStats.confidenceHi());
    }
}
