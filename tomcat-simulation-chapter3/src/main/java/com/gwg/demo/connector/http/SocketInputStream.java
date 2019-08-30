package com.gwg.demo.connector.http;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 解析HTTP请求
 */
public class SocketInputStream {

    //---------------------------------------------常亮

    /**
     * CR
     */
    public static final byte CR = (byte) '\r';

    /**
     * LF
     */
    public static final byte LF = (byte) '\n';

    /**
     * SP
     */
    public static final byte SP = (byte)' ';

    /**
     * HT
     */
    private static final byte HT= (byte) '\t';

    /**
     * COLON
     */
    private static final byte COLON = (byte) ':';


    /**
     *
     */
    private static final int LC_OFFSET = 'A'-'a';


    protected byte buf[];

    protected int count;

    /**
     * 缓存位置
     */
    protected int pos;

    /**
     * 分析的输入流
     */
    protected InputStream is;



    /**
     *
     * @param inputStream
     * @param bufferSize
     */
    public SocketInputStream(InputStream inputStream, int bufferSize){

        this.is = is;
        this.buf = new byte[bufferSize];
    }


    protected static StringManager sm = StringManager.getManager("com.gwg.demo.connector.http");

    /**
     * readRequestLine()方法会返回一个HTTP请求中第一行的内容，即包含了URI,请求方法和HTTP版本信息的内容。由于从套接字的输入流中处理字节流是指读取从第1个字节到
     *最后一个字节(无法从后往前读取)的内容。因此readRequestLine()方法必须在readHeader()方法之前调用。
     * @param requestLine
     * @throws IOException
     */
    public HttpRequestLine readRequestLine(HttpRequestLine requestLine) throws IOException{
        //回收检查
        if(requestLine.methodEnd != 0){
            requestLine.recycle();
        }
        //检查空行
        int chr = 0;
        do{
            try {
                chr = read();
            } catch (IOException e) {
                chr = -1;
            }
        }while((chr == CR) || (chr == LF));

        if(chr == -1){
            throw new EOFException(sm.getString("requestStream.readLine.error"));
        }
        pos--;
        //读取请求方法名称
        int maxRead = requestLine.method.length;
        int readStart = pos;
        int readCount = 0;

        boolean space = false;

        while()



    }

    /**
     * 每次调用readHeader()方法都会返回一个名/值对，所以应重复调用readHeader()，直到读取了所有的请求头信息
     */
    public void readHeader(HttpHeader httpHeader){
        //回收检查
        if(httpHeader.nameEnd != 0){

            httpHeader.recycle();
        }

        int chr = read();




    }

    public int read() throws IOException {
        if(pos >= count){
            fill();//填充buf[]
            if(pos >= count){
                return -1;
            }
        }
        return buf[pos++] & 0xff;
    }

    /**
     * 使用底层输入流中的数据填充内部缓冲区。
     */
    public void fill() throws IOException {
        pos = 0;
        count = 0;
        int nRead = is.read(buf, 0 ,buf.length);
        if(nRead > 0 ){
            count = nRead;
        }

    }
}
