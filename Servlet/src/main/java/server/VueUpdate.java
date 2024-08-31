package server;

import Dao.OrderMapper;
import Model.CRUD;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

@WebServlet("/VueUpdate")
public class VueUpdate extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        try {
            // 获取json数据
            String datajson = req.getParameter("data");
            if (datajson == null) {
                throw new RuntimeException("没有收到数据");
            }
            System.out.println("收到的数据是" + datajson);


            // 使用json格式转换数据
            JSONObject jsonObject = JSON.parseObject(datajson);

            // 获取数据并进行检查
            int id = Integer.parseInt(jsonObject.getString("id"));
            String name = jsonObject.getString("name");
            String sex = jsonObject.getString("sex");
            String age = jsonObject.getString("age");
            String tel = jsonObject.getString("tel");
            String scores = jsonObject.getString("scores");
            String school = jsonObject.getString("school");

            // 将数据封装到对象中
            CRUD crud = new CRUD();
            crud.setId(id);
            crud.setName(name);
            crud.setSex(sex);
            crud.setAge(Integer.parseInt(age));
            crud.setTel(Long.parseLong(tel));
            crud.setScores(Double.parseDouble(scores));
            crud.setSchool(school);

            InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
            SqlSession sqlSession = build.openSession();// 参数为true时，自动提交事务
            OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);// 获取接口的实现类
            int i = orderMapper.updateStu1(crud);
            sqlSession.commit();
            sqlSession.close();

            // 返回成功消息
            resp.getWriter().write("{\"status\":\"success\"}");
        } catch (Exception e) {
            e.printStackTrace();
            // 返回错误消息
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
