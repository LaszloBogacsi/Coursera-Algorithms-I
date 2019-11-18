import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {
    private int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
    }

    // string representation of this board
    public String toString() {
        String PADDING = " ";
        StringBuilder sb = new StringBuilder();
        sb.append(this.dimension());
        sb.append("\n" + PADDING);
        for (int i = 0; i < tiles.length; i++) {
            String row = Arrays.stream(tiles[i]).mapToObj(String::valueOf).collect(Collectors.joining(" "));
            sb.append(row);
            sb.append("\n" + PADDING);
        }
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
        return tileValue == (row * this.dimension() + col);
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan_distance = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                int t = tiles[i][j];
                if (t != 0 && !isInPlace(i, j, t)) {
                    int dx, dy;
                    int tx = t / this.dimension();
                    int ty = t - (tx * this.dimension());
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

        return Collections.emptyList();
    }

//    // a board that is obtained by exchanging any pair of tiles
//    public Board twin()
//
    // unit testing (not graded)
    public static void main(String[] args) {}
}
