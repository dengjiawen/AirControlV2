package main.java.resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class ImageResource {

    public static BufferedImage map_YUMA_airport;

    public static BufferedImage plane;
    public static BufferedImage plane_s;

    public static BufferedImage white_nav;
    public static BufferedImage red_nav;
    public static BufferedImage green_nav;
    public static BufferedImage red_nav_intense;

    public static void init() {

        map_YUMA_airport = loadImage("/map/YUMA_airport_base.jpg");
        plane = loadImage("/planes/bombardier_global_7000.png");
        plane_s = loadImage("/planes/bombardier_global_7000_s.png");

        white_nav = loadImage("/luminence/white.png");
        red_nav = loadImage("/luminence/red.png");
        green_nav = loadImage("/luminence/green.png");
        red_nav_intense = loadImage("/luminence/intense_red.png");

    }

    private static BufferedImage loadImage(String res_path) {

        try {
            return ImageIO.read(ImageResource.class.getResource(res_path));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Resources are missing. " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(100);
        }
        return null;
    }

}