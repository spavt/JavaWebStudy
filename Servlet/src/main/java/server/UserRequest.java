package server;

import Dao.OrderMapper;
import Model.admin;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/islogin")
public class UserRequest extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("user");
        String pwd = req.getParameter("pwd");
        String userCode = req.getParameter("code");
        System.out.println("user: " + user + " pwd: " + pwd + " code: " + userCode);

        // 检查参数是否为空
        if (user == null || pwd == null || userCode == null) {
            resp.getWriter().write("error");
            return;
        }

        HttpSession session = req.getSession();
        String code = (String) session.getAttribute("code");

        // 验证验证码是否正确
        if (code == null || !userCode.equals(code)) {
            System.out.println("验证码错误");
            req.getRequestDispatcher("/userError").forward(req, resp);
            return;
        }

        try (InputStream in = Resources.getResourceAsStream("mybatis-config.xml")) {
            SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
            try (SqlSession sqlSession = build.openSession()) {
                OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
                admin login = orderMapper.findAdmin(user, pwd);
                System.out.println("login: " + login);

                if (login != null) {
                    session.setAttribute("login", "login");
                    resp.getWriter().write("success");
                    System.out.println("用户登录成功");
                } else {
//                    resp.getWriter().write("error");
                    System.out.println("用户登录失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("error");
        }
    }
}
