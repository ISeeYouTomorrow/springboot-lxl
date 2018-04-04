package com.lxl.springboot;

/**
 * @author lxl lukas
 * @description
 * @create 2018/2/5
 */
public class ThreadTest {

    public static void main(String[] args) {
        Thread t1 = new MyThread();
        t1.start();

        Thread t2 = new MyThread();

        t2.start();

        System.out.println("before yield current thread is :"+Thread.currentThread().getName());
        Thread.yield();//将当前线程转为可运行状态，运行其他相同优先级的线程


        Thread t3 = new MyThread();
        t3.start();
        System.out.println("after yield current thread is :"+Thread.currentThread().getName());
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("t1 finish ...");


    }

}

class MyThread extends Thread{
    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName()+" mythread is run...");
    }
}
