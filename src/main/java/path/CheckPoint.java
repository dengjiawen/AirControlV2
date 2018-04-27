package main.java.path;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class CheckPoint extends Point2D {

    float x;
    float y;

    CheckPoint next_point;
    CheckPoint prev_point;

    public CheckPoint intersection;

    public float heading;

    PathType type;
    PathAssignment assignment;

    public CheckPoint(float x, float y, PathType type) {
        this.x = x;
        this.y = y;

        this.type = type;
    }

    public CheckPoint(Point2D p, PathType type, PathAssignment assignment, float heading) {
        this.type = type;
        setLocation(p);
        this.heading = heading;
        this.assignment = assignment;

        MapUtils.common_collection.add(this);
    }

    public void setNextPoint (CheckPoint p) {

        next_point = p;

    }

    public CheckPoint getNextPoint() {

        return next_point;

    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setLocation(Point2D p) {
        this.x = (float) p.getX();
        this.y = (float) p.getY();
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public enum PathType {

        RUNWAY, TAXIWAY, PARKING

    }

    public enum PathAssignment {

        rwy26(MapUtils.rwy26), rwy35(MapUtils.rwy35), rwy3L(MapUtils.rwy3L), rwy3R(MapUtils.rwy3R),

        taxiE(MapUtils.taxiE), taxiD(MapUtils.taxiD),

        taxiA(MapUtils.taxiA), taxiA1(MapUtils.taxiA1), taxiA2(MapUtils.taxiA2), taxiA3(MapUtils.taxiA3);

        Line2D runway_reference;

        PathAssignment(Line2D rwy) {

            this.runway_reference = rwy;

        }

        public static PathAssignment getAssignment (Line2D rwy_ref) {

            for (PathAssignment assignment : values()) {
                if (assignment.runway_reference == rwy_ref) {
                    return assignment;
                }
            }

            return null;

        }



    }

}
