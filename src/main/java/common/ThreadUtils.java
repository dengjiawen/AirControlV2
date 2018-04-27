package main.java.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {

    public static ExecutorService mouse_daemon;
    public static ExecutorService frost_daemon;

    public static void init() {
        mouse_daemon = Executors.newCachedThreadPool();
        frost_daemon = Executors.newCachedThreadPool();
    }

}
