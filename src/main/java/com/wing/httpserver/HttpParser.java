package com.wing.httpserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by russell on 2017/5/13.
 */
public class HttpParser {


    private Socket socket;

    private ByteBuffer byteBuffer = ByteBuffer.allocate(10);

    public HttpParser(Socket socket) {
        this.socket = socket;
    }

    public void pareseHttpRequest() {
        try {
            readSocketData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readSocketData() throws IOException {
        SocketChannel socketChannel = this.socket.getSocketChannel();
        int bytesRead = socketChannel.read(this.byteBuffer);
        HttpHeader httpHeader = new HttpHeader();

        while (bytesRead > 0) {
            this.byteBuffer.flip();

//            int num = 0;
//            while (this.byteBuffer.hasRemaining()) {
//                System.out.print((char) this.byteBuffer.get());
//            }

            // 从ByteBuffer转成byte[]的方法, ByteBuffer.get后, position会发生变化, 如果再读取需要调用clear函数
            byte[] nextBytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(nextBytes, 0, nextBytes.length);

            // 把字节流解析成HTTP头对象
            httpHeader.parseByte2HttpHeader(nextBytes);
            System.out.println(httpHeader.toString());
//            System.out.println(new String(bytes));

            this.byteBuffer.clear(); //make buffer ready for writing
            bytesRead = socketChannel.read(this.byteBuffer);
        }

    }


}
