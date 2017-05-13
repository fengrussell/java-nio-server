package com.wing.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;

/**
 * Created by russell on 2017/5/13.
 */
public class HttpConnector implements Runnable {
    // queue存储ServerSocket accepted socket
    private Queue<Socket> socketQueue;

    private int port;

    ServerSocketChannel serverSocketChannel = null;

    public HttpConnector(int port, Queue<Socket> queue) {
        this.port = port;
        this.socketQueue = queue;
    }

    @Override
    public void run() {

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            while (true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                System.out.println("Time:" + "HttpConnector accept new socket:" + socketChannel.toString());
                this.socketQueue.add(new Socket(socketChannel));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
