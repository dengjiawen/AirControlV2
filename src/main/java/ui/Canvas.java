package main.java.ui;

import main.java.common.ThreadUtils;
import main.java.logic.Airplane;
import main.java.logic.RefUtils;
import main.java.path.CheckPoint;
import main.java.path.MapUtils;
import main.java.resources.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Canvas extends JPanel {

    public static Canvas current_canvas_reference;

    public static float zoom_factor = 1f / 7f;
    public static float transpose_x;
    public static float transpose_y;

    public static float rel_mouse_point_x;
    public static float rel_mouse_point_y;

    public Canvas() {

        super();

        transpose_x = 0f;
        transpose_y = (getHeight() - ImageResource.map_YUMA_airport.getHeight() * zoom_factor) / 2;

        setBounds(0, 0,
                ImageResource.map_YUMA_airport.getWidth(),
                ImageResource.map_YUMA_airport.getHeight());

        current_canvas_reference = this;

        writeScreenForFrost();
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        RenderUtils.applyQualityRenderingHints(g2d);
        calcRelMousePoint();

        AffineTransform transform = new AffineTransform();
        transform.scale(zoom_factor, zoom_factor);
        transform.translate(transpose_x, transpose_y);

        g2d.transform(transform);

        g2d.drawImage(ImageResource.map_YUMA_airport, 0, 0, getWidth(), getHeight(), this);

        paintPath(MapUtils._rwy26, Color.red, g2d);
        paintPath(MapUtils._rwy35, Color.green, g2d);
        paintPath(MapUtils._rwy3L, Color.orange, g2d);
        paintPath(MapUtils._rwy3R, Color.blue, g2d);

        paintPath(MapUtils._taxiE, Color.cyan, g2d);
        paintPath(MapUtils._taxiD, Color.magenta, g2d);

        paintPath(MapUtils._taxiA, Color.darkGray, g2d);
        paintPath(MapUtils._taxiA1, Color.pink, g2d);
        paintPath(MapUtils._taxiA2, Color.yellow, g2d);
        paintPath(MapUtils._taxiA3, Color.white, g2d);

        g2d.setStroke(new BasicStroke(5f));
        g2d.setColor(Color.red);

        MapUtils.intersecting_collection.forEach(i -> {

            g2d.fillOval((int) i.getX(), (int)i.getY(), 20, 20);

        });

        RefUtils.planes.forEach((i, p) -> {
            paintPlane(p, g2d);
        });

    }

    protected void paintPath(ArrayList<CheckPoint> points, Color color, Graphics2D g2d) {

        g2d.setColor(color);
        points.forEach(p -> {
            g2d.fillOval((int) p.getX(), (int) p.getY(), 10, 10);
        });

    }

    protected void paintPlane(Airplane plane, Graphics2D g2d) {

        AffineTransform original = g2d.getTransform();
        g2d.rotate(Math.PI + plane.current_heading, plane.getX(), plane.getY());

        g2d.drawImage(ImageResource.plane_s,
                (int)(plane.getX() - 10 - (ImageResource.plane.getWidth() * 0.1)/2),
                (int)(plane.getY() + 3 - (ImageResource.plane.getHeight() * 0.1)/2),
                (int)(ImageResource.plane.getWidth() * 0.1), (int)(ImageResource.plane.getHeight() * 0.1), this);

        g2d.drawImage(ImageResource.plane,
                (int)(plane.getX() - (ImageResource.plane.getWidth() * 0.1)/2),
                (int)(plane.getY() - (ImageResource.plane.getHeight() * 0.1)/2),
                (int)(ImageResource.plane.getWidth() * 0.1), (int)(ImageResource.plane.getHeight() * 0.1), this);

        g2d.drawLine((int)plane.getX(), (int)(plane.getY() - (ImageResource.plane.getHeight() * 0.1)/2),
                (int)plane.getX(), (int)(plane.getY() + (ImageResource.plane.getHeight() * 0.1)/2));

        g2d.drawOval((int)plane.getX() - 20, (int)(plane.getY() - (ImageResource.plane.getHeight() * 0.1)/2) - 20, 40, 40);

//        if (!plane.strobe_on) {
//            g2d.drawImage(ImageResource.white_nav,
//                    (int) (plane.getX() - (ImageResource.plane.getWidth() * 0.1) / 2 - 30),
//                    (int) (plane.getY()),
//                    (int) (ImageResource.white_nav.getWidth() * 0.5),
//                    (int) (ImageResource.white_nav.getWidth() * 0.5), this);
//            g2d.drawImage(ImageResource.white_nav,
//                    (int) (plane.getX() + (ImageResource.plane.getWidth() * 0.1) / 2 - 30),
//                    (int) (plane.getY()),
//                    (int) (ImageResource.white_nav.getWidth() * 0.5),
//                    (int) (ImageResource.white_nav.getHeight() * 0.5), this);
//        }
//
//        if (!plane.acl_on) {
//            g2d.drawImage(ImageResource.red_nav,
//                    (int) (plane.getX() - (ImageResource.plane.getWidth() * 0.1) / 2 + 15),
//                    (int) (plane.getY() - 30),
//                    (int) (ImageResource.red_nav_intense.getWidth() * 0.5),
//                    (int) (ImageResource.red_nav_intense.getHeight() * 0.5), this);
//        }

        g2d.setTransform(original);

    }

    protected static void writeScreenForFrost() {

        final Area clipped_area = new Area(
                new Rectangle2D.Double(0, 0, Window.window_width, Window.window_height));
        clipped_area.subtract(new Area(new Rectangle2D.Double(525, 0, 500, 756)));

        if (!current_canvas_reference.isVisible()) return;

        ThreadUtils.frost_daemon.submit(() -> {
            FrostedPane.canvas_image_buffer = new BufferedImage(Window.window_width, Window.window_height, BufferedImage.TYPE_INT_RGB);
            Graphics g = FrostedPane.canvas_image_buffer.getGraphics();
            g.setClip(clipped_area);
            current_canvas_reference.paintComponent(g);

            g.dispose();

            FrostedPane.canvas_active_image = FrostedPane.canvas_image_buffer;
            FrostedPane.current_active_panes.forEach(e -> {
                e.updateBlurImage();
            });
        });

    }

    public static void calcRelMousePoint() {
        rel_mouse_point_x = Window.mouse_point_x / zoom_factor - transpose_x;
        rel_mouse_point_y = Window.mouse_point_y / zoom_factor - transpose_y;

    }

    public static Point2D calcRelPoint(Point2D point) {

        Point2D rel_point = new Point2D.Float();

        rel_point.setLocation(point.getX() / zoom_factor - transpose_x,
                point.getY() / zoom_factor - transpose_y);

        return rel_point;

    }

}
