package com.lxl.springboot;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *
 * CGLib采用了非常底层的字节码技术，
 * 其原理是通过字节码技术为一个类创建子类，
 * 并在子类中采用方法拦截的技术拦截所有父类方法的调用，顺势织入横切逻辑。
 * @author lxl lukas
 * @description
 * @create 2018/1/27
 */
public class CGlibProxyFactory implements MethodInterceptor{
    private Object target;

    public CGlibProxyFactory(Object target) {
        this.target = target;
    }

    /**
     * 为目标对象创建一个代理对象
     * @return
     */
    public Object getProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("cglib start writing....");

        /**执行目标对象的方法**/
        Object result = method.invoke(target,objects);

        System.out.println("cglib  end  writing....");

        return result;
    }
}
