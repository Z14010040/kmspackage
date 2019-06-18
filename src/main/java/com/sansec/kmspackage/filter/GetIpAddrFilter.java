package com.sansec.kmspackage.filter;

import com.sansec.kmspackage.tools.LogTool;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class GetIpAddrFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String ip = LogTool.getIpAddr(httpServletRequest);
        MDC.put("ip", ip);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
