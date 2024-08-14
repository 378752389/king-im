package com.king.im.common.interceptor;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.im.common.exceptions.GlobalException;
import com.king.im.common.utils.JwtUtils;
import com.king.im.user.constants.LoginConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        /**
         * /swagger-ui/
         * /swagger-resources/
         * /v3/**
         * /webjars/**
         */
        if (request.getRequestURI().startsWith("/v3/api-docs")) {
            return true;
        }

        //从 http 请求头中取出 token
        String token = request.getHeader("accessToken");
        if (StrUtil.isEmpty(token)) {
            log.error("未登陆，url:{}", request.getRequestURI());
            throw new GlobalException("accessToken无效或过期");
        }
        //验证 token
        if (!JwtUtils.checkSign(token, LoginConstants.accessSecret)) {
            log.error("token已失效，url:{}", request.getRequestURI());
            throw new GlobalException("accessToken无效或过期");
        }


        String userInfoStr = JwtUtils.getUserInfo(token);
        if (userInfoStr == null) {
            return false;
        }
        UserInfo userInfo = objectMapper.readValue(userInfoStr, new TypeReference<UserInfo>() {
        });

        if (userInfo == null) {
            return false;
        }

        RequestInfoHolder.setUid(userInfo.getUid());
        RequestInfoHolder.setUserInfo(userInfo);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        RequestInfoHolder.clear();
    }
}
