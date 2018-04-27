package main.java.logic;

import main.java.common.ThreadUtils;
import main.java.path.CheckPoint;
import main.java.path.PathUtils;
import main.java.resources.ImageResource;
import main.java.ui.RenderUtils;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

public class Airplane {

    Point2D position;
    PlaneType type;

    public CheckPoint current_checkpoint;

    public Timer incremental_update;

    float speed;

    public boolean strobe_on;
    public boolean strobe_rest;

    public boolean nav_on;
    public boolean acl_on;

    private Timer acl_light_regulator;
    private Timer strobe_regulator;

    public int wingtip_strobe_offset_x;
    public int wingtip_strobe_offset_y;

    public int wingtip_nav_offset_x;
    public int wingtip_nav_offset_y;

    public int tail_nav_offset_x;
    public int tail_nav_offset_y;

    public int top_acl_offset_x;

    public float plane_image_scale_factor;

    public Airplane() {

        type = PlaneType.BOMBARDIER_G_7000;
        position = new Point2D.Double(0, 0);

    }

    public Airplane(Point2D starting_point, CheckPoint check_point) {

        this.position = new Point2D.Double(starting_point.getX(), starting_point.getY());
        this.type = PlaneType.BOMBARDIER_G_7000;
        this.current_checkpoint = check_point;

        speed = 0.02f;

        incremental_update = new Timer(1000 / 30, e -> {
            ThreadUtils.mouse_daemon.submit(() -> {
                incrementalMovement(current_checkpoint);
                //System.out.println(current_checkpoint);
                if (current_checkpoint.getNextPoint() == null) {
                    incremental_update.stop();
                }
            });
        });

        acl_light_regulator = new Timer(1000, e -> {

            acl_on = !acl_on;
            acl_light_regulator.setDelay(
                    (acl_light_regulator.getDelay() == 500) ? 1500 : 500);

            RenderUtils.invokeRepaint();

            System.out.println(this + ", " + position);

        });
        strobe_regulator = new Timer(1000, e -> {

            if (!strobe_on && !strobe_rest) {
                strobe_on = true;
                strobe_regulator.setDelay(250);
            } else if (strobe_on && !strobe_rest) {
                if (strobe_regulator.getDelay() == 250) {
                    strobe_on = false;
                    strobe_rest = true;
                    strobe_regulator.setDelay(250);
                } else {
                    strobe_on = false;
                    strobe_rest = false;
                    strobe_regulator.setDelay(1250);
                }
            } else if (!strobe_on && strobe_rest) {
                strobe_rest = false;
                strobe_on = true;
                strobe_regulator.setDelay(251);
            }

            RenderUtils.invokeRepaint();

        });

        acl_light_regulator.start();
        strobe_regulator.start();

    }

    public BufferedImage getImage() {
        return type.plane_image;
    }

    float target_time_in_seconds;
    double dx;
    double dy;

    boolean recalc = true;

    final int tolerance = 2;

    public void incrementalMovement(CheckPoint p1) {

        if (recalc) {
            target_time_in_seconds = (1 / speed) / PathUtils.getDistance(p1, p1.getNextPoint());

            dx = (p1.getNextPoint().getX() - p1.getX()) / (target_time_in_seconds * 1000 / 30);
            dy = (p1.getNextPoint().getY() - p1.getY()) / (target_time_in_seconds * 1000 / 30);

            recalc = false;
        }

        position.setLocation(position.getX() + dx, position.getY() + dy);

        if (Math.abs(position.getX() - p1.getNextPoint().getX()) <= tolerance &&
                Math.abs(position.getY()) - p1.getNextPoint().getY() <= tolerance) {
            current_checkpoint = p1.getNextPoint();
            position.setLocation(current_checkpoint);
            recalc = true;
        }

        RenderUtils.invokeRepaint();

    }

    public void lookAhead() {

        

    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public enum PlaneType {

        BOMBARDIER_G_7000(0);

        public int type_assignment;
        public BufferedImage plane_image;

        PlaneType(int type_assignment) {
            this.type_assignment = type_assignment;
            this.plane_image = ImageResource.plane;
        }

    }

}
