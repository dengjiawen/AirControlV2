package main.java.path;

import java.awt.geom.Point2D;

public class Intersection extends CheckPoint {

    PathAssignment assignment_a;
    PathAssignment assignment_b;

    public Intersection (Point2D point, PathAssignment a, PathAssignment b) {

        super(point);

        this.assignment_a = a;
        this.assignment_b = b;

    }

    public boolean equals (Intersection intersection) {

        if (getX() == intersection.getX() && getY() == intersection.getY()) {

            return true;

        }

        return false;

    }

    public boolean isIntersecting(PathAssignment a, PathAssignment b) {

        return (a == a && b == b) || (b == a && a == b);

    }

}
