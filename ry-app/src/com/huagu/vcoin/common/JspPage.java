package com.huagu.vcoin.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

public class JspPage extends ModelAndView {
    private HttpServletRequest request;

    public JspPage(HttpServletRequest request) {
        this.request = request;
    }

    public void add(String key, Object value) {
        this.addObject(key, value);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setJspFile(String viewName) {
        String uri = request.getRequestURI().toLowerCase().trim();
        if (!uri.startsWith("/m/")) {
            this.setViewName(viewName);
            return;
        }
        // 手机环境
        if (viewName.startsWith("front/app/")) {
            this.setViewName(viewName.substring(6));
        } else if (viewName.startsWith("/front/app/")) {
            this.setViewName(viewName.substring(7));
        } else if (viewName.startsWith("front/")) {
            this.setViewName("app/" + viewName.substring(6));
        } else if (viewName.startsWith("/front/")) {
            this.setViewName("app/" + viewName.substring(7));
        } else {
            this.setViewName("m/" + viewName);
        }
    }

    public void setRedirectJsp(String viewName) {

        if (viewName.equals("/")) {
            viewName = "/index";
        }
        String uri = request.getRequestURI().toLowerCase().trim();
        if (!uri.startsWith("/m/")) {
            this.setViewName("redirect:" + viewName);
            return;
        }
        // 手机环境
        if (viewName.startsWith("front/app/")) {
            this.setViewName("redirect:/" + viewName.substring(6));
        } else if (viewName.startsWith("/front/app")) {
            this.setViewName("redirect:/" + viewName.substring(7));
        } else if (viewName.startsWith("front")) {
            this.setViewName("redirect:/" + "m/" + viewName.substring(6));
        } else if (viewName.startsWith("/front")) {
            this.setViewName("redirect:/" + "m/" + viewName.substring(7));
        } else {
            this.setViewName("redirect:/" + "m" + viewName);
        }
    }

    public static void main(String[] args) {
        System.out.println("front/".substring(6));
    }
}
