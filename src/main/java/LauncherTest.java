package main.java;

import main.java.common.ThreadUtils;
import main.java.logic.RefUtils;
import main.java.music.MusicUtils;
import main.java.path.MapUtils;
import main.java.resources.FontResource;
import main.java.resources.ImageResource;
import main.java.ui.RenderUtils;
import main.java.ui.Window;

public class LauncherTest {

    static {
        ImageResource.init();
        FontResource.init();
        MapUtils.init();
        ThreadUtils.init();
        MusicUtils.init();
        RefUtils.init();
    }

    public static void main (String... args) {

        Window window = new Window();

        RenderUtils.init();

    }

}
