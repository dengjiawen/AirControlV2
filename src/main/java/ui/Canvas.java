package main.java.ui;

import main.java.common.ThreadUtils;
import main.java.logic.MapUtils;
import main.java.resources.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

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

        g2d.setStroke(new BasicStroke(10f));

        g2d.setColor(Color.red);
        g2d.draw(MapUtils.rwy3L);

        g2d.setColor(Color.green);
        g2d.draw(MapUtils.rwy3R);

        g2d.setColor(Color.orange);
        g2d.draw(MapUtils.rwy26);

        g2d.setColor(Color.blue);
        g2d.draw(MapUtils.rwy35);

        g2d.setColor(Color.cyan);
        g2d.draw(MapUtils.taxiE);

        g2d.setColor(Color.magenta);
        g2d.draw(MapUtils.taxiD);

        g2d.setColor(Color.lightGray);
        g2d.draw(MapUtils.taxiA);

        g2d.setColor(Color.pink);
        g2d.draw(MapUtils.taxiA1);

        g2d.setColor(Color.yellow);
        g2d.draw(MapUtils.taxiA2);

        g2d.setColor(Color.white);
        g2d.draw(MapUtils.taxiA3);

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

    public static void calcRelMousePoint () {
        rel_mouse_point_x = Window.mouse_point_x / zoom_factor - transpose_x;
        rel_mouse_point_y = Window.mouse_point_y / zoom_factor - transpose_y;

    }

    public static Point2D calcRelPoint (Point2D point) {

        Point2D rel_point = new Point2D.Float();

        rel_point.setLocation(point.getX() / zoom_factor - transpose_x,
                point.getY() / zoom_factor - transpose_y);

        return rel_point;

    }

}
