package com.zys.springbootjwt.service;

import com.zys.springbootjwt.entity.User;

/**
 * @author zhongyushi
 * @date 2020/9/22 0022
 * @dec 描述
 */

public interface LoginService {

    boolean login(User user);
}
