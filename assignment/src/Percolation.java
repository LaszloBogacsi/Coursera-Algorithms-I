import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * @author Laszlo Bogacsi
 * @since 20/01/2018
 *
 * The percolation class implements the weighted union find algorithm-s API
 * in order to find out if a given system percolates or not.
 * Initializes a N*N grid with all-blocked sites, then by opening a random site
 * one by one can check whether the system percolates.
 */

public class Percolation {
    // size of a square grid
    private final int gridSize;
    // array to track opened sites, values: false blocked, true open
    private final boolean[] openSites;
    // counter for how many sites are open
    private int numberOfOpenSites;
    // the main quic union find object with 2 virtual nodes to check if the
    // system percolates
    private final WeightedQuickUnionUF wQUUF;
    /* to work around the backwash problem, this wQUF object has only the top
     * virtual node, used only to tell if a site is full
     */
    private final WeightedQuickUnionUF wQUUFNoBackwash;
    // the id of the top virtual node ( last site +1 )
    private final int vTopNode;
    // the id of the bottom virtual node ( last site +2 )
    private final int vBottomNode;

    /**
     * The constructor initializes the grid and the blocked sites,
     * creates an array to track open sites,
     * creates two virtual nodes,
     * instantiates the weighted quick union find object.
     * @exception  IllegalArgumentException on invalid grid-size (less than
     * or equal to 0
     * @param n size of the grid
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                    "Grid size must be positive integer");
        }
        gridSize = n;
        openSites = new boolean[gridSize * gridSize+2];
        vTopNode = gridSize * gridSize ;
        vBottomNode = gridSize * gridSize + 1;
        wQUUF = new WeightedQuickUnionUF(gridSize * gridSize + 2);
        wQUUFNoBackwash = new WeightedQuickUnionUF(gridSize * gridSize + 1);

        numberOfOpenSites = 0;
        for (int i = 0; i < n * n+2 ; i++) {
            openSites[i] = false;
        }
        // mark the virtual top and bottom nodes open
        openSites[vTopNode] = true;
        openSites[vBottomNode] = true;
    }

    /**
     * Opens a site if it's not already open and connects the site to
     * it's 4 adjacent neighbours (left, right, above, below)
     * @param row row number of the site (first row is 1 last is n)
     *            numbering starts at the top left corner
     * @param col column nuber of the site (first column is 1 last is n)
     *            numbering starts at the top left corner
     */
    public void open(int row, int col) {
        validate(row, col);
        int currSite = xyTo1D(row, col);

        // if site is not open, then open it
        if (!isOpen(row, col)){
            openSites[currSite] = true;
            numberOfOpenSites ++;
        } else {
            return;
        }
        // upon first row, automatically connect to virtual top node
        if (row == 1) {
            connectNeighbour(wQUUF, currSite, vTopNode);
            connectNeighbour(wQUUFNoBackwash, currSite, vTopNode);

        }
        // upon last row, automatically connect to virtual bottom node
        if (row == gridSize) connectNeighbour(wQUUF, currSite, vBottomNode);
        // Left neighbour
        if ((col - 1 > 0) && isOpen(row, col -1 )) {
            int leftSite = xyTo1D(row, col -1);
            connectNeighbour(wQUUF, currSite, leftSite);
            connectNeighbour(wQUUFNoBackwash, currSite, leftSite);
        }
        // Top neighbour
        if ((row - 1 > 0) && isOpen(row-1, col )) {
            int topSite = xyTo1D(row -1, col);
            connectNeighbour(wQUUF, currSite, topSite);
            connectNeighbour(wQUUFNoBackwash, currSite, topSite);
        }
        // Right neighbour
        if ((col + 1  <= gridSize) && isOpen(row, col + 1 )) {
            int rightSite = xyTo1D(row, col + 1);
            connectNeighbour(wQUUF, currSite, rightSite);
            connectNeighbour(wQUUFNoBackwash, currSite, rightSite);
        }
        // Bottom neighbour
        if ((row + 1 <= gridSize) && isOpen(row + 1, col)) {
            int bottomtSite = xyTo1D(row + 1, col);
            connectNeighbour(wQUUF, currSite, bottomtSite);
            connectNeighbour(wQUUFNoBackwash, currSite, bottomtSite);
        }
    }

    // is site (row, col) open?

    /**
     * The isOpen method determines whether a site is open or not
     * based on the sites position in the grid.
     * @param row row number of the site (first row is 1 last is n)
     *            numbering starts at the top left corner
     * @param col column nuber of the site (first column is 1 last is n)
     *            numbering starts at the top left corner
     * @return true if open, false if blocked
     */
    public boolean isOpen(int row, int col) {
        return openSites[xyTo1D(row, col)];
    }

    /**
     * The isFull method determines whether a site is full or not
     * based on the sites position in the grid.
     * Full if connected to top virtual node.
     * @param row row number of the site (first row is 1 last is n)
     *            numbering starts at the top left corner
     * @param col column nuber of the site (first column is 1 last is n)
     *            numbering starts at the top left corner
     * @return true if full, false if empty (but open)
     */
    public boolean isFull(int row, int col) {
        int site = xyTo1D(row, col);
        return isOpen(row, col) && wQUUFNoBackwash.connected(site, vTopNode);
    }

    // number of open sites

    /**
     * This method returns the number of currently open sites
     * @return number of open sites (int)
     */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /**
     * Determines whether the system percolates
     * A system percolates if a site in the top row is connected
     * to a site in the bottom row.
     * @return true if percolates, false if not.
     */
    public boolean percolates() {
        return wQUUF.connected(vTopNode, vBottomNode);
    }

    /**
     * mmain method for testing
     * @param args currently unused
     */
    public static void main(String[] args) {
        // optional tests here
    }

    /**
     * Private method to translate 2D coordinates(row column) to
     * 1D coordinate (array index) and validates the input coordinates.
     * @param row row number of the site (first row is 1 last is n)
     *            numbering starts at the top left corner
     * @param col column nuber of the site (first column is 1 last is n)
     *            numbering starts at the top left corner
     * @return an int starting from 0 (for array index)
     */
    private int xyTo1D(int row, int col) {
        validate(row, col);
        return (row - 1) * gridSize + col - 1;
    }

    /**
     * This method validates a pair of site coordinates
     * to check if they fall inside or outside the grid
     * @exception IllegalArgumentException on coordinates
     * that fall outside the grid.
     * @param row row number of the site (first row is 1 last is n)
     *            numbering starts at the top left corner
     * @param col column nuber of the site (first column is 1 last is n)
     *            numbering starts at the top left corner
     */
    private void validate(int row, int col){
        if ((row <= 0 || row > gridSize ) || (col <= 0 || col > gridSize )) {
            throw new IllegalArgumentException( "The index is out of the grid" );
        }
    }

    /**
     * convenience method for re-usability. Calls the union method on the
     * weightedQuickUnionFind object to connect two sites if they are not
     * already connected.
     * @param uf WeightedQuickUnionFind object
     * @param currSite the id of the current site
     * @param neighbourSite the id of the adjacent neighbour site
     *                      (try to connect the current site to this)
     */
    private void connectNeighbour(WeightedQuickUnionUF uf,
                                  int currSite, int neighbourSite){
        if (!uf.connected(currSite, neighbourSite)) {
            uf.union(neighbourSite, currSite);
        }
    }
}
