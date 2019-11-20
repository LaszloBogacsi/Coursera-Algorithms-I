import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Board {
    private int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.dimension());
        sb.append("\n ");
        final String body = Arrays.stream(tiles)
                .map(row -> Arrays.stream(row)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n "));
        sb.append(body);
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return this.tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int distance = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                int t = tiles[i][j];
                if (t != 0 && !isInPlace(i, j, t)) {
                    distance++;
                }
            }
        }
        return distance;
    }

    private boolean isInPlace(int row, int col, int tileValue) {
        return tileValue == (row * this.dimension() + col) + 1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan_distance = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                int t = tiles[i][j];
                if (t != 0 && !isInPlace(i, j, t)) {
                    int dx, dy;
                    int tx = (t-1) / this.dimension();
                    int ty = (t-1) - (tx * this.dimension());
                    dx = Math.abs(i - tx);
                    dy = Math.abs(j - ty);
                    System.out.println(t + ": " + dx + " " + dy);
                    manhattan_distance += (dx + dy);
                }
            }
        }
        return manhattan_distance;
    }

//    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        Board that = (Board) y;
        boolean hasSameSize = this.dimension() == that.dimension();
        boolean tilesInSamePosition = Arrays.deepEquals(this.tiles, that.tiles);
        return hasSameSize && tilesInSamePosition;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        final int[] zeroPos = getZeroPos(tiles);
        System.out.println("zeroPos = " + zeroPos[0] + ":" +zeroPos[1]);
        return createNeighbour(zeroPos[0], zeroPos[1]);
    }

    private List<Board> createNeighbour(int row, int col) {
        List<Board> neighbouringBoards = new ArrayList<>();
        // go left (col -1)
        if (col - 1 >= 0) {
            neighbouringBoards.add(new Board(exchangeTo(row, col, row, col - 1)));
        }
        // go right (col +1)
        if (col + 1 <= dimension()) {
            neighbouringBoards.add(new Board(exchangeTo(row, col, row, col + 1)));
        }
        // go down (row + 1)
        if (row + 1 <= dimension()) {
            neighbouringBoards.add(new Board(exchangeTo(row, col, row + 1, col)));
        }
        // go up (row - 1)
        if (row - 1 >= 0) {
            neighbouringBoards.add(new Board(exchangeTo(row, col, row - 1, col)));
        }

        return neighbouringBoards;
    }

    private int[][] exchangeTo(int row, int col, int newRow, int newCol) {
        int[][] copy = getCopy();
        int valToMove = copy[newRow][newCol];
        int valFromMove = copy[row][col];
        copy[newRow][newCol] = valFromMove;
        copy[row][col] = valToMove;
        return copy;
    }

    private int[] getZeroPos(int[][] tiles) {
        int row = 0;
        int col = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                int t = tiles[i][j];
                if (t == 0) {
                    row = i;
                    col = j;
                }
            }
        }
        return new int[]{row, col};
    }

        // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
//         avoid row of 0, and swap the col 0 with col 1;
        int[] zeroPos = getZeroPos(tiles);
        int row = zeroPos[0];
        int swapRow = row == 0 ? row + 1 : row == dimension() -1 ? row - 1  : row + 1;
        return new Board(exchangeTo(swapRow, 0, swapRow, 1));
    }

    private int[][] getCopy() {
        return Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] threeByThree = {
                {1, 0, 2},
                {4, 6, 3},
                {7, 5, 8}
        };
        Board board = new Board(threeByThree);

        int[][] threeByThreeSolved = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };
        Board solvedBoard = new Board(threeByThreeSolved);

        StdOut.println("toString: " + board.toString());
        assert board.toString().equals(
                "3\n" +
                " 1 0 2\n" +
                " 4 6 3\n" +
                " 7 5 8");
        StdOut.println("Dimension: " + board.dimension());
        assert board.dimension() == threeByThree.length;

        final int hamming = board.hamming();
        StdOut.println("Hamming Distance: " + hamming);
        assert hamming == 5;
        final int solvedHamming = solvedBoard.hamming();
        StdOut.println("Hamming Distance (SOLVED): " + solvedHamming);
        assert solvedHamming == 0;

        final int manhattan = board.manhattan();
        StdOut.println("Manhattan Distance: " + manhattan);
        assert manhattan == 5;
        final int solvedManhattan = solvedBoard.manhattan();
        StdOut.println("Manhattan Distance (SOLVED): " + solvedManhattan);
        assert solvedManhattan == 0;

        final boolean isGoal = solvedBoard.isGoal();
        StdOut.println("Is Goal? " + isGoal);
        assert isGoal;
        final boolean isGoalUnsolved = board.isGoal();
        StdOut.println("Is Goal? " + isGoalUnsolved);
        assert !isGoalUnsolved;


        final boolean isSameEqual = board.equals(new Board(threeByThree));
        StdOut.println("Is Equal? " + isSameEqual);
        assert isSameEqual;
        final boolean isDiffEqual = board.equals(new Board(threeByThreeSolved));
        StdOut.println("Is Equal? " + isDiffEqual);
        assert !isDiffEqual;


        int[][] leftNeighbour = {
                {0, 1, 2},
                {4, 6, 3},
                {7, 5, 8}
        };

        int[][] rightNeighbour = {
                {1, 2, 0},
                {4, 6, 3},
                {7, 5, 8}
        };

        int[][] lowerNeighbour = {
                {1, 6, 2},
                {4, 0, 3},
                {7, 5, 8}
        };
        final List<Board> expectedNeighbours = Arrays.asList(new Board(leftNeighbour), new Board(rightNeighbour), new Board(lowerNeighbour));

        final Iterable<Board> neighborBoards = board.neighbors();
        assert neighborBoards.iterator().hasNext();
        neighborBoards.forEach(b -> {
            assert expectedNeighbours.contains(b);
        });

        int[][] threeByThreeTwin = {
                {1, 0, 2},
                {6, 4, 3},
                {7, 5, 8}
        };

        boolean isTwin = board.twin().equals(new Board(threeByThreeTwin));
        StdOut.println("Twin board? " + isTwin);
        assert isTwin;

    }
}
