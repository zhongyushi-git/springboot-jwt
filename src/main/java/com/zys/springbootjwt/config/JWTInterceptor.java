package com.zys.springbootjwt.config;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.Claim;
import com.zys.springbootjwt.util.JWTUtil;
import com.zys.springbootjwt.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongyushi
 * @date 2020/9/22 0022
 * @dec jwt拦截器，验证token
 */
@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Value("${jwt.config.header}")
    private String header;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    //在请求之前进行拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头信息
        String token = request.getHeader(header);
        //从redis获取token副本，解决退出问题。当用户退出后，清空redis的token
        boolean keyIsExists = redisUtil.keyIsExists(header);
        if (keyIsExists) {
            String redisToken = redisUtil.getValue(header);
            //判断副本和原token是否相同
            if (!redisToken.equals(token)) {
                token = null;
            }
        } else {
            token = null;
        }
        Map<String, Object> result = new HashMap<>();
        //如果token为空直接返回错误信息
        if (StringUtils.isEmpty(token)) {
            result.put("msg", "未授权，无法访问资源！");
        } else {
            try {
                //验证token
                jwtUtil.verifyToken(token);
                //验证通过就放行
                return true;
            } catch (SignatureVerificationException e) {
                //签名不一致
                result.put("msg", "抱歉，签名不一致！");
            } catch (TokenExpiredException e) {
                //token过期
                e.printStackTrace();
                result.put("msg", "抱歉，token已过期！");
            } catch (AlgorithmMismatchException e) {
                //验证算法不一致
                e.printStackTrace();
                result.put("msg", "抱歉，验证算法不一致！");
            } catch (InvalidClaimException e) {
                //payload失效
                e.printStackTrace();
                result.put("msg", "抱歉，token已失效！");
            } catch (Exception e) {
                //其他异常
                e.printStackTrace();
                result.put("msg", "认证失败，无法访问资源！");
            }
        }

        result.put("status", false);
        //验证不通过，就给浏览器返回错误信息
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
        return false;
    }
}
