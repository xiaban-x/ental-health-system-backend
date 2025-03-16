package com.interceptor;

import java.io.PrintWriter;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;

import com.annotation.IgnoreAuth;
import com.entity.TokenEntity;
import com.service.TokenService;
import com.utils.R;

/**
 * 权限(Token)验证
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    public static final String LOGIN_TOKEN_KEY = "token";

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 支持跨域请求
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers",
                "x-requested-with,request-source,Token, Origin,imgType, Content-Type, cache-control,postman-token,Cookie, Accept,authorization");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        // 获取请求路径
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isEmpty() && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }

        // 放行 Swagger/SpringDoc 相关路径
        if (path.startsWith("/swagger-ui") ||
                path.startsWith("/swagger-ui.html") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars/") ||
                path.equals("/error") ||
                path.contains("api-docs") ||
                path.contains("swagger")) {
            System.out.println("Swagger路径放行: " + path);
            return true;
        }

        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.OK.value());
            return false;
        }

        IgnoreAuth annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
        } else {
            return true;
        }

        String token = request.getHeader(LOGIN_TOKEN_KEY);
        if (token == null) {
            // 尝试从 Authorization 头获取 Bearer token
            String authHeader = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7); // 去掉 "Bearer " 前缀
            }
        }
        /**
         * 不需要验证权限的方法直接放过
         */
        if (annotation != null) {
            return true;
        }

        TokenEntity tokenEntity = null;
        if (StringUtils.isNotBlank(token)) {
            tokenEntity = tokenService.getTokenEntity(token);
        }

        if (tokenEntity != null) {
            request.getSession().setAttribute("userId", tokenEntity.getUserid());
            request.getSession().setAttribute("role", tokenEntity.getRole());
            request.getSession().setAttribute("tableName", tokenEntity.getTablename());
            request.getSession().setAttribute("username", tokenEntity.getUsername());
            return true;
        }

        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSONObject.toJSONString(R.error(401, "请先登录")));
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        // throw new EIException("请先登录", 401);
        return false;
    }
}
