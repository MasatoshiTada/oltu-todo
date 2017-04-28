package com.example.authorizationserver.web.servletfilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Map;

/**
 * POST時のリクエストボディを保存するフィルター
 * （Jersey処理後がリクエストボディが消えるため）
 */
public class RequestBodyWrappingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(httpServletRequest) {
            @Override
            public Map<String, String[]> getParameterMap() {
                return parameterMap;
            }

            @Override
            public String getParameter(String name) {
                String[] params = parameterMap.get(name);
                return params == null || params.length == 0 ? null : params[0];
            }

            @Override
            public String[] getParameterValues(String name) {
                return parameterMap.get(name);
            }
        };
        chain.doFilter(requestWrapper, servletResponse);
    }

    @Override
    public void destroy() {}
}
