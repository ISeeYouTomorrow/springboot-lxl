package com.lxl.springcache;

import com.lxl.springcache.bean.Person;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl lukas
 * @description
 * @create 2018/2/2
 * 拦截器在我们项目中经常使用的，这里就来介绍下最简单的判断是否登录的使用。

要实现拦截器功能需要完成以下2个步骤：

创建我们自己的拦截器类并实现 HandlerInterceptor 接口

其实重写WebMvcConfigurerAdapter中的addInterceptors方法把自定义的拦截器类添加进来即可


 */
public class HRInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        boolean flag = true;
        Person person = (Person) httpServletRequest.getSession().getAttribute("person");
        if(null == person){
            httpServletResponse.sendRedirect("toLogin");
            flag = false;
        }else{
            flag = true;
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
