import edu.princeton.cs.algs4.MinPQ;

import java.util.*;
import java.util.stream.Collectors;

public class Solver {
    SearchNode goalNode;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial Board must not be null");
        final Comparator<SearchNode> hammingPriorityFunction = Comparator.comparingInt(SearchNode::hammingPriority);
        MinPQ pq = new MinPQ(hammingPriorityFunction);

        final SearchNode initialSearchNode = new SearchNode(initial, 0, null);
        pq.insert(initialSearchNode);
        while (!pq.isEmpty()) {
            final SearchNode minNode = (SearchNode) pq.delMin();
            if (minNode.board.isGoal()) {
                goalNode = minNode;
                break;
            }
            for (Board neighbour : minNode.board.neighbors()) {
                pq.insert(new SearchNode(neighbour, minNode.moves + 1, minNode));
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return true;
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

        SearchNode(Board board, int moves, SearchNode previousNode) {

            this.board = board;
            this.moves = moves;
            this.previousNode = previousNode;
        }

        int hammingPriority() {
            return board.hamming() + moves;
        }

        int manhattanPriority() {
            return board.manhattan() + moves;
        }


    }
}
