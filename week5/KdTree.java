import edu.princeton.cs.algs4.*;

public class KdTree {
    private Node root;
    Stack<Point2D> pointsInRect;

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private  Node lb;             // the left/bottom subtree
        private  Node rt;             // the right/top subtree
        private  int size;            // the number of nodes in the subtree

        public Node(Point2D p, RectHV rect, int size) {
            this.p = p;
            this.rect = rect;
            this.size = size;
        }

    }

    public KdTree() {
    }                              // construct an empty set of points

    public boolean isEmpty() {
        return size() == 0;
    }                      // is the set empty?

    public int size() {
        return size(root);
    }                        // number of points in the set

    private int size(Node node) {
        if (node == null) return 0;
        else return node.size;
    }

    public void insert(Point2D p) {
        RectHV rootRect = new RectHV(0, 0, p.x(), 1);
        root = insert(root, p, rootRect);
    }              // add the point to the set (if it is not already in the set)

    private Node insert(Node node, Point2D p, RectHV rect) {
        if (node == null) return new Node(p, rect, 1);
        if (p.x() < node.p.x()) node.lb = insertY(node.lb, p, new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), p.y()));
        else if (p.x() >= node.p.x() && p.y() != node.p.y()) node.rt = insertY(node.rt, p, new RectHV(node.rect.xmax(), node.rect.ymin(), 1, p.y()));
        node.size = 1 + size(node.lb) + size(node.rt);
        return node;
    }

    private Node insertY (Node node, Point2D p, RectHV rect) {
        if (node == null) return new Node(p, rect, 1);
        if (p.y() < node.p.y()) node.lb = insert(node.lb, p, new RectHV(node.rect.xmin(), node.rect.ymin(), p.x(), node.rect.ymax()));
        else if (p.y() >= node.p.y() && p.x() != node.p.x()) node.rt = insert(node.rt, p, new RectHV(node.rect.xmin(), node.rect.ymax(), p.x(), 1));
        node.size = 1 + size(node.lb) + size(node.rt);
        return node;
    }

    public boolean contains(Point2D p) {
        return contains(root, p);
    }       // does the set contain point p?

    private boolean contains(Node node, Point2D p) {
        if (node == null) return false;
        if (node.p.equals(p)) return true;
        else if (p.x() < node.p.x()) return containsY(node.lb, p);
        else if (p.x() >= node.p.x()) return containsY(node.rt, p);
        return false; // should never happen
    }

    private boolean containsY(Node node, Point2D p) {
        if (node == null) return false;
        if (node.p.equals(p)) return true;
        else if (p.y() < node.p.y()) return contains(node.lb, p);
        else if (p.y() >= node.p.y()) return contains(node.rt, p);
        return false; // should never happen
    }


    public void draw() {
        traverseInOrderXAndDraw(root);
    }                         // draw all points to standard draw

    private void traverseInOrderXAndDraw(Node node) {
        if (node != null) {
            traverseInOrderYAndDraw(node.lb);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            node.rect.draw();
            traverseInOrderYAndDraw(node.rt);
        }
    }

    private void traverseInOrderYAndDraw(Node node) {
        if (node != null) {
            traverseInOrderXAndDraw(node.lb);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            node.rect.draw();
            traverseInOrderXAndDraw(node.rt);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        pointsInRect = new Stack<>();
        traverseInOrder(root, rect);
        return pointsInRect;
    }             // all points that are inside the rectangle (or on the boundary)


    private void traverseInOrder(Node node, RectHV rect) {
        if (node != null) {
            traverseInOrder(node.lb, rect);
            if (rect.contains(node.p)) {
                pointsInRect.push(node.p);
            }
            traverseInOrder(node.rt, rect);
        }
    }


    public Point2D nearest(Point2D p) {
        final Stack<Point2D> closest = new Stack<>();
        closest.push(root.p);
        traverseInOrderNearest(root, p, closest);
        return closest.pop();
    }             // a nearest neighbor in the set to point p; null if the set is empty


    private void traverseInOrderNearest(Node node, Point2D p, Stack<Point2D> closest) {
        if (node != null) {
            traverseInOrderNearest(node.lb, p, closest);
            if (node.p.distanceTo(p) < p.distanceTo(closest.peek())) {
                closest.push(node.p);
            }
            traverseInOrderNearest(node.rt, p, closest);
        }
    }

    public static void main(String[] args) {

    }                  // unit testing of the methods (optional)
}
