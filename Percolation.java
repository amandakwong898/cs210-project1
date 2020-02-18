package edu.umb.cs210.p1;

import dsa.WeightedQuickUnionUF;
import stdlib.In;
import stdlib.StdOut;

// Models an N-by-N percolation system.
public class Percolation {
    int N; // percolation system size
    boolean[][] open; // percolation system
    int openSites; // number of open sites
    WeightedQuickUnionUF uf1; // union-find representations
    WeightedQuickUnionUF uf2; //  of the percolation system

    // Creates an N-by-N grid, with all sites blocked.
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        this.N = N;
        open = new boolean[N][N];
        openSites = 0;
        uf1 = new WeightedQuickUnionUF(N * N + 2);
        uf2 = new WeightedQuickUnionUF(N * N + 1);

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < N; j++) {
                uf1.union(0, encode(0, j));
                uf2.union(0, encode(0, j));
            }
        }
        for (int i = N - 1; i < N; i++) {
            for (int j = 0; j < N; j++) {
                uf1.union(encode(N - 1, j), N * N + 1);
            }
        }
    }

    // Opens site (i, j) if it is not open already.
    public void open(int i, int j) {
        if (!(open[i][j])) {
            if (i < 0 && i > (N - 1) && j < 0 && j > (N - 1)) {
                throw new IndexOutOfBoundsException();
            }
            if ((i - 1) >= 0 && isOpen(i - 1, j)) {
                uf1.union(encode(i, j), encode(i - 1, j));
                uf2.union(encode(i, j), encode(i - 1, j));
            }
            if ((i + 1) < N && isOpen(i + 1, j)) {
                uf1.union(encode(i, j), encode(i + 1, j));
                uf2.union(encode(i, j), encode(i + 1, j));
            }
            if ((j - 1) >= 0 && isOpen(i, j - 1)) {
                uf1.union(encode(i, j), encode(i, j - 1));
                uf2.union(encode(i, j), encode(i, j - 1));
            }
            if ((j + 1) < N && isOpen(i, j + 1)) {
                uf1.union(encode(i, j), encode(i, j + 1));
                uf2.union(encode(i, j), encode(i, j + 1));
            }
            open[i][j] = true;
            openSites += 1;
        }
    }

    // Checks if site (i, j) is open.
    public boolean isOpen(int i, int j) {
        if (i < 0 && i > (N - 1) && j < 0 && j > (N - 1)) {
            throw new IndexOutOfBoundsException();
        }
        return open[i][j];
    }

    // Checks if site (i, j) is full (a site is full if open and its
    // corresponding site is connected to the source site).
    public boolean isFull(int i, int j) {
        if (i < 0 && i > (N - 1) && j < 0 && j > (N - 1)) {
            throw new IndexOutOfBoundsException();
        }
        return isOpen(i, j) && uf1.connected(0, encode(i, j));

    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Checks if the system percolates (system percolates if sink
    // site is connected to the source site).
    public boolean percolates() {
        return uf1.connected(0, N * N + 1);
    }

    // Returns an integer ID (1...N) for site (i, j).
    protected int encode(int i, int j) {
        int id = i * N + j + 1;
        return id;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        }
        else {
            StdOut.println("does not percolate");
        }

        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}
