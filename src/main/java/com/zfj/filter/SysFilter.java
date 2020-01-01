package com.zfj.filter;

import com.zfj.pojo.User;
import com.zfj.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author zfj
 * @create 2019/12/22 15:56
 */
public class SysFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION);
        if(user==null){
            response.sendRedirect("/smbms02/error.jsp");
        }else{
            filterChain.doFilter(req,resp);
        }
//        filterChain.doFilter(req,resp);


    }

    public void destroy() {

    }
}
