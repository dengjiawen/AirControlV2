package main.java.path;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class MapUtils {

    public static Line2D rwy26 = new Line2D.Float(9369, 576, 5649, 594);
    public static Line2D rwy35 = new Line2D.Float(6033, 3487, 6017, 145);
    public static Line2D rwy3L = new Line2D.Float(1719, 6222, 7105, 768);
    public static Line2D rwy3R = new Line2D.Float(4360, 4032, 8265, 71);

    public static Line2D taxiE = new Line2D.Float(2527, 6256, 7116, 1611);
    public static Line2D taxiD = new Line2D.Float(7209, 1652, 6724, 1164);

    public static Line2D taxiA = new Line2D.Float(8792, 1187, 8777, 387);
    public static Line2D taxiA1 = new Line2D.Float(8714, 341, 8012, 344);
    public static Line2D taxiA2 = new Line2D.Float(8012, 344, 6017, 365);
    public static Line2D taxiA3 = new Line2D.Float(6017, 365, 5783, 361);


    public static ArrayList<CheckPoint> _rwy26;
    public static ArrayList<CheckPoint> _rwy35;
    public static ArrayList<CheckPoint> _rwy3L;
    public static ArrayList<CheckPoint> _rwy3R;

    public static ArrayList<CheckPoint> _taxiE;
    public static ArrayList<CheckPoint> _taxiD;

    public static ArrayList<CheckPoint> _taxiA;
    public static ArrayList<CheckPoint> _taxiA1;
    public static ArrayList<CheckPoint> _taxiA2;
    public static ArrayList<CheckPoint> _taxiA3;

    public static ArrayList<CheckPoint> common_collection;
    public static ArrayList<Intersection> intersecting_collection;

    public static void init() {

        common_collection = new ArrayList<>();

        _rwy26 = PathUtils.findPointArray(rwy26, CheckPoint.PathType.RUNWAY);
        _rwy35 = PathUtils.findPointArray(rwy35, CheckPoint.PathType.RUNWAY);
        _rwy3L = PathUtils.findPointArray(rwy3L, CheckPoint.PathType.RUNWAY);
        _rwy3R = PathUtils.findPointArray(rwy3R, CheckPoint.PathType.RUNWAY);

        _taxiE = PathUtils.findPointArray(taxiE, CheckPoint.PathType.TAXIWAY);
        _taxiD = PathUtils.findPointArray(taxiD, CheckPoint.PathType.TAXIWAY);

        _taxiA = PathUtils.findPointArray(taxiA, CheckPoint.PathType.TAXIWAY);
        _taxiA1 = PathUtils.findPointArray(taxiA1, CheckPoint.PathType.TAXIWAY);
        _taxiA2 = PathUtils.findPointArray(taxiA2, CheckPoint.PathType.TAXIWAY);
        _taxiA3 = PathUtils.findPointArray(taxiA3, CheckPoint.PathType.TAXIWAY);

        intersecting_collection = new ArrayList<>();
        PathUtils.findAllIntersectingPoints();

        intersecting_collection.forEach(i -> {

            boolean inline;

            inline = ((int)(PathUtils.getDistance(i.assignment_a.runway_reference.getP1(), i) +
                    PathUtils.getDistance(i, i.assignment_a.runway_reference.getP2())) ==
                    (int)PathUtils.getDistance(i.assignment_a.runway_reference.getP1(),
                            i.assignment_a.runway_reference.getP2()));

            System.out.println(inline);

        });

    }

    static Intersection reference;

    public static Intersection getIntersection(CheckPoint.PathAssignment a, CheckPoint.PathAssignment b) {

        reference = null;

        intersecting_collection.forEach(i -> {

            if (i.isIntersecting(a, b)) {
                reference = i;
            }

        });

        return reference;

    }

}
