package main.java.resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class ImageResource {

    public static BufferedImage map_YUMA_airport;

    public static void init() {

        map_YUMA_airport = loadImage("/map/YUMA_airport_base.jpg");

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