package com.zys.springbootjwt.controller;

import com.alibaba.fastjson.JSONObject;
import com.zys.springbootjwt.entity.User;
import com.zys.springbootjwt.service.LoginService;
import com.zys.springbootjwt.util.JWTUtil;
import com.zys.springbootjwt.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongyushi
 * @date 2020/9/22 0022
 * @dec 用户登录接口
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${jwt.config.header}")
    private String header;

    /**
     * 用户登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public JSONObject login(User user) {
        boolean u = loginService.login(user);
        JSONObject json = new JSONObject();
        if (u) {
            //设置payload中存储的参数，方便在后台获取
            Map<String,String> params=new HashMap<>();
            params.put("username",user.getUsername());
            //生成token并返回
            String token = jwtUtil.createToken(params);
            //保存token副本到redis
            redisUtil.setValue(header,token);
            json.put("msg","登录成功");
            json.put("status",true);
            json.put("token",token);
        }else{
            json.put("msg","用户名或密码错误");
            json.put("status",false);
        }
        return json;
    }

    /**
     * 退出登录
     * @return
     */
    @GetMapping("/logout")
    public JSONObject logout(){
        //删除缓存信息
        redisUtil.deleteKey(header);
        JSONObject json = new JSONObject();
        json.put("msg","退出成功");
        json.put("status",true);
        return json;
    }
}
