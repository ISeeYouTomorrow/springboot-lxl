package com.lxl.webone;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

/**
 * @author lxl lukas
 * @description
 * @create 2018/1/30
 */
public class RedisTest {

    @Test
    public void redisForJava(){
        System.out.println("begin to fetch....");
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");

        // 获取数据并输出
        Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key);
        }
        jedis.close();
    }

}
