import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {
    SearchNode goalNode;
    boolean isSolvable;
    // find a solution to the initial board (using the A* algorithm)
    private enum PriorityType {
        MANHATTAN, HAMMING
    }
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial Board must not be null");
        Board twinBoard = initial.twin();

        final Comparator<SearchNode> priorityFunction = Comparator.comparingInt(SearchNode::priority);
        MinPQ<SearchNode> pq = new MinPQ<>(priorityFunction);
        MinPQ<SearchNode> twinPQ = new MinPQ<>(priorityFunction);

        final SearchNode initialSearchNode = createManhattanSearchNode(null, initial, 0);
        final SearchNode initialTwinSearchNode = createManhattanSearchNode(null, twinBoard, 0);
        pq.insert(initialSearchNode);
        twinPQ.insert(initialTwinSearchNode);
        while (!pq.isEmpty()) {
            final SearchNode minNode = (SearchNode) pq.delMin();
            final SearchNode twinMinNode = (SearchNode) twinPQ.delMin();

            if (minNode.board.isGoal()) {
                goalNode = minNode;
                isSolvable = true;
                break;
            }

            if (twinMinNode.board.isGoal()) {
                isSolvable = false;
                break;
            }


            for (Board neighbour : minNode.board.neighbors()) {
                if (minNode.previousNode == null) {
                    pq.insert(createManhattanSearchNode(minNode, neighbour, minNode.moves + 1));
                } else if (!neighbour.equals(minNode.previousNode.board)) {
                    pq.insert(createManhattanSearchNode(minNode, neighbour, minNode.moves + 1));
                }
            }
            for (Board neighbour : twinMinNode.board.neighbors()) {
                if (twinMinNode.previousNode == null) {
                    twinPQ.insert(createManhattanSearchNode(twinMinNode, neighbour, twinMinNode.moves + 1));
                } else if (!neighbour.equals(twinMinNode.previousNode.board)) {
                    twinPQ.insert(createManhattanSearchNode(twinMinNode, neighbour, twinMinNode.moves + 1));
                }
            }
        }
    }

    private SearchNode createManhattanSearchNode(SearchNode minNode, Board neighbour, int moves) {
        return new ManhattanSearchNode(neighbour, moves, minNode);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return goalNode.moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
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

    }


    private class SearchNode {
        private final Board board;
        private final int moves;
        private final SearchNode previousNode;
        int priority;

        SearchNode(Board board, int moves, SearchNode previousNode, PriorityType priorityType) {

            this.board = board;
            this.moves = moves;
            this.previousNode = previousNode;
            this.priority = board.manhattan() + moves;
            switch (priorityType) {
                case MANHATTAN:
                    this.priority = board.manhattan() + moves;
                    break;
                case HAMMING:
                    this.priority = board.hamming() + moves;
                    break;
                default:
                    throw new RuntimeException("Unknown priority type: " + priorityType);
            }
        }

        int priority() {
            return priority;
        }
    }

    private class ManhattanSearchNode extends SearchNode {
        ManhattanSearchNode(Board board, int moves, SearchNode previousNode) {
            super(board, moves, previousNode, PriorityType.MANHATTAN);
        }
    }
}
