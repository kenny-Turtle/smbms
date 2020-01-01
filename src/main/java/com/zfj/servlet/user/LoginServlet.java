package com.zfj.servlet.user;

import com.zfj.pojo.User;
import com.zfj.service.user.UserService;
import com.zfj.service.user.UserServiceImpl;
import com.zfj.util.Constants;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * @Author zfj
 * @create 2019/12/22 13:27
 */
public class LoginServlet extends HttpServlet {

    private UserService userService;

    public LoginServlet(){
        userService=new UserServiceImpl();
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("login ============ " );
        //获取用户名和密码
        String userCode = request.getParameter("userCode");
        String userPassword = request.getParameter("userPassword");
        //调用service方法，进行用户匹配
//        UserService userService = new UserServiceImpl();
        User user = userService.login(userCode, userPassword);
        if(null != user){//登录成功
            //放入session
            request.getSession().setAttribute(Constants.USER_SESSION, user);
            //页面跳转（frame.jsp）
            response.sendRedirect("jsp/frame.jsp");
        }else{
            //页面跳转（login.jsp）带出提示信息--转发
            request.setAttribute("error", "用户名或密码不正确");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }

    }



}
