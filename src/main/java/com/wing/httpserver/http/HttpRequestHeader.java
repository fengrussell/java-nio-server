package com.wing.httpserver.http;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by russell on 2017/5/13.
 * 类名修改为HttpRequestHeader，为了区分Response  modified by russell on 2016/6/3
 */
public class HttpRequestHeader {
    // HTTP头关键字
    private String method = "";
    private String protocol = "";
    private String version = "";
    private String host = "";
    private String uri = "";

    // 未解析的字段都存储到这个List中
    private List<String> unparsedFileds = null;

    // 是否有效
    private boolean isValid = false;

    // 未完成解析的字节
    private byte[] remainBytes = null;


    public HttpRequestHeader() {

        this.unparsedFileds = new ArrayList<String>();
    }

    public String getMethod() {
        return this.method;
    }

    public String getUri() {
        return this.uri;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public String getVersion() {
        return this.version;
    }

    public boolean isValid() {
        return this.isValid;
    }

    @Override
    public String toString() {
        return "Method: " + this.method + ", Version: " + this.version + ", URI: " + this.uri;
    }

    /**
     * 从channel读到byte数组转化为HTTP头的关键字
     * 因为请求头的字节数超过ByteBuffer分配的大小，整条流由多个byte数组组成。所以需要保留上次byte未完成解析的byte数据，和下一个byte一起解析，直到结束。
     * 解析方法：
     * 1、remainBytes和nextBytes合成一个bytes数组，\r\n从从0开始。
     * 2、遇到连续的\r\n，则提取一个完成字符串，给关键字赋值，如果没有对应的关键字保留下来
     * 3、遇到两个连续的\r\n代表结束，或者byte遍历完
     * 4、到了bytes数组长度，发现未结束，则把这些byte存储到remainBytes中
     * @param nextBytes
     * @return
     */
    public void parseByte2HttpHeader(byte[] nextBytes) {
        byte[] bytes = null;
        if (this.remainBytes != null) {
            bytes = new byte[this.remainBytes.length + nextBytes.length];
            System.arraycopy(this.remainBytes, 0, bytes, 0, this.remainBytes.length);
            System.arraycopy(nextBytes, 0, bytes, this.remainBytes.length, nextBytes.length);

            this.remainBytes = null;
        } else {
            bytes = nextBytes;
        }

        // \r\n的位置，判断结束符
        int posR = 0;
        int posN = 0;
        int posNewField = 0;
        boolean breakline = false;

        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == '\r') {
                posR = i;
            } else if (bytes[i] == '\n') {
                posN = i;
            }

            // 如果是连续的\r\n，需要提取一个字段
            if (posN == (posR + 1)) {
                breakline = true;
                byte[] fieldBytes = new byte[posR - posNewField];

                System.arraycopy(bytes, posNewField, fieldBytes, 0, posR - posNewField);
                extractHttpHeaderFieldFromBytes(fieldBytes);

                if (posN < (bytes.length - 1)) posNewField = posN + 1;
                posN = 0;
                posR = 0;
            }
        }

        if (breakline) {
            // 不为0说明\r\n不是byte的最后的位置
            if (posNewField != 0) {
                this.remainBytes = new byte[bytes.length - posNewField];

                System.arraycopy(bytes, posNewField, this.remainBytes, 0, bytes.length - posNewField);
            } else {
                this.remainBytes = null; // byte最后两个字符为\r\n
            }
        } else {
            this.remainBytes = new byte[bytes.length];
            System.arraycopy(bytes, 0, this.remainBytes, 0, bytes.length);
        }

    }

    private void extractHttpHeaderFieldFromBytes(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return;
        String str = new String(bytes);

        if (str.indexOf("GET ") == 0) {
            int posHTTP = str.indexOf("HTTP/");
            // GET / HTTP/1.1  indexOf返回下标从0开始，HTTP/1.1前面最少会有6个字符，所以最小值为6
            if (posHTTP >= 6) {
                this.isValid = true;
                this.method = "GET";
                this.uri = str.substring(4, posHTTP-1);
                this.protocol = str.substring(posHTTP, str.length());
                this.version = str.substring(posHTTP+5, str.length());
            } else {
                return;
            }

        } else if (str.indexOf("POST ") == 0) {
            int posHTTP = str.indexOf("HTTP/");
            if (posHTTP >= 7) {
                this.isValid = true;
                this.method = "GET";
                this.uri = str.substring(5, posHTTP-1);
                this.protocol = str.substring(posHTTP, str.length());
                this.version = str.substring(posHTTP+5, str.length());
            } else {
                return;
            }
        } else if (str.indexOf("Host: ") == 0) {
            this.host = str.substring(6, str.length());
        } else { // 没有解析到的都添加到list中
            this.unparsedFileds.add(str);
        }

    }

}
