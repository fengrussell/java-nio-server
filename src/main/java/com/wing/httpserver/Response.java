package com.wing.httpserver;

/**
 * Created by russell on 2017/5/20.
 */
public class Response {
    //
    private Socket socket;

    private Request request = null;


    public Response (Socket socket) {
        this.socket = socket;
    }

    public void setRequest(Request request) {
        this.request =  request;
    }

    public Request getRequest() {
        return this.request;
    }
}
