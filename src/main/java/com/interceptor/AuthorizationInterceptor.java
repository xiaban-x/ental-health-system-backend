package com.interceptor;

import java.io.IOException;
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

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 支持跨域请求
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers",
                "Authorization, Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        // 处理预检请求
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.OK.value());
            return false;
        }

        // 获取请求路径
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (StringUtils.isNotEmpty(contextPath) && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }

        // 放行公开路径
        if (isPublicPath(path)) {
            return true;
        }

        // 检查是否需要跳过认证
        IgnoreAuth annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
            if (annotation != null) {
                return true;
            }
        }

        // OAuth 2.0 Bearer Token 认证
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        System.out.println("authHeader ==>" + authHeader);
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            handleUnauthorized(response, "缺少有效的访问令牌");
            return false;
        }

        // 提取并验证 token
        String token = authHeader.substring(BEARER_PREFIX.length());
        TokenEntity tokenEntity = tokenService.getTokenEntity(token);

        if (tokenEntity == null) {
            handleUnauthorized(response, "访问令牌无效或已过期");
            return false;
        }

        // 设置认证信息
        request.setAttribute("userId", tokenEntity.getUserid());
        request.setAttribute("role", tokenEntity.getRole());
        request.setAttribute("username", tokenEntity.getUsername());

        return true;
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars/") ||
                path.equals("/error") ||
                path.contains("api-docs");
    }

    private void handleUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSONObject.toJSONString(R.error(401, message)));
    }
}
