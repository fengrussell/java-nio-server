package com.wing.httpserver.util;

import java.io.File;

/**
 * Created by russell on 2017/6/3.
 */
public class FileUtil {

    /**
     * 一个文件的路径，判断是否存在
     * @param filePath
     * @return
     */
    public static boolean exists(String filePath) {
        File file = new File(filePath);
        return exists(file);
    }

    /**
     * 封装File的方法
     * @param file
     * @return
     */
    public static boolean exists(File file) {
        if (file != null) {
            return file.exists();
        } else {
            return false;
        }
    }

    /**
     * 一个文件路径，返回一个File对象
     * @param filePath
     * @return
     */
    public static File getFile(String filePath) {
        return new File(filePath);
    }
}
