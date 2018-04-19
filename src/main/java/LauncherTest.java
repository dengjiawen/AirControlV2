package main.java;

import main.java.common.ThreadUtils;
import main.java.resources.FontResource;
import main.java.resources.ImageResource;
import main.java.ui.RenderUtils;
import main.java.ui.Window;

public class LauncherTest {

    public static void main (String... args) {

        ImageResource.init();
        FontResource.init();

        ThreadUtils.init();

        Window window = new Window();

        RenderUtils.init();

    }

}
