package com.lxl.springboot;

/**
 * 对象代理类，与持有的对象实现同一个接口，便于随时扩展代理对象的方法
 * 代理角色，内部含有对目标对象TargetSubject的引用，从而可以操作真实对象。
 * 代理对象提供与目标对象相同的接口，以便在任何时刻都能代替目标对象。
 * 同时，代理对象可以在执行目标对象操作时，附加其他的操作，相当于对真实对象进行封装。
 * @author lxl lukas
 * @description
 * @create 2018/1/27
 */
public class BlogProxy implements IBlogService {

    private IBlogService service;

    public BlogProxy(IBlogService service) {
        this.service = service;
    }

    @Override
    public void writeBlog() {
        System.out.println("before write blog...");
        service.writeBlog();
        System.out.println("after write blog...");
    }
}
