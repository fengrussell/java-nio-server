package com.wing.httpserver;

import sun.awt.image.ByteBandedRaster;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

/**
 * Created by russell on 2017/5/13.
 */
public class HttpContainer implements Runnable {

    private Queue<Socket> socketQueue = null;

    private Selector readSelector = null;
    private Selector writeSelector = null;

    private Set<Socket> nonEmptySockets = new HashSet<>();
    private ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024);


    public HttpContainer(Queue<Socket> socketQueue) throws IOException {
        this.socketQueue = socketQueue;
        readSelector = Selector.open();
        writeSelector = Selector.open();
    }

    @Override
    public void run() {
        while(true){
            try{
                executeCycle();
            } catch(IOException e){
                e.printStackTrace();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
            SelectionKey selectionKey = newSocket.getSocketChannel().register(this.readSelector, SelectionKey.OP_READ);
            selectionKey.attach(newSocket);

            newSocket = this.socketQueue.poll();
        }
    }

    private void readFromSockets() throws IOException {
        int readRead = this.readSelector.selectNow(); // 非阻塞

        if (readRead > 0) {
            Set<SelectionKey> selectionKeys = this.readSelector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                // 遍历读事件的key，在register的时候，socket已经attach到key上。
                Socket socket = (Socket)key.attachment();

                // 解析socket，处理HTTP请求
                new HttpParser(socket).pareseHttpRequest();
                processSocket(socket);

                keyIterator.remove();
            }

            selectionKeys.clear();
        }
    }

    private void writeToSockets() throws IOException {
        int writeReady = this.writeSelector.selectNow();

        if(writeReady > 0) {
            Set<SelectionKey> selectionKeys = this.writeSelector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while(keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();

                Socket socket = (Socket) key.attachment();

                this.writeByteBuffer.clear(); // 要先clear，然后再put

                this.writeByteBuffer.put("HTTP/1.1 200 OK\r\nContent-Length: 5\r\n\r\n 404!".getBytes());
                this.writeByteBuffer.flip();

                int bytesWritten      = socket.getSocketChannel().write(writeByteBuffer);
                while(bytesWritten > 0 && writeByteBuffer.hasRemaining()){
                    this.writeByteBuffer.flip();
                    bytesWritten = socket.getSocketChannel().write(writeByteBuffer);;
                    this.writeByteBuffer.clear();
                }
                socket.setClosable(true);
                processSocket(socket);
                keyIterator.remove();
            }

            selectionKeys.clear();
        }
    }

    private void processSocket(Socket socket) throws IOException {
        if (socket.isClosable()) {
            socket.close();
        } else {
//            nonEmptySockets.add(socket);
            // 如果又要返回的数据，socket register为write
            socket.getSocketChannel().register(this.writeSelector, SelectionKey.OP_WRITE, socket);
        }
    }
}
