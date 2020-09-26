package com.zys.springbootjwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhongyushi
 * @date 2020/9/22 0022
 * @dec 拦截器配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private JWTInterceptor interceptor;

    //从配置文件读取忽略的url,不拦截这些请求
    @Value("${system.IgnoreUrl}")
    private String ignoreUrl;


    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns表示拦截所有请求，excludePathPatterns表示不拦截的请求
        registry.addInterceptor(interceptor).addPathPatterns("/**").excludePathPatterns(ignoreUrl);
    }
}
