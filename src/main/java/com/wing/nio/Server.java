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
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        int bytesRead = socketChannel.read(byteBuffer);

        // 对方发送的字节数可能超过allocate分配的长度，所以在while循环最后一行代码还需要read，直到byteRead=0时才表示一次完整的数据读取完了。
        // 注意最后有两个不可见字符（可能时回车换行，待确认）
        while (bytesRead > 0) {
            byteBuffer.flip();


            int num = 0;
            // hasRemaining是判断position是否小于limit。byteBuffer.get()执行一次postion会加1
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
