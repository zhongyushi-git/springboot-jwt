package com.zys.springbootjwt.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zhongyushi
 * @date 2020/9/12 16:30:30
 * @dec redis工具类
 */
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * string类型
     */

    //删除key
    public void deleteKey(String key) {
        stringRedisTemplate.delete(key);
    }

    //判断key存在
    public boolean keyIsExists(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    //设置key失效时间,以秒为单位
    public void setKeyTimeOut(String key, long second) {
        stringRedisTemplate.expire(key, second, TimeUnit.SECONDS);
    }

    //设置值
    public void setValue(String key, String val) {
        stringRedisTemplate.boundValueOps(key).set(val);
    }

    //获取值
    public String getValue(String key) {
        return stringRedisTemplate.boundValueOps(key).get();
    }



    /**
     * object类型
     */
    //存入对象
    public void setObject(String key,Object obj){
        getRedisTemplate().boundValueOps(key).set(obj);
    }

    //获取对象
    public Object getObject(String key){
        return getRedisTemplate().boundValueOps(key).get();
    }

    //删除对象
    public void delObject(String key){
        getRedisTemplate().delete(key);
    }

    //设置对象过期时间
    public void setObjectTimeOut(String key,long second){
        getRedisTemplate().expire(key,second,TimeUnit.SECONDS);
    }

    //判断对象是否存在
    public boolean objectIsExists(String key) {
        return getRedisTemplate().hasKey(key);
    }


    //获取RedisTemplate,把key进行string序列化,那么在库中就显示的原始的key值，否则就是16进制的值不方便。
    private RedisTemplate getRedisTemplate(){
        //设置key是string类型的序列
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置hashKey是string类型的序列
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
