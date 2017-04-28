package com.example.readclient.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;

public class HttpLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(HttpLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("================================================");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String method = httpServletRequest.getMethod();
        String requestURI = httpServletRequest.getRequestURI();
        logger.info("{} {}", method, requestURI);
        Enumeration<String> requestHeaderNames = httpServletRequest.getHeaderNames();
        for (String headerName; requestHeaderNames.hasMoreElements();) {
            headerName = requestHeaderNames.nextElement();
            String headerValue = httpServletRequest.getHeader(headerName);
            System.out.println(headerName + " : " + headerValue);
        }
        System.out.println("================================================");

        chain.doFilter(request, response);

        System.out.println("================================================");
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        int status = httpServletResponse.getStatus();
        logger.info("{}", status);
        Collection<String> responseHeaderNames = httpServletResponse.getHeaderNames();
        for (String headerName : responseHeaderNames) {
            String headerValue = httpServletResponse.getHeader(headerName);
            System.out.println(headerName + " : " + headerValue);
        }
        System.out.println("================================================");
    }

    @Override
    public void destroy() {}
}
