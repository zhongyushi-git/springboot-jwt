package com.zys.springbootjwt.service.impl;

import com.zys.springbootjwt.entity.User;
import com.zys.springbootjwt.service.LoginService;
import org.springframework.stereotype.Service;

/**
 * @author zhongyushi
 * @date 2020/9/22 0022
 * @dec 描述
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public boolean login(User user) {
        if("admin".equals(user.getUsername())&&"1234".equals(user.getPassword())){
            return true;
        }
        return false;
    }
}
