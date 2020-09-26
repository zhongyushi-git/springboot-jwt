package com.zys.springbootjwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongyushi
 * @date 2020/9/22 0022
 * @dec 描述
 */
@Component
public class JWTUtil {

    @Value("${jwt.config.secret}")
    private String SING;

    @Value("${jwt.config.expire}")
    private Integer expire;

    @Value("${jwt.config.header}")
    private String header;

    /**
     * 生成token
     * @param map
     * @return
     */
    public String createToken(Map<String,String> map){

        //创建jwt构建器
        JWTCreator.Builder builder= JWT.create();

        //设置token的过期时间
        Calendar calendar=Calendar.getInstance();
        //默认是30分钟
        calendar.add(Calendar.MINUTE,expire);
        builder.withExpiresAt(calendar.getTime());

        //设置payload，存储需要的一些参数
        map.forEach((key,value)->{
            builder.withClaim(key,value);
        });

        //加密后生成token
        String token = builder.sign(Algorithm.HMAC256(SING));
        return token;
    }

    /**
     * 验证token，验证通过返回参数信息
     * @param token
     * @return
     */
    public DecodedJWT verifyToken(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SING)).build();
        //验证token,如果验证失败会抛出异常
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT;
    }

    /**
     * 获取登录的用户信息
     * @param request
     * @return
     */
    public Map<String,Object> getLoginUser(HttpServletRequest request){
        //获取请求头信息
        String token = request.getHeader(header);
        DecodedJWT decodedJWT = verifyToken(token);
        //获取payload中设置的参数
        Map<String, Claim> claims = decodedJWT.getClaims();
        Map<String,Object> result=new HashMap<>();
        result.put("username",claims.get("username").asString());
        return result;
    }
}
