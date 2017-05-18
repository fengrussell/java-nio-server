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

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

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

    /**
     * 读取socket中的数据
     *
     * @throws IOException
     */
    private void readSocketData() throws IOException {
        SocketChannel socketChannel = this.socket.getSocketChannel();
        int bytesRead = socketChannel.read(this.byteBuffer);
        HttpHeader httpHeader = new HttpHeader();

        while (bytesRead > 0) {
            this.byteBuffer.flip();

//            while (this.byteBuffer.hasRemaining()) {
//                System.out.print((char) this.byteBuffer.get());
//            }

            // 从ByteBuffer转成byte[]的方法, ByteBuffer.get后, position会发生变化, 如果再读取需要调用clear函数
            byte[] nextBytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(nextBytes, 0, nextBytes.length);

            // 把字节流解析成HTTP头对象
            httpHeader.parseByte2HttpHeader(nextBytes);
//            System.out.println(httpHeader.toString());

            this.byteBuffer.clear(); //make buffer ready for writing
            bytesRead = socketChannel.read(this.byteBuffer);
        }

        // TODO 此处有问题，通过浏览器输入地址，可以打印出来信息，后续再请求就不停打印，死循环了。还需要抓包分析一下。
        System.out.println(httpHeader.toString());
        processHttpRequest(socket, httpHeader);

    }

    private void processHttpRequest(Socket socket, HttpHeader httpHeader) throws IOException {

        if (httpHeader.isValid()) { // 可以正确解析到HTTP头，则进一步处理HTTP请求

            // 根据请求URI来解析，查找对应的文件或执行动态类

            // 先模拟一个文件返回

        } else { // 非法的HTTP请求，之间关闭socket
            socket.setClosable(true);
//            socket.close();
        }

    }

}
