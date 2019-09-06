package com.gwg.demo.connector.http;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 解析HTTP请求
 */
public class SocketInputStream extends InputStream{

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
     * SP 空格，一个字符
     */
    public static final byte SP = (byte)' ';

    /**
     * HT \t表示空四个字符，也称缩进
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


    //内部缓冲区buffer
    protected byte buf[];

    //标识内部缓冲区buffer的有效字节数
    protected int count;

    /**
     * 读取内部缓冲区的位置
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
     * 最后一个字节(无法从后往前读取)的内容。因此readRequestLine()方法必须在readHeader()方法之前调用。
     *
     * 流程：读取 请求方法名称 -> 读取 请求地址uri -> 读取 协议protocol
     *
     * HTTP请求中第一行的格式如下： POST http://www.baidu.com/index.html HTTP/1.1
     * @param requestLine
     * @throws IOException
     */
    public void readRequestLine(HttpRequestLine requestLine) throws IOException{
        //回收检查
        if(requestLine.methodEnd != 0){
            requestLine.recycle();
        }
        //检查空行
        int chr = 0;
        do{
            try {
                chr = read();//填充内部缓冲buffer
            } catch (IOException e) {
                chr = -1;
            }
        }while((chr == CR) || (chr == LF));

        if(chr == -1){
            throw new EOFException(sm.getString("requestStream.readLine.error"));
        }
        pos--;
        //1.读取请求方法名称
        int maxRead = requestLine.method.length;
        int readStart = pos;
        int readCount = 0;

        boolean space = false;


        while(!space){
            //如果存储请求方法名字的数组(即HttpRequestLine中的成员变量char[] method)太小了，则扩充该数组,扩充一倍原数组大小。
            if(readCount > maxRead){
                if((maxRead * 2) <= HttpRequestLine.MAX_METHOD_SIZE){
                    char[] newBuffer = new char[maxRead * 2];
                    //将原数组数据复制到新数据
                    System.arraycopy(requestLine.method, 0, newBuffer, 0, maxRead);
                    requestLine.method = newBuffer;
                    maxRead = requestLine.method.length;
                }else{
                    throw new IOException(sm.getString("requestStream.readline.toolong"));
                }
            }
            //如果我们在内部缓冲区的末端，即内存缓冲区buffer中的有效字节已经全部读取了
            if(pos > count){
                int val = read();//再次填充内部缓冲区buffer，返回第一个字节的int值
                if(val == -1){//如果没有从输入流读取到数据
                    throw new IOException(sm.getString("requestStream.readline.error"));
                }
                pos = 0;
                readStart = 0;
            }
            if(buf[pos] == SP){//如果遇到了空格
                space = true;
            }
            //获取请求方法名字
            requestLine.method[readCount] = (char)buf[pos];
            readCount++;
            pos++;

        }
        requestLine.methodEnd = readCount -1;

        //2.读取uri
        maxRead = requestLine.uri.length;
        readStart = pos;
        readCount = 0;

        space = false;

        while(!space){
            //如果存储uri的字符数组(即 HttpRequestLine中char[] uri)容量不够，则扩充该字符数组，在原数组大小的基础上扩充一倍
            if(readCount > maxRead){
                if((maxRead * 2) <= HttpRequestLine.MAX_URI_SIZE){
                    char[] newBuffer = new char[maxRead * 2];
                    System.arraycopy(requestLine.method, 0, newBuffer, 0, maxRead);
                    requestLine.uri = newBuffer;
                    maxRead = requestLine.uri.length;
                }
            }
            //如果内部缓存buffer的有效字节已经全部读取了
            if(pos > count){
                int val = read();
                if(val == -1){
                    throw new IOException(sm.getString("requestStream.readline.error"));
                }
                pos = 0;
                readStart = 0;
            }

            if(buf[pos] == SP){//如果遇到了空格
                space = true;
            }

            //获取uri
            requestLine.uri[readCount] = (char)buf[pos];
            readCount++;
            pos++;
        }

        requestLine.uriEnd = readCount - 1;





        //3.读取协议protocol
        maxRead = requestLine.protocol.length;
        readStart = pos;
        readCount = 0;

        //标识是不是行结尾
        boolean eol = false;

        while(!eol){

            //如果存储protocol的字符数组容量不够用，则扩容，在原字符数组容量的基础上扩充一倍
            if(readCount > maxRead){
                if((maxRead * 2) <= HttpRequestLine.MAX_PROTOCOL_SIZE){
                    char[] newBuffer = new char[maxRead * 2];
                    System.arraycopy(requestLine.protocol, 0 , newBuffer, 0, maxRead);
                    requestLine.protocol = newBuffer;
                    maxRead = requestLine.protocol.length;
                }
            }
            if(pos > count){
                int val = read();
                if(val == -1){
                    throw new IOException(sm.getString("requestStream.readline.error"));
                }
                pos = 0;
                readStart = 0;
            }
            if(buf[pos] == CR){
                //跳过，什么都不执行,字符'\n'才是标识行末尾
            }else if(buf[pos] == LF){//标识到达行末尾了
                eol = true;
            }else{
                requestLine.protocol[readCount] = (char)buf[pos];
                readCount++;
            }
            pos++;
        }
        requestLine.protocolEnd = readCount;
    }

    /**
     * HTTP请求报文由3部分组成：请求行+请求头+请求体
     * 每次调用readHeader()会解析请求头中的一行，该方法都会返回一个名/值对且名/值对封装在HttpHeader之中，所以应重复调用readHeader()，直到读取了所有的请求头信息
     *
     * Http请求头的格式： Content-Type: application/json ， 注意冒号后面会有空格
     */
    public void readHeader(HttpHeader httpHeader) throws IOException {
        //检查重置，循环使用，解析请求头下一行用
        if(httpHeader.nameEnd != 0){
            httpHeader.recycle();
        }

        //前提是要缓存区buffer内的有效字节全部读完了，读取填充内置缓冲区，使用count来标识内置缓冲区buffer有效字节数
        int chr = read();

        //1.检查空行，空行是分割请求头header与body的
        if(chr == CR || chr == LF){

        }

        //2.读取请求头header的名
        int maxRead = httpHeader.name.length;
        int readStart = pos;
        int readCount = 0;

        boolean colon = false;//冒号
        while(!colon){
            //1.如果存储请求头名的字符数组(即HttpHeader中的char[] name)容量不够，则扩容，在原容量基础上扩容一倍
            if(readCount > maxRead){
                if((maxRead * 2) < HttpHeader.MAX_NAME_SIZE){
                    char[] newBuffer = new char[maxRead * 2];
                    System.arraycopy(httpHeader.name, 0 , newBuffer, 0, maxRead);
                    httpHeader.name = newBuffer;
                    maxRead = httpHeader.name.length;
                }
            }
            //2.判断内部缓冲区有效字节数count是否全部读取分析完了
            if(pos > count){
                int val = read();
                if(val == -1){
                    throw new IOException(sm.getString(""));//IO异常
                }
                pos = 0;
                readStart = 0;
            }
            //3.判断有没有读到冒号
            if(buf[pos] == COLON){
                colon = true;
            }

            //4.大写字母转小写字母
            char val = (char)buf[pos];
            if(val >= 'A' && val <= 'Z'){
                val = (char)(val - LC_OFFSET); //LC_OFFSET是一个负值，因此是在val值基础上加32j
            }
            //5.赋值
            httpHeader.name[readCount] = val;
            readCount++;
            pos++;
        }

        httpHeader.nameEnd = readCount - 1;

        /**
         * 3.读取请求头header的值( Http请求头的值会跨多行吗？)
         * 问题：Http请求头的值会跨多行吗？
         * 解释：请求头字段可以扩展多行，方法是在每个额外行之前至少加上一个SP或HT。其中SP表示空格字符(0x20)， HT表示水平制表符(0x09)。
         *
         * bolince: 从RFC822开始，这种机制就一直存在，只是为了将长标题行折叠成80个字符。
         * Pacerier: 不过，这种有趣的规则确实应该废除。他们无缘无故地把事情复杂化。
         *
         * bolince: 同意，有太多有趣的小问题，其中HTTP不清不楚和头部解析不一致————组件之间的解释不匹配可能导致安全问题。
         * 但是现在做任何事情都太晚了:HTTP头语法是无可救药的，我们只能希望HTTP 2.0中定义更好的东西来替换它
         *
         *
         * 参考：https://stackoverflow.com/questions/521275/how-to-escape-a-line-break-literal-in-the-http-header
         */
        maxRead = httpHeader.value.length;
        readStart = pos;
        readCount = 0;

        boolean eol = false;//标识是不是到达行末尾
        boolean validLine = true;//因为http请求头的值可能跨多行

        while(validLine){//http请求头的值可能跨多行

            //1.请求头的值每一个行的首字符过滤
            //Http请求头的格式： Content-Type: application/json ， 注意冒号后面会有空格。如果该请求头的值还有另一行，则第二行的首字符是HT
            boolean space = true;//标识请求头值前面的空格SP或者水平制表符HT,Http请求头中冒号后面第一个字符就是空格
            while(space){
                if(pos > count){
                    int val = read();
                    if(val == -1){
                        throw new IOException(sm.getString(""));
                    }
                    pos = 0;
                    readStart = 0;
                }
                if(buf[pos] == SP || buf[pos] == HT){
                    pos++;
                }else{
                    space=false;
                }
            }


            //2.主要逻辑：读取请求值
            while(!eol){

                //1.如果存储请求头的值的字符数组(即HttpHeader中的char[] value)容量不够,则扩容。在原容量基础之上扩容一倍
                if(readCount > maxRead){
                    if((maxRead * 2) <= HttpHeader.MAX_VALUE_SIZE){
                        char[] newBuffer = new char[maxRead * 2];
                        //复制数据
                        System.arraycopy(httpHeader.value, 0, newBuffer, 0, maxRead);
                        httpHeader.value = newBuffer;
                        maxRead = httpHeader.value.length;
                    }
                }
                //2.如果内置缓冲区buf有效字节数已经全部读取，则再次填充内置缓冲区，填充的字节数由count标识
                if(pos > count){
                    int val = read();
                    if(val == -1){//如果没有读到数据
                        throw new IOException(sm.getString(""));
                    }
                    pos = 0;
                    readStart = 0;
                }
                //3.如果读取到行末尾
                if(buf[pos] == CR){
                    //不做任何处理
                }else if(buf[pos] == LF){//如果已经读到该请求头最后一个字符
                    eol = true;//标识行末尾
                }else{
                    httpHeader.value[readCount] = (char)buf[pos];
                    readCount++;
                }
                pos++;

            }

            //3.检查下一行首字符,如果首字符是SP或者HT，则说明该请求头的值是跨多行的，否则说明是新的请求头
            int nextChr = read();
            if(nextChr != SP && nextChr != HT){//如果成立，则说明读到新的请求头
                pos--;//这里为什么要后移一位？ 因为读取到下一个请求头的首个字符了
                validLine = false;
            }else{//说明该请求头的值是跨多行的
                //扩容，可能容量不够用
                if(readCount > maxRead){
                    if((maxRead * 2) <= HttpHeader.MAX_VALUE_SIZE){
                        char[] newBuffer = new char[maxRead * 2];
                        System.arraycopy(httpHeader.value, 0 , newBuffer, 0, maxRead);
                        httpHeader.value = newBuffer;
                        maxRead = httpHeader.value.length;
                    }
                }

                httpHeader.value[readCount] = ' ';//Http请求头跨多行的值以空字符分割
                readCount++;
            }

        }




    }

    public int read() throws IOException {
        //count是buf中的有效字节数，pos标识从buf中读取的位置
        //如果pos >= count成立，则说明buf中有效字节已经读完了
        if(pos >= count){
            fill();//填充buf[]
            if(pos >= count){//如果没读取到
                return -1;
            }
        }
        //&运算，将字节类型转换成整型
        return buf[pos++] & 0xff;// &表示按位与,只有两个位同时为1,才能得到1
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
