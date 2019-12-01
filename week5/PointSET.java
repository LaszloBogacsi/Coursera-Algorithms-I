import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

public class PointSET {
    private SET<Point2D> points;

    public PointSET() {
        points = new SET<>();
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return points.isEmpty();
    }                      // is the set empty?

    public int size() {
        return points.size();
    }                         // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }           // does the set contain point p?

    public void draw() {
        points.forEach(Point2D::draw);
    }                         // draw all points to standard draw
//
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> pointsInRect = new Stack<>();
        final Iterator<Point2D> it = points.iterator();
        while (it.hasNext()) {
            final Point2D point = it.next();
            if (rect.contains(point)) {
                pointsInRect.push(point);
            }
        }
        return pointsInRect;
    }             // all points that are inside the rectangle (or on the boundary)
//
    public Point2D nearest(Point2D p) {
        if (points.isEmpty()) return null;
        Point2D closest = p;
        final Iterator<Point2D> it = points.iterator();
        while (it.hasNext()) {
            final Point2D point = it.next();
            if (point.distanceTo(p) < p.distanceTo(closest) && p != point) {
                closest = point;
            }
        }
        return closest;
    }            // a nearest neighbor in the set to point p; null if the set is empty
//
//    public static void main(String[] args)                  // unit testing of the methods (optional)

}
