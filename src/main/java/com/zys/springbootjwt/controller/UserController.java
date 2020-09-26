package com.zys.springbootjwt.controller;


import com.zys.springbootjwt.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * @author zhongyushi
 * @date 2020/9/22 0022
 * @dec 用户接口
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("/name")
    public String getName(HttpServletRequest request){
        Map<String, Object> loginUser = jwtUtil.getLoginUser(request);
        System.out.println("登录用户信息:"+loginUser);
        return "欢迎来到JWT世界";
    }


}
