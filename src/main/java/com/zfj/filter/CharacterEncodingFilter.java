package com.zfj.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Author zfj
 * @create 2019/12/22 12:36
 */
public class CharacterEncodingFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        servletRequest.setCharacterEncoding("utf-8");
        servletResponse.setCharacterEncoding("utf-8");

        filterChain.doFilter(servletRequest,servletResponse);

    }

    public void destroy() {

    }
}
