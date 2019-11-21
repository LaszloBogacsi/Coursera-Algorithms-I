import edu.princeton.cs.algs4.MinPQ;

import java.util.Collections;

public class Solver {
    private int moves = 0;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial Board must not be null");
        MinPQ pq = new MinPQ(initial.dimension());
        // TODO: implement the algorithm here, using the min priority queue.
        // add the initial, add the neighbours, find the one to continue with based on the smallest manhattan distance.
        //
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return true;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return Collections.emptyList();
    }

    // test client (see below)
    public static void main(String[] args) {

    }
}
