package Tools;


import cn.dsna.util.images.ValidateCode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/vcode")
public class VCode extends HttpServlet {
    // 生成验证码
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ValidateCode validateCode = new ValidateCode(100, 30, 4, 10);
        String code = validateCode.getCode();
        HttpSession session = req.getSession();
        session.setAttribute("code", code);//将验证码存入session
        validateCode.write(resp.getOutputStream());//将验证码图片响应给客户端
    }
}
