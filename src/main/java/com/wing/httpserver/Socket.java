package com.wing.httpserver;

import java.nio.channels.SocketChannel;

/**
 * Created by russell on 2017/5/13.
 */
public class Socket {

    // ID
    private long sockeId;

    private SocketChannel socketChannel = null;

    public Socket(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void setSockeId(long sockeId) {
        this.sockeId = sockeId;
    }

    public long getSockeId() {
        return this.sockeId;
    }

    public SocketChannel getSocketChannel() {
        return this.socketChannel;
    }
}
