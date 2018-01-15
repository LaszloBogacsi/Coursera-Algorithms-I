import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    int gridSize; // size of a square grid
    int[] openSites;  // 0 closed, 1, open
    int numberOfOpenSites; // counter for how many sites are open
    WeightedQuickUnionUF weightedQuickUnionUF;
    WeightedQuickUnionUF weightedQuickUnionUFNoBackwash;
    int currSite;
    int virtualTopNode;
    int virtualBottomNode;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("Grid size must be positive integer");
        gridSize = n;
        openSites = new int[gridSize * gridSize+2];
        virtualTopNode  = gridSize * gridSize ;
        virtualBottomNode  = gridSize * gridSize + 1;
        weightedQuickUnionUF = new WeightedQuickUnionUF(gridSize * gridSize + 2);
        weightedQuickUnionUFNoBackwash = new WeightedQuickUnionUF(gridSize * gridSize + 1);

        numberOfOpenSites = 0;
        for (int i = 0; i < n * n+2 ; i++) {
            openSites[i] = 0;
        }
        openSites[virtualTopNode] = 1;
        openSites[virtualBottomNode] = 1;
    }
    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        currSite = xyTo1D(row, col);

        // if site is not open, then open it
        if(!isOpen(row, col)){
            openSites[currSite] = 1;
            numberOfOpenSites ++;
        } else {
            return;
        }
        // Try connect site to neighbours
        //Left neighbour
        if (row == 1) {
            connectNeighbourIfPossible(weightedQuickUnionUF, currSite, virtualTopNode);
            connectNeighbourIfPossible(weightedQuickUnionUFNoBackwash, currSite, virtualTopNode);

        }
        if (row == gridSize) connectNeighbourIfPossible(weightedQuickUnionUF, currSite, virtualBottomNode);
        if ((col - 1 > 0) && isOpen(row, col -1 )) {
            int leftSite = xyTo1D(row, col -1);
            connectNeighbourIfPossible(weightedQuickUnionUF, currSite, leftSite);
            connectNeighbourIfPossible(weightedQuickUnionUFNoBackwash, currSite, leftSite);
        }
        //Top neighbour
        if ((row - 1 > 0) && isOpen(row-1, col )) {
            int topSite = xyTo1D(row -1, col);
            connectNeighbourIfPossible(weightedQuickUnionUF, currSite, topSite);
            connectNeighbourIfPossible(weightedQuickUnionUFNoBackwash, currSite, topSite);
        }
        //Right neighbour
        if ((col + 1  <= gridSize) && isOpen(row, col + 1 )) {
            int rightSite = xyTo1D(row, col + 1);
            connectNeighbourIfPossible(weightedQuickUnionUF, currSite, rightSite);
            connectNeighbourIfPossible(weightedQuickUnionUFNoBackwash, currSite, rightSite);
        }
        //Bottom neighbour
        if ((row + 1 <= gridSize) && isOpen(row + 1, col)) {
            int bottomtSite = xyTo1D(row + 1, col);
            connectNeighbourIfPossible(weightedQuickUnionUF, currSite, bottomtSite);
            connectNeighbourIfPossible(weightedQuickUnionUFNoBackwash, currSite, bottomtSite);
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        return openSites[xyTo1D(row, col)] == 1;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        int site = xyTo1D(row, col);
        return  isOpen(row, col) && weightedQuickUnionUFNoBackwash.connected(site, virtualTopNode);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return weightedQuickUnionUF.connected(virtualTopNode, virtualBottomNode);
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(5);
        int row, col;
        row = 1;
        col = 2;
        percolation.open(row, col);
        System.out.println("isOpen? " + percolation.isOpen(row, col));
        System.out.println("isFull? " + percolation.isFull(row, col));
        System.out.println("does it percolate? " + percolation.percolates());
        System.out.println("num of open sites: " + percolation.numberOfOpenSites());
        System.out.println("---------------------------------");
        row = 2;
        percolation.open(row, col);
        System.out.println("isOpen? " + percolation.isOpen(row, col));
        System.out.println("isFull? " + percolation.isFull(row, col));
        System.out.println("does it percolate? " + percolation.percolates());
        System.out.println("num of open sites: " + percolation.numberOfOpenSites());
        System.out.println("---------------------------------");
        row = 3;
        percolation.open(row, col);
        System.out.println("isOpen? " + percolation.isOpen(row, col));
        System.out.println("isFull? " + percolation.isFull(row, col));
        System.out.println("does it percolate? " + percolation.percolates());
        System.out.println("num of open sites: " + percolation.numberOfOpenSites());
        System.out.println("---------------------------------");
        row = 4;
        percolation.open(row, col);
        System.out.println("isOpen? " + percolation.isOpen(row, col));
        System.out.println("isFull? " + percolation.isFull(row, col));
        System.out.println("does it percolate? " + percolation.percolates());
        System.out.println("num of open sites: " + percolation.numberOfOpenSites());
        System.out.println("---------------------------------");
        row = 5;
        percolation.open(row, col);
        System.out.println("isOpen? " + percolation.isOpen(row, col));
        System.out.println("isFull? " + percolation.isFull(row, col));
        System.out.println("does it percolate? " + percolation.percolates());
        System.out.println("num of open sites: " + percolation.numberOfOpenSites());
        System.out.println("---------------------------------");

    }

    // translate 2D coordinates to 1D pointer
    private int xyTo1D(int row, int col) {
        validate(row, col);
        return (row - 1) * gridSize + col - 1;
    }

    private void validate(int row, int col){
        if((row <= 0 || row > gridSize ) || (col <= 0 || col > gridSize )) {
            throw new IllegalArgumentException("The index is out of the grid");
        }
    }

    private void connectNeighbourIfPossible(WeightedQuickUnionUF uf , int currSite, int neighbourSite){
        if (!uf.connected(currSite, neighbourSite)) {
            uf.union(neighbourSite, currSite);
        }
    }
}
