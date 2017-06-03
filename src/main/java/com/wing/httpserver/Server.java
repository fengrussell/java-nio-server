package com.wing.httpserver;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by russell on 2017/5/13.
 */
public class Server {

    private HttpConnector httpConnector = null;
    private HttpProcessor httpProcessor = null;

    private int port;

    public Server(int port) {

        this.port = port;
     }

    public void start() throws IOException {
        Config.init();
        Queue<Socket> socketQueue = new ArrayBlockingQueue<Socket>(1024);
        this.httpConnector = new HttpConnector(port,socketQueue);
        this.httpProcessor = new HttpProcessor(socketQueue, new HttpContainer());

        new Thread(this.httpConnector).start();
        new Thread(this.httpProcessor).start();
    }

    public static void main(String[] args) throws IOException {
        new Server(8080).start();
    }
}
