package com.wing.httpserver;

import com.sun.org.apache.regexp.internal.RE;
import com.wing.httpserver.http.HttpResponseHeader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

/**
 * Created by russell on 2017/5/20.
 */
public class HttpProcessor implements Runnable {
    // Socket队列
    private Queue<Socket> socketQueue = null;

    // read write 分别两个Selector
    private Selector readSelector = null;
    private Selector writeSelector = null;
    // 容器
    private HttpContainer httpContainer = null;

    private Set<Socket> nonEmptySockets = new HashSet<>();
    private ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024);

    public HttpProcessor(Queue<Socket> socketQueue, HttpContainer httpContainer) throws IOException {
        this.socketQueue = socketQueue;
        this.httpContainer = httpContainer;
        this.readSelector = Selector.open();
        this.writeSelector = Selector.open();
    }

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
            processAcceptedSockets();
            readFromSockets();
            writeToSockets();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理接收到的Sokcet，从Queue中获取socketChannel，然后注册到readSelector
     * @throws IOException
     */
    private void processAcceptedSockets(){
        Socket socket = this.socketQueue.poll();

        SelectionKey selectionKey = null;
        while (socket != null) {
            try {
                // 设置socket的ID、socketChannel
                socket.setSockeId(1L);
                socket.getSocketChannel().configureBlocking(false);

                // register the readSelector, and thie socket attached the key
                selectionKey = socket.getSocketChannel().register(this.readSelector, SelectionKey.OP_READ);
                selectionKey.attach(socket);
            }
            catch (IOException e) {
                try {
                    selectionKey.attach(null);
                    selectionKey.cancel();
                    socket.getSocketChannel().close();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            socket = this.socketQueue.poll();
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

                try {

                    // 解析socket，处理HTTP请求
                    process(socket);

                } catch (IOException e) {
                    try {
                        key.attach(null);
                        key.cancel();
                        socket.getSocketChannel().close();
                    }
                    catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

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

                // 在write的时候attack的是Response，socket是Response的变量
                Response response = (Response)key.attachment();
                Socket socket = response.getSocket();

                File htmlFile = response.getDataFile();
                // 1、创建HTTP应答头
                HttpResponseHeader respHeader = new HttpResponseHeader(200, htmlFile.length());
                response.setResponseHeader(respHeader);

                // 2、打开要返回内容的文件
                FileInputStream fis = new FileInputStream(htmlFile);
                FileChannel fileChannel = fis.getChannel();
                ByteBuffer buf = ByteBuffer.allocate(1024);

                try {
                    // 3、开始向buffer写入数据
                    // 3.1、先写应答头
                    // 3.2、再写文件的内容
                    // TODO 这个逻辑有问题，如果文件过大有异常
                    this.writeByteBuffer.clear(); // 要先clear，然后再put

                    // 写入200 OK 应答头
                    this.writeByteBuffer.put(respHeader.getResponseHeaderBytes());
                    // 写入file流
                    while (fileChannel.read(buf) != -1) {
                        writeByteBuffer.put(buf);
                        buf.clear();
                    }

                    this.writeByteBuffer.flip();

                    int bytesWritten      = socket.getSocketChannel().write(writeByteBuffer);
                    while(bytesWritten > 0 && writeByteBuffer.hasRemaining()){
                        this.writeByteBuffer.flip();
                        bytesWritten = socket.getSocketChannel().write(writeByteBuffer);;
                        this.writeByteBuffer.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 4、相关的close
                    fileChannel.close();
                    fis.close();
                }
                socket.close();

                keyIterator.remove();
            }

            selectionKeys.clear();
        }
    }

    // 处理socket
    // 1、解析请求，判断是否是合法的HTTP请求
    // 2、HTTP -> Request
    private void process(Socket socket) throws IOException {
        Request request = new Request(socket);
        Response response = new Response(socket);

        request.setResponse(response);
        response.setRequest(request);

        request.parseRequest();

        // 不是有效的http请求头，直接关闭socket
        if (!request.isValidHttpRequest()) {
            socket.close(); // 改成抛出Exception，由上层关闭
        } else {
            this.httpContainer.service(request, response);
            // 如果reponse有数据要返回，则加入到写数据的queue，由Q再register到writeSelector
            if (response.isReturnData()) {
                // 进入到writer模式，用队列的方式。当前直接register
                socket.getSocketChannel().register(this.writeSelector, SelectionKey.OP_WRITE, response);
            } else {
                // 可以关闭socket
                socket.close();
            }
        }
    }

}
