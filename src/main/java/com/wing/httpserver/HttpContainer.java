package com.wing.httpserver;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

/**
 * Created by russell on 2017/5/13.
 */
public class HttpContainer implements Runnable {

    private Queue<Socket> socketQueue = null;

    private Selector readSelecor = null;
    private Selector writeSelecor = null;

    public HttpContainer(Queue<Socket> socketQueue) {
        this.socketQueue = socketQueue;
    }

    @Override
    public void run() {

    }

    private void executeCycle() throws IOException {
        try {
            takeNewSockets();
            readFromSockets();
            writeToSockets();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void takeNewSockets() throws IOException {
        Socket newSocket = this.socketQueue.poll();

        while (newSocket != null) {
            // 设置socket的ID、socketChannel
            newSocket.setSockeId(1L);
            newSocket.getSocketChannel().configureBlocking(false);

            // register the readSelector, and thie socket attached the key
            SelectionKey selectionKey = newSocket.getSocketChannel().register(this.readSelecor, SelectionKey.OP_READ);
            selectionKey.attach(newSocket);

            newSocket = this.socketQueue.poll();
        }
    }

    private void readFromSockets() throws IOException {
        int readRead = this.readSelecor.selectNow(); // 非阻塞

        if (readRead > 0) {
            Set<SelectionKey> selectionKeys = this.readSelecor.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                // 遍历读事件的key，在register的时候，socket已经attach到key上。
                Socket socket = (Socket)key.attachment();

                // 解析socket，处理HTTP请求


                keyIterator.remove();
            }

            selectionKeys.clear();
        }
    }

    private void writeToSockets() throws IOException {

    }
}
