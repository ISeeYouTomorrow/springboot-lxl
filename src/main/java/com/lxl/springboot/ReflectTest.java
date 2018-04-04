package com.lxl.springboot;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author lxl lukas
 * @description
 * @create 2018/1/28
 */
public class ReflectTest {

    @Test
    public void methodTest(){
        User user = new User();
        Class c = User.class;
        try {
            c = Class.forName("com.lxl.springboot.User");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        c = user.getClass();


        String path = c.getClassLoader().getResource("").getPath();
        System.out.println("path: "+path);

        Field[] fields =
                c.getDeclaredFields();//获取所有属性
//        c.getFields();//获取public 属性
        for (Field f:fields
             ) {
            System.out.println("field :"+f.getName()+" ;ftype:"+f.getType());
        }

        Method[] methods = c.getMethods();//getMethod()所有的 public方法，包括父类继承的 public
        methods = c.getDeclaredMethods();//getDeclaredMethods获取所有生命方法，不包括父类的方法
        for (Method m:methods
             ) {
            System.out.println("method: "+m.getName()+" ,return type:"+m.getReturnType());
            Parameter[] ps = m.getParameters();
            for (Parameter p:ps
                 ) {
                System.out.println("parameter: "+p.getName()+" ;type:"+p.getType().getTypeName());
            }

        }

        try {
            User instance = (User) c.newInstance();
            instance.setId(1);
            instance.setUserName("lxl");

            System.out.println("instance is :"+instance);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Method m = c.getDeclaredMethod("setUserName",String.class);
            m.invoke(user,"lxl");

            Method m1 = c.getDeclaredMethod("setId",Integer.class);
            m1.invoke(user,1);

            System.out.println("user;"+user);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }



}
