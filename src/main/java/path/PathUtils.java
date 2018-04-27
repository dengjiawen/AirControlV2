package main.java.path;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PathUtils {

    static final int point_intervals = 50;

    public static float getSlope(Line2D map_path) {

        return
                (float) ((map_path.getY1() - map_path.getY2()) / (map_path.getX1() - map_path.getX2()));

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

        CheckPoint.PathAssignment assignment = CheckPoint.PathAssignment.getAssignment(map_path);

        point_array.add(new CheckPoint(map_path.getP1(), type, assignment, heading));

        Point2D point_buffer = map_path.getP1();
        int counter = 0;

        float target_length = getDistance(map_path.getP1(), map_path.getP2());

        Directions target_direction = (map_path.getX1() < map_path.getX2()) ? Directions.FORWARDS : Directions.BACKWARDS;

        while (counter * point_intervals <= target_length) {

            CheckPoint new_point = new CheckPoint(point_buffer, type, assignment, heading);

            point_array.get(point_array.size() - 1).setNextPoint(new_point);

            point_array.add(new_point);
            point_buffer = findNextPoint(point_buffer, map_path, target_direction);

            counter++;

        }

        point_array.get(point_array.size() - 1).setNextPoint(null);

        return point_array;

    }

    public static void findAllIntersectingPoints(ArrayList<CheckPoint> all_points) {

        all_points.forEach(p1 -> {

            all_points.forEach(p2 -> {

                if (getDistance(p1, p2) < point_intervals / 2 && p1.assignment != p2.assignment) {
                    p1.intersection = p2;
                }

            });

        });

    }

    private enum Directions {
        FORWARDS, BACKWARDS
    }

}
