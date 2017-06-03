package com.wing.httpserver;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by russell on 2017/5/13.
 */
public class Socket {

    // ID
    private long sockeId;

    private SocketChannel socketChannel = null;

    private boolean closable = false;

    private FileChannel fileChannel = null;

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

//    public void setClosable(boolean closable) {
//        this.closable = closable;
//    }
//
//    public boolean isClosable() {
//        return this.closable;
//    }

    public void close() throws IOException {
        if (socketChannel != null) {
            socketChannel.close();
        }
    }
}
