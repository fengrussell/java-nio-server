package com.wing.httpserver;

/**
 * Created by russell on 2017/5/14.
 */
public class HttpUtil {
    private static final byte[] GET    = new byte[]{'G','E','T'};
    private static final byte[] POST   = new byte[]{'P','O','S','T'};
    private static final byte[] PUT    = new byte[]{'P','U','T'};
    private static final byte[] HEAD   = new byte[]{'H','E','A','D'};
    private static final byte[] DELETE = new byte[]{'D','E','L','E','T','E'};

    private static final byte[] HOST           = new byte[]{'H','o','s','t'};
    private static final byte[] CONTENT_LENGTH = new byte[]{'C','o','n','t','e','n','t','-','L','e','n','g','t','h'};

    private static final String S_GET = "GET";
    private static final String S_POST = "POST";
    private static final String S_HOST = "Host";
}
