package com.wing.httpserver;

import java.io.File;
import java.net.URL;

/**
 * Created by russell on 2017/6/3.
 */
public class Config {

    private static String rootPath = "";

    private static String resourceRootPath = "";

    /**
     * 初始化
     */
    public static void init () {
        URL url = Config.class.getResource("/");
        rootPath = url.getPath();

        System.out.println("App Root Path : " + rootPath);
    }

    public static String getPathSeparator() {
        return File.separator;
    }

    public static String getRootPath() {
        return rootPath;
    }

    public static String geResourceRootPath() {
        return rootPath + "html" + getPathSeparator();
    }

    public static void main(String[] args) {
        Config.init();
        System.out.println(Config.geResourceRootPath());

        File file = new File(Config.geResourceRootPath() + "404.html");
        System.out.println(file.isDirectory());
        System.out.println(file.exists());
        System.out.println(file.length());
    }
}
