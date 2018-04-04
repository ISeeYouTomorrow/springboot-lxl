package com.lxl.springboot;

import java.lang.reflect.Proxy;

/**
 * java动态代理
 * @author lxl lukas
 * @description
 * @create 2018/1/27
 */
public class JdkBlogProxyFactory {
    private Object target;

    public JdkBlogProxyFactory(Object object) {
        this.target = object;
    }

    public Object newInstance(){
        return Proxy.newProxyInstance(this.target.getClass().getClassLoader(),
                this.target.getClass().getInterfaces(), (proxy, method, args) -> {
                    System.out.println("start writing....");
                    Object o = method.invoke(target,args);
                    System.out.println("end   writing....");
                    return o;
                });
    }
}
