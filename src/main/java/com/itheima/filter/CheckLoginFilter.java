package com.itheima.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.common.BaseContext;
import com.itheima.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "checkLogin",urlPatterns = "/*") // /只拦截路径 /*既拦截路径也拦截页面
public class CheckLoginFilter implements Filter {
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        String[] urls = new String[]{"/employee/login","/employee/logout","/backend/**","/front/**",
                                        "/common/**","/user/sendMsg","/user/login"};
        boolean check = check(urls, requestURI);

        if(check) {
            filterChain.doFilter(request,response);
            return;
        }

        if(request.getSession().getAttribute("employee") != null) {
            BaseContext.setCurrentId((Long)request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }

        if(request.getSession().getAttribute("user") != null) {
            BaseContext.setCurrentId((Long)request.getSession().getAttribute("user"));
            filterChain.doFilter(request,response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        log.info("拦截到请求:{}",requestURI);
    }

    public boolean check(String[] urls,String uri) {
        for(String url : urls) {
            boolean match = antPathMatcher.match(url, uri);
            if(match) {
                return true;
            }
        }
        return false;
    }
}
