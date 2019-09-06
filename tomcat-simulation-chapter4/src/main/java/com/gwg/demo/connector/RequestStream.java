package com.gwg.demo.connector;

import com.gwg.demo.connector.http.HttpRequest;
import com.gwg.demo.connector.http.HttpResponse;
import jdk.internal.util.xml.impl.Input;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RequestStream extends ServletInputStream{

    //----------------------------------------成员变量
    //这个流已经关闭
    protected boolean closed = false;
    //我们是否应该在刷新时提交响应
    private boolean commit = false;
    //这个流已经返回的字节数
    protected int count = 0;
    //contentLength请求体的长度，如果没有定义内容长度，则为-1。
    protected int length = -1;
    //与这个InputStream关联的Response
    protected HttpResponse response = null;
    //我们应该写入数据的底层输出流。
    protected InputStream input = null;

    //----------------------------------------构造函数
    /**
     * 构造一个ServletInputStream与指定的Request关联
     * @param request
     */
    public RequestStream(HttpRequest request) throws IOException {
        super();
        closed = false;
        commit = false;
        count = 0;
        length = request.getContentLength();
        input = request.getStream();
    }

    public void setCommit(boolean commit) {
        this.commit = commit;
    }

    public boolean isCommit() {
        return commit;
    }

    @Override
    public void close() throws IOException {
        if(closed){
            throw new IOException("responseStream.close.closed");
        }
        response.flushBuffer();
        closed = true;
    }

    /**
     * 刷新此输出流的所有缓冲数据，这也会导致提交响应。
     * @throws IOException
     */
    public void flush() throws IOException{
        if(closed){
            throw new IOException("responseStream.flush.closed");
        }
        if(commit){
            response.flushBuffer();
        }
    }

    /**
     * 将指定的字节写入输出流。
     * @param b
     * @throws IOException
     */
    public void write(int b) throws IOException{
        if(closed){
            throw new IOException("responseStream.write.closed");
        }
        if(length > 0 && count >= length){
            throw new IOException("responseStream.write.count");
        }
        response.write(b);

    }

    /**
     * 功能1：读取请求体
     *
     */
    public int read(byte b[], int offset, int len) throws IOException {
        int toRead = len;
        if(length > 0){
            if(count >= length){//如果从输入流InputStream读取的字节数大于content-length,则直接返回
                return -1;
            }
            if((count + len) > length){//输入流InputStream读取的字节数count + 尝试从输入流InputStream读取的字节数len > contentLength, 则toRead = contentLength - count;
                toRead = length -count;
            }
        }
        int actuallyRead = super.read(b, offset, toRead);
        return actuallyRead;
    }


    //------------------------------------------公共方法

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }

    @Override
    public int read() throws IOException {
        return 0;
    }
}
