package FilterTest1;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class FilterTest1 implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8"); // 设置请求编码
//        servletResponse.setContentType("text/html;charset=UTF-8"); // 设置响应编码

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpSession session = httpRequest.getSession(false);
        String requestURI = httpRequest.getRequestURI().toLowerCase(); // 转为小写以忽略大小写

        // 简化条件检查，使用正则匹配URI
        if (requestURI.matches(".*(islogin|login.html|vcode|\\.js|\\.css|\\.png|\\.jpg)$")) {
            // 这些URI不需要验证登录状态，直接放行
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (session == null || session.getAttribute("login") == null) {
            // 用户未登录或会话不存在，记录并重定向到登录页面
            System.out.println("未登录用户尝试访问: " + requestURI);
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/Login.html");
        } else {
            // 用户已登录，继续处理请求
            try {
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (Exception e) {
                e.printStackTrace();
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误，请稍后再试。");
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("过滤器销毁");
    }
}
