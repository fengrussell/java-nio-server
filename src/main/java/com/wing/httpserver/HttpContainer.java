package com.wing.httpserver;


import com.wing.httpserver.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Created by russell on 2017/5/13.
 * Modified by ruessell on 2017/5/20
 * Container的角色回归到处理Request、Response
 */
public class HttpContainer {

    /**
     * 1、简单的思路，根据Request中URL来寻找某个html文件，如果命中则返回文件流，由Response负责。如果没有找到返回404（也是一个文件）
     * @param request
     * @param response
     */
    public void service(Request request, Response response) throws IOException {
        String uri = request.getUri();

        // 解析uri，查找文件，流赋值到response

        // 先返回404html，后面再完善逻辑
        File dataFile = FileUtil.getFile(Config.geResourceRootPath() + "404.html");
        response.setDataFile(dataFile);


    }
}
