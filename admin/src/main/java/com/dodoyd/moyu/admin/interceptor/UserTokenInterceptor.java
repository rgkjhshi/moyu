package com.dodoyd.moyu.admin.interceptor;

import com.dodoyd.moyu.admin.util.UserLocalUtils;
import com.dodoyd.moyu.common.enums.ExceptionEnum;
import com.dodoyd.moyu.common.exception.BaseException;
import com.dodoyd.moyu.common.model.BaseResponse;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 针对vue router中的history模式拦截器，url匹配不到任何静态资源或者在白名单（即此url交由vue处理）时，则应该返回index.html
 *
 * @author shisong
 * @since 2019-12-31
 */
@Slf4j
@Component
public class UserTokenInterceptor implements HandlerInterceptor {

    private static final String TOKEN_HEADER_KEY = "X-Token";

//    @Resource
//    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // 需要验证token
            String token = request.getHeader(TOKEN_HEADER_KEY);
            if (Strings.isNullOrEmpty(token)) {
                log.info("token为空, 访问路径:{}", request.getRequestURI());
                throw new BaseException(ExceptionEnum.INVALID_PARAMETER.getCode(), "登录信息有误");
            }
            // 验证登录并解析userId（不通过会抛出异常）
//            Long userId = tokenService.getUserIdByToken(token);
//            log.info("用户userId:{}, 访问路径:{}", userId, request.getRequestURI());
//            UserLocalUtils.setUserId(userId);
        } catch (BaseException e) {
            BaseResponse<?> result = new BaseResponse<>(e.getCode(), e.getMessage());
            response.setCharacterEncoding(Charsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().print(new Gson().toJson(result));
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理userId
        if (UserLocalUtils.getUserId() != null) {
            UserLocalUtils.setUserId(null);
        }
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
