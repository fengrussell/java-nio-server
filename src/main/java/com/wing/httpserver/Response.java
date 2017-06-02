package com.wing.httpserver;

import java.nio.channels.FileChannel;

/**
 * Created by russell on 2017/5/20.
 */
public class Response {
    //
    private Socket socket;

    private Request request = null;

    private FileChannel fileChannel = null;

    public Response (Socket socket) {
        this.socket = socket;
    }

    public void setRequest(Request request) {
        this.request =  request;
    }

    public Request getRequest() {
        return this.request;
    }

    public void setFileChannel(FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }

    public FileChannel getFileChannel() {
        return this.fileChannel;
    }

    public boolean isReturnData() {
        if (this.fileChannel == null) {
            return false;
        } else {
            return true;
        }
    }
}
