package main.java.music;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

public class MusicUtils {

    public static void init () {
        String bip = "/music/17.Calm:Alternative.ogg";

        TinySound.init();
        Music music = TinySound.loadMusic(bip, true);
        music.play(true);
    }

}
