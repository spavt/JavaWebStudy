package server;

import Dao.OrderMapper;
import Model.CRUD;
import com.alibaba.fastjson.JSON;
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
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/vueTest")
public class VueTest extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
        SqlSession sqlSession = build.openSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

        int pageSize = 12; // 每页显示的记录数
        int curPage = 1; // 当前页码，默认值为1
        if (req.getParameter("curPage") != null) {
            curPage = Integer.parseInt(req.getParameter("curPage"));
        }
        int start = (curPage - 1) * pageSize; // 计算起始位置

        List<CRUD> crud = orderMapper.showCrudPage(start, pageSize); // 获取当前页的数据
        int totalRecords = orderMapper.getTotalCount1(); // 获取总记录数
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize); // 计算总页数

        // 构建结果集
        Map<String, Object> result = new HashMap<>();
        result.put("crud", crud);
        result.put("totalPages", totalPages);
        result.put("curPage", curPage);

        // 将结果集转换为JSON字符串
        String res = JSON.toJSONString(result);
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.write(res);

        writer.flush();
        writer.close();
        sqlSession.close();
        in.close();
    }
}
