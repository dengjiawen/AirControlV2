package main.java;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ZoomTest extends JPanel {

    AffineTransform tx = new AffineTransform();

    Rectangle2D.Double rect = new Rectangle2D.Double(-15, -30, 30, 60);

    public ZoomTest() {
        this.addMouseWheelListener(new ZoomHandler());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        Shape shape = tx.createTransformedShape(rect);
        g2.draw(shape);
    }

    private class ZoomHandler implements MouseWheelListener {

        double scale = 1.0;

        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

                scale += (.1 * e.getWheelRotation());
                scale = Math.max(0.1, scale);
                Point p = e.getPoint();

                tx.setToIdentity();
                tx.translate(p.getX(), p.getY());
                tx.scale(scale, scale);

                ZoomTest.this.revalidate();
                ZoomTest.this.repaint();
            }
        }
    }

    public static void main(String[] args) {

        JFrame f = new JFrame("ZoomDemo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ZoomTest zoomDemo = new ZoomTest();
        f.getContentPane().add(zoomDemo);
        f.setSize(500, 500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}