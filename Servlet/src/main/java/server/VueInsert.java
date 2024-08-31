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

@WebServlet("/VueInsert")
public class VueInsert extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        try {
            // 获取json数据
            String datajson = req.getParameter("data");
            if (datajson == null) {
                throw new RuntimeException("No data received");
            }
            System.out.println("Encoded data received: " + datajson);

//            // 解码数据
//            datajson = URLDecoder.decode(datajson, "UTF-8");
//            System.out.println("Decoded data: " + datajson);

            // 使用json格式转换数据
            JSONObject jsonObject = JSON.parseObject(datajson);
            if (jsonObject == null) {
                throw new RuntimeException("Failed to parse JSON data");
            }

            // 获取数据并进行检查
            String name = jsonObject.getString("name");
            String sex = jsonObject.getString("sex");
            String age = jsonObject.getString("age");
            String tel = jsonObject.getString("tel");
            String scores = jsonObject.getString("scores");
            String school = jsonObject.getString("school");

            // 将数据封装到对象中
            CRUD crud = new CRUD();
            crud.setName(name);
            crud.setSex(sex);
            crud.setAge(Integer.parseInt(age));
            crud.setTel(Long.parseLong(tel));
            crud.setScores(Double.parseDouble(scores));
            crud.setSchool(school);

            // 将数据插入到数据库
            InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
            SqlSession sqlSession = build.openSession();// 参数为true时，自动提交事务
            OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);// 获取接口的实现类
            int i = orderMapper.insertStuDAO1(crud);
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
