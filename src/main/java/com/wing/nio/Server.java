package com.wing.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by russell on 2017/5/7.
 */
public class Server implements Runnable {
    //
    private int port = 8000;

    private int defaultBufferSize = 1024;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        Selector selector = null;
        ServerSocketChannel serverSocketChannel = null;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(this.port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);

                    } else if (key.isReadable()) {
                        readMsg(key);

                    } else if (key.isWritable()) {

                    }
                }


            }

        } catch (Exception e) {

        }
        finally {
            try {
                selector.close();
                serverSocketChannel.socket().close();
                serverSocketChannel.close();
            } catch (Exception e) {
                // do nothing - server failed
            }
        }
    }

    private void readMsg(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel)key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);

        int bytesRead = socketChannel.read(byteBuffer);

        while (bytesRead != -1) {
            byteBuffer.flip();

            while (byteBuffer.hasRemaining()) {
                System.out.print((char) byteBuffer.get());
            }

            byteBuffer.clear(); //make buffer ready for writing
            bytesRead = socketChannel.read(byteBuffer);

        }
    }

    private void writeMsg(SelectionKey key, String msg) throws IOException {


    }

    public static void main(String[] args) {
        new Thread(new Server(8000)).start();
    }
}
