package main.java;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

import javax.swing.*;
import java.io.File;

public class MusicTest {

    public static void main (String... args) {

        String bip = "/music/17.Calm:Alternative.ogg";

        JDialog test = new JDialog();
        test.setSize(10, 10);
        test.setVisible(true);

       TinySound.init();
       Music music = TinySound.loadMusic(bip, true);
       music.play(true);


    }

}
