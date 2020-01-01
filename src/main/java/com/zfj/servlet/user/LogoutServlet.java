package com.zfj.servlet.user;

import com.zfj.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author zfj
 * @create 2019/12/22 15:46
 */
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //移除用户session
        req.getSession().removeAttribute(Constants.USER_SESSION);

        resp.sendRedirect(req.getContextPath()+"/login.jsp");


    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
