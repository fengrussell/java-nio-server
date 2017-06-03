package com.wing.httpserver.http;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by russell on 2017/6/3.
 */
public class HttpResponseHeader {

    private final String newLine = "\r\n";

    private String statusCode = "HTTP/1.1 200 OK";
    private String date = "Date:";
    private String server = "Server:wing";
    private String contentType = "Content-type:text/html";
    private String contentLength = "Content-length:";

    private int status_code = 200;
    private long content_length = 0L;

    public HttpResponseHeader(int status_code, long content_length) {
        this.status_code = status_code;
        this.content_length = content_length;
    }

    public String getResponseHeader() {
        return statusCode + newLine
                + date + getNowGMTTime() + newLine
                + server + newLine
                + contentType + newLine
                + contentLength + content_length + newLine
                + newLine; // 结束是两个\r\n
    }

    public byte[] getResponseHeaderBytes() {
        return  getResponseHeader().getBytes();
    }

    private String getNowGMTTime() {
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        return sdf.format(cd.getTime());
    }
}
