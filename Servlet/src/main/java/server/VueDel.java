package server;

import Dao.OrderMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/vueDel")
public class VueDel extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
        SqlSession sqlSession = build.openSession(true); // Enable auto-commit
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

        String idParam = req.getParameter("id");
        int id = Integer.parseInt(idParam);
        int result = orderMapper.delStuDAO1(id);

        resp.setContentType("application/json");
        if (result > 0) {
            resp.getWriter().write("{\"status\":\"success\"}");
        } else {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Delete operation failed\"}");
        }

        sqlSession.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
