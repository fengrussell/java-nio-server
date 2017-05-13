package com.wing.httpserver;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by russell on 2017/5/13.
 */
public class Server {

    private HttpConnector httpConnector = null;
    private HttpContainer httpContainer = null;
    private int port;

    public Server(int port) {
        this.port = port;
     }

    public void start() {
        Queue<Socket> socketQueue = new ArrayBlockingQueue<Socket>(1024);
        httpConnector = new HttpConnector(port,socketQueue);

    }
}
