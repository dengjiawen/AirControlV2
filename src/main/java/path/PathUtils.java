package main.java.path;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PathUtils {

    static final int point_intervals = 50;

    public static float getSlope(Line2D map_path) {

        return (float) ((map_path.getY1() - map_path.getY2()) / (map_path.getX1() - map_path.getX2()));

    }

    public static float getIntercept(float m, float x, float y) {

        return y - m * x;

    }

    public static Point2D getPoint(float m, float k, float x) {

        return new Point2D.Float(x, m * x + k);

    }

    public static Point2D findNextPoint(Point2D prev_point, Line2D map_path, Directions direction) {

        float m = getSlope(map_path);
        float k = getIntercept(m, (float) map_path.getX1(), (float) map_path.getY1());

        float x = 0f;

        switch (direction) {
            case FORWARDS:
                x = (float) (prev_point.getX() + Math.sqrt(Math.pow(point_intervals, 2) / (1 + Math.pow(m, 2))));
                break;
            case BACKWARDS:
                x = (float) (prev_point.getX() - Math.sqrt(Math.pow(point_intervals, 2) / (1 + Math.pow(m, 2))));
                break;
        }

        return getPoint(m, k, x);

    }

    public static float getDistance(Point2D p1, Point2D p2) {

        return (float) Math.sqrt(
                Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2));

    }

    public static float getAngle(Point2D p1, Point2D p2) {

        float related_acute_angle = (float) Math.atan2(Math.abs(p1.getY() - p2.getY()), Math.abs(p1.getX() - p2.getX()));

        if (p1.getX() < p2.getX()) {
            if (p1.getY() < p2.getY()) {
                related_acute_angle = (float) (Math.PI / 2 + related_acute_angle);
            } else {
                related_acute_angle = (float) (Math.PI / 2 + Math.PI - related_acute_angle);
            }
        } else {
            if (p1.getY() < p2.getY()) {
                related_acute_angle = (float) (Math.PI / 2 - Math.PI - related_acute_angle);
            } else {
                related_acute_angle = (float) (Math.PI / 2 - Math.PI + related_acute_angle);
            }
        }

        if (!(p1.getX() < p2.getX() && p1.getY() > p2.getY())) {
            related_acute_angle = (float) (related_acute_angle + Math.PI);
        }

        return related_acute_angle;

    }

    public static ArrayList<CheckPoint> findPointArray(Line2D map_path, CheckPoint.PathType type) {

        ArrayList<CheckPoint> point_array = new ArrayList<>();

        float heading = getAngle(map_path.getP1(), map_path.getP2());

        point_array.add(new CheckPoint(map_path.getP1(), type, heading));

        Point2D point_buffer = map_path.getP1();
        int counter = 0;

        float target_length = getDistance(map_path.getP1(), map_path.getP2());

        Directions target_direction = (map_path.getX1() < map_path.getX2()) ? Directions.FORWARDS : Directions.BACKWARDS;

        while (counter * point_intervals <= target_length) {

            CheckPoint new_point = new CheckPoint(point_buffer, type, heading);

            point_array.get(point_array.size() - 1).setNextPoint(new_point);

            point_array.add(new_point);
            point_buffer = findNextPoint(point_buffer, map_path, target_direction);

            counter++;

        }

        point_array.get(point_array.size() - 1).setNextPoint(null);

        return point_array;

    }

    public static Intersection findIntersectionPoint(Line2D l1, Line2D l2) {

        Point2D A = l1.getP1();
        Point2D B = l1.getP2();

        Point2D C = l2.getP1();
        Point2D D = l2.getP2();

        // Line AB represented as a1x + b1y = c1
        double a1 = B.getY() - A.getY();
        double b1 = A.getX() - B.getX();
        double c1 = a1 * (A.getX()) + b1 * (A.getY());

        // Line CD represented as a2x + b2y = c2
        double a2 = D.getY() - C.getY();
        double b2 = C.getX() - D.getX();
        double c2 = a2 * (C.getX()) + b2 * (C.getY());

        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0) {
            return null;
        } else {
            double x = (b2 * c1 - b1 * c2) / determinant;
            double y = (a1 * c2 - a2 * c1) / determinant;

            if (xIsOnLine(x, l1) && xIsOnLine(x, l2) &&
                    yIsOnLine(y, l1) && yIsOnLine(y, l2)) {
                return new Intersection(new Point2D.Double(x, y),
                        CheckPoint.PathAssignment.getAssignment(l1), CheckPoint.PathAssignment.getAssignment(l2));
            }
        }

        return null;

    }

    public static boolean xIsOnLine(double x, Line2D line) {

        if (x >= line.getX1()) {

            return x <= line.getX2();

        } else {

            return x >= line.getX2();

        }

    }

    public static boolean yIsOnLine(double y, Line2D line) {

        if (y >= line.getY1()) {

            return y <= line.getY2();

        } else {

            return y >= line.getY2();

        }

    }

    static boolean exists;

    public static void findAllIntersectingPoints() {

        for (CheckPoint.PathAssignment assignment : CheckPoint.PathAssignment.values()) {

            for (CheckPoint.PathAssignment a : CheckPoint.PathAssignment.values()) {

                Intersection int_point = findIntersectionPoint(assignment.runway_reference, a.runway_reference);

                if (int_point != null) {
                    exists = false;

                    MapUtils.intersecting_collection.forEach(p -> {
                        if (int_point.equals(p)) {
                            exists = true;
                        }
                    });

                    if (!exists) {

                        MapUtils.intersecting_collection.add(int_point);

                    }
                }

            }

        }

    }

    private enum Directions {
        FORWARDS, BACKWARDS
    }

}
