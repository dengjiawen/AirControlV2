package main.java;

import kuusisto.tinysound.TinySound;

import javax.swing.*;

public class MusicTest {

    public static void main (String... args) {

        JDialog test = new JDialog();
        test.setSize(10, 10);
        test.setVisible(true);

        TinySound.init();


    }

}
