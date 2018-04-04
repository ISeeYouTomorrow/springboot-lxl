package com.lxl.springboot;

import org.junit.Test;

/**
 * @author lxl lukas
 * @description
 * @create 2018/1/27
 */
public class ProxyTest {

    /**
     * 静态代理模式测试 静态代理，在不修改目标对象的情况下，可以通过代理对象做额外的扩展功能
     */
    @Test
    public void staticProxyTest(){
        IBlogService service = new BlogServiceImpl();
        BlogProxy proxy = new BlogProxy(service);
        proxy.writeBlog();
    }

    /**
     * jdk 动态代理测试
     * 通过反射实现动态代理
     */
    @org.junit.jupiter.api.Test
    public void jdkProxyTest(){
        IBlogService service = new BlogServiceImpl();
        // 给目标对象，创建代理对象
        JdkBlogProxyFactory proxyFactory = new JdkBlogProxyFactory(service);

        //class $Proxy0   内存中动态生成的代理对象
        IBlogService proxy = (IBlogService) proxyFactory.newInstance();

        proxy.writeBlog();
    }


    @Test
    public void cglibProxyTest(){
        IBlogService service = new BlogServiceImpl();

        CGlibProxyFactory proxyFactory = new CGlibProxyFactory(service);

        IBlogService proxy = (IBlogService)proxyFactory.getProxyInstance();

        proxy.writeBlog();
    }
}
