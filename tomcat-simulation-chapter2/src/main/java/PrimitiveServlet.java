
import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 生成servlet的class文件放到webroot下面
 */
public class PrimitiveServlet implements Servlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("init");

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("from service");
        //必须添加响应头，否则google浏览器无法正常显示
        //响应头必须，否则浏览器无法正确显示
        String sucRespHeader = "HTTP/1.1 200 OK\r\n"+
                "Content-Type: text/html;charset=UTF-8\r\n"+
                "\r\n"; //CRLF告诉浏览器响应实体的正文从哪里开始
        PrintWriter writer = servletResponse.getWriter();
        //响应头
        writer.println(sucRespHeader);
        //响应体正文
        writer.println("Hello, Roses are red");
        writer.print("Vioets are blue");//这行没有输出，因为没自动刷新
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}
