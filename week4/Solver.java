import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Solver class initialized with an initial board tries to find the goal board and also tires to find the goalboard for a twin board
 * if a twinboard has a solution than the initial board is deemed unsolvable
 */
public class Solver {
    private SearchNode goalNode;
    private boolean isSolvable;

    /**
     * find a solution to the initial board (using the A* algorithm)
     * Can throw IllegalArgumentException if initial board is null.
     * @param initial, a board to start the search from.
     */
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial Board must not be null");
        Board twinBoard = initial.twin();

        final Comparator<SearchNode> priorityFunction = Comparator.comparingInt(SearchNode::getPriority);
        MinPQ<SearchNode> initialPQ = new MinPQ<>(priorityFunction);
        MinPQ<SearchNode> twinPQ = new MinPQ<>(priorityFunction);

        final SearchNode initialSearchNode = createManhattanSearchNode(null, initial, 0);
        final SearchNode initialTwinSearchNode = createManhattanSearchNode(null, twinBoard, 0);
        initialPQ.insert(initialSearchNode);
        twinPQ.insert(initialTwinSearchNode);
        while (!initialPQ.isEmpty()) {
            final SearchNode minNode = initialPQ.delMin();
            final SearchNode twinMinNode = twinPQ.delMin();

            if (minNode.board.isGoal()) {
                goalNode = minNode;
                isSolvable = true;
                break;
            }

            if (twinMinNode.board.isGoal()) {
                goalNode = null;
                isSolvable = false;
                break;
            }

            insertNeighboursFor(initialPQ, minNode);
            insertNeighboursFor(twinPQ, twinMinNode);
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return this.isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return goalNode != null ? goalNode.moves : -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (goalNode == null) return null;
        SearchNode node = goalNode;
        List<Board> boards = new ArrayList<>();
        boards.add(node.board);
        while (node.previousNode != null) {
            boards.add(node.previousNode.board);
            node = node.previousNode;
        }
        Collections.reverse(boards);
        return boards;
    }

    // test client (see below)
    public static void main(String[] args) {
        // unit tests here
    }


    private void insertNeighboursFor(MinPQ<SearchNode> pq, SearchNode minNode) {
        for (Board neighbour : minNode.board.neighbors()) {
            if (minNode.previousNode == null) {
                pq.insert(createManhattanSearchNode(minNode, neighbour, minNode.moves + 1));
            } else if (!neighbour.equals(minNode.previousNode.board)) {
                pq.insert(createManhattanSearchNode(minNode, neighbour, minNode.moves + 1));
            }
        }
    }

    private SearchNode createManhattanSearchNode(SearchNode minNode, Board neighbour, int moves) {
        return new SearchNode(neighbour, moves, minNode);
    }

    private static class SearchNode {
        private final Board board;
        private final int moves;
        private final SearchNode previousNode;
        private final int priority;

        SearchNode(Board board, int moves, SearchNode previousNode) {

            this.board = board;
            this.moves = moves;
            this.previousNode = previousNode;
            this.priority = board.manhattan() + moves;
        }

        private int getPriority() {
            return priority;
        }
    }


}
