package com.wing.httpserver;

import com.wing.httpserver.http.HttpRequestHeader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by russell on 2017/5/20.
 */
public class Request {
    //
    private Socket socket;

    //
    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    private HttpRequestHeader reqHeader = null;

    private Response response = null;

    public Request(Socket socket) {
        this.socket = socket;
        this.reqHeader = new HttpRequestHeader();
    }

    public void parseRequest() throws IOException {
        SocketChannel socketChannel = this.socket.getSocketChannel();
        int bytesRead = socketChannel.read(this.byteBuffer);

        while (bytesRead > 0) {
            this.byteBuffer.flip();

//            while (this.byteBuffer.hasRemaining()) {
//                System.out.print((char) this.byteBuffer.get());
//            }

            // 从ByteBuffer转成byte[]的方法, ByteBuffer.get后, position会发生变化, 如果再读取需要调用clear函数
            byte[] nextBytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(nextBytes, 0, nextBytes.length);

            // 把字节流解析成HTTP头对象
            reqHeader.parseByte2HttpHeader(nextBytes);
//            System.out.println(httpHeader.toString());

            this.byteBuffer.clear(); //make buffer ready for writing
            bytesRead = socketChannel.read(this.byteBuffer);
        }

        // TODO 此处有问题，通过浏览器输入地址，可以打印出来信息，后续再请求就不停打印，死循环了。还需要抓包分析一下。
        System.out.println(reqHeader.toString());
    }

    public boolean isValidHttpRequest() {
        return this.reqHeader.isValid();
    }

    public String getMethond() {
        return this.reqHeader.getMethod();
    }

    public String getUri() {
        return this.reqHeader.getUri();
    }

    public String getProtocol() {
        return this.reqHeader.getProtocol();
    }

    public String getProtocolVersion() {
        return this.reqHeader.getVersion();
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
