package edu.umb.cs210.p1;

import stdlib.StdOut;
import stdlib.StdRandom;
import stdlib.StdStats;

// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {
    int T; // number of independent experiments
    double[] p;  // percolation threshold for the T experiments

    // Performs T independent experiments (Monte Carlo simulations) on an
    // N-by-N grid.
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.T = T;
        p = new double[T];

        for (int i = 0; i < T; i++) {
            Percolation percolation = new Percolation(N);
            while (!(percolation.percolates())) {
                int r = StdRandom.uniform(0, N);
                int c = StdRandom.uniform(0, N);
                percolation.open(r, c);
            }
            p[i] = 1.0 * percolation.numberOfOpenSites() / (N * N);
        }
    }

    // Returns sample mean of percolation threshold.
    public double mean() {
        return StdStats.mean(p);
    }

    // Returns sample standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(p);
    }

    // Returns low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        double confidenceFraction = (1.96 * stddev()) / Math.sqrt(T);
        return mean() - confidenceFraction;
    }

    // Returns high endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        double confidenceFraction = (1.96 * stddev()) / Math.sqrt(T);
        return mean() + confidenceFraction;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}

