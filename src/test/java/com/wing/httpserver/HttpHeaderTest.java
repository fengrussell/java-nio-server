package com.wing.httpserver; 

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* HttpHeader Tester. 
* 
* @author <Authors name> 
* @since <pre></pre>
* @version 1.0 
*/ 
public class HttpHeaderTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: extractHttpHeaderFieldFromBytes(byte[] bytes) 
* 
*/ 
@Test
public void testExtractHttpHeaderFieldFromBytes() throws Exception {

    // --------------- Test Case 1 ----------------
    // GET / HTTP/1.1   长度14(不包含\r\n)
    byte[] bytes1_1 = {'G', 'E', 'T', ' ', '/', ' ', 'H', 'T', 'T', 'P', '/', '1', '.', '1', '\r', '\n'};
    // Host: 127.0.0.1:8080    长度20(不包含\r\n)
    byte[] bytes1_2 = {'H', 'o', 's', 't', ':', ' ', '1', '2', '7', '.', '0', '.', '0', '.', '1', ':', '8', '0', '8', '0', '\r', '\n'};

    HttpHeader httpHeader1 = new HttpHeader();
    httpHeader1.parseByte2HttpHeader(bytes1_1);
    httpHeader1.parseByte2HttpHeader(bytes1_2);

    Assert.assertEquals("GET", httpHeader1.getMethod());

    // --------------- Test Case 2 ----------------
    // GET / HTTP/1.1\r\nHos   长度19(包含\r\n)
    byte[] bytes2_1 = {'G', 'E', 'T', ' ', '/', ' ', 'H', 'T', 'T', 'P', '/', '1', '.', '1', '\r', '\n', 'H', 'o', 's'};
    // t: 127.0.0.1:8080    长度19(包含\r\n)
    byte[] bytes2_2 = {'t', ':', ' ', '1', '2', '7', '.', '0', '.', '0', '.', '1', ':', '8', '0', '8', '0', '\r', '\n'};

    HttpHeader httpHeader2 = new HttpHeader();
    httpHeader2.parseByte2HttpHeader(bytes2_1);
    httpHeader2.parseByte2HttpHeader(bytes2_2);

    Assert.assertEquals("GET", httpHeader2.getMethod());

    // --------------- Test Case 2 ----------------
    // GET / HTTP/1.1\r   长度15(包含\r\n)
    byte[] bytes3_1 = {'G', 'E', 'T', ' ', '/', ' ', 'H', 'T', 'T', 'P', '/', '1', '.', '1', '\r'};

    byte[] bytes3_2 = {'\n', 'H', 'o', 's'};
    // t: 127.0.0.1:8080    长度19(包含\r\n)
    byte[] bytes3_3 = {'t', ':', ' ', '1', '2', '7', '.', '0', '.', '0', '.', '1', ':', '8', '0', '8', '0', '\r', '\n'};


    HttpHeader httpHeader3 = new HttpHeader();
    httpHeader3.parseByte2HttpHeader(bytes3_1);
    httpHeader3.parseByte2HttpHeader(bytes3_2);
    httpHeader3.parseByte2HttpHeader(bytes3_3);

    Assert.assertEquals("GET", httpHeader3.getMethod());

//TODO: Test goes here... 
/* 
try { 
   Method method = HttpHeader.getClass().getMethod("extractHttpHeaderFieldFromBytes", byte[].class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

} 
