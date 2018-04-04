package com.lxl.springboot;

/**
 * 代理模式接口的实现
 * @author lxl lukas
 * @description
 * @create 2018/1/27
 */
public class BlogServiceImpl implements IBlogService {

    @Override
    public void writeBlog() {
        System.out.println("I am writing blog...");
    }
}
