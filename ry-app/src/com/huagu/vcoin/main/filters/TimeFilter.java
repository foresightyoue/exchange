package com.huagu.vcoin.main.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cerc.jbean.core.ServerConfig;

public class TimeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        boolean flag = CompareTime();
        if (flag) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect("/error/error.html");
        }

    }

    @Override
    public void destroy() {

    }

    public boolean CompareTime() {
        ServerConfig Config = ServerConfig.getInstance();
        String time = Config.getProperty("mns.queue.time", String.valueOf(System.currentTimeMillis()));
        Long nowTime = System.currentTimeMillis();
        long sumTime = (Long.parseLong(time) + (1 * 24 * 60 * 60 * 1000) * 45L);
        /*if (nowTime <= sumTime) {
            return true;
        } else {
            return false;
        }*/
        return true;
    }

}
