package com.wing.httpserver;

import java.nio.channels.SelectionKey;

/**
 * Created by russell on 2017/5/13.
 */
public class HttpParser {

    private Socket socket;

    public HttpParser(Socket socket) {
        this.socket = socket;
    }


}
