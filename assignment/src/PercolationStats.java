import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    double[] results;
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("n or trials should be a positive integer");
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
    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);

    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96*stddev()/Math.sqrt(results.length));

    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96*stddev()/Math.sqrt(results.length));

    }

    public static void main(String[] args) {
//        int n = Integer.parseInt(args[0]);
//        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(20, 1000);
        System.out.println("mean:                    = "+ percolationStats.mean());
        System.out.println("stddev:                  = " + percolationStats.stddev());
        System.out.println("95% confidence interval: = [ "+ percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() +" ]");
    }
}
