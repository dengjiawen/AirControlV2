package main.java.path;

import java.awt.geom.Point2D;

public class CheckPoint extends Point2D {

    float x;
    float y;

    CheckPoint next_point;
    CheckPoint prev_point;

    CheckPoint intersection;

    public float heading;

    PathType type;

    public CheckPoint(float x, float y, PathType type) {
        this.x = x;
        this.y = y;

        this.type = type;
    }

    public CheckPoint(Point2D p, PathType type, float heading) {
        this.type = type;
        setLocation(p);
        this.heading = heading;
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

}
