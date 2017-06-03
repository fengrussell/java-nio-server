package com.wing.httpserver;

import com.sun.org.apache.regexp.internal.RE;
import com.wing.httpserver.http.HttpResponseHeader;

import java.io.File;
import java.net.URL;
import java.nio.channels.FileChannel;

/**
 * Created by russell on 2017/5/20.
 */
public class Response {
    //
    private Socket socket;

    private Request request = null;

    private HttpResponseHeader responseHeader = null;

    private File dataFile = null;

    public Response (Socket socket) {
        this.socket = socket;
    }

    public void setRequest(Request request) {
        this.request =  request;
    }

    public Request getRequest() {
        return this.request;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public HttpResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(HttpResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public File getDataFile() {
        return dataFile;
    }

    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }

    public boolean isReturnData() {
        if (this.dataFile == null) {
            return false;
        } else {
            return true;
        }
    }
}
