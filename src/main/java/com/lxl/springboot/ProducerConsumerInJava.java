package com.lxl.springboot;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *  永远在synchronized的函数或对象里使用wait、notify和notifyAll，不然Java虚拟机会生成 IllegalMonitorStateException。
 *  永远在while循环里而不是if语句下使用wait。这样，循环会在线程睡眠前后都检查wait的条件，并在条件实际上并未改变的情况下处理唤醒通知。
 *  永远在多线程间共享的对象（在生产者消费者模型里即缓冲区队列）上使用wait。
 *  基于前文提及的理由，更倾向用 notifyAll()，而不是 notify()
 * @author lxl lukas
 * @description
 * @create 2018/2/5
 */
public class ProducerConsumerInJava {

    public static void main(String[] args) {

        System.out.println("How to use wait and notify in Java Thread!");

        System.out.println("Solving producer and consumer problem.");

        Queue<Integer> queue = new LinkedList<>();
        int maxsize = 10;
        Producer producer = new Producer(queue, maxsize, "PRODUCER");

        Consumer consumer = new Consumer(queue,maxsize,"CONSUMER");

        producer.start();

        consumer.start();

        Consumer consumer1 = new Consumer(queue,maxsize,"CONSUMER1");
        consumer1.start();

        Consumer consumer2 = new Consumer(queue,maxsize,"CONSUMER2");
        consumer2.start();
    }
}

    class Producer extends Thread{
        private Queue<Integer> queue;
        private Integer maxSize;
        private String name;

        public Producer(Queue<Integer> queue, Integer maxSize, String name) {
            this.queue = queue;
            this.maxSize = maxSize;
            this.name = name;
        }

        @Override
        public void run() {
            while (true){
                synchronized (queue){
                    while (queue.size() == maxSize){
                        System.out.println("queue is full,Producer thread waiting for consumer to take something from queue");
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Random random = new Random();
                    int i = random.nextInt();
                    queue.add(i);
                    System.out.println("Producing value for queue: "+i);
                    queue.notifyAll();
                }
            }
        }
    }

    /**
     * 消费者
     */
    class Consumer extends Thread{
        private Queue<Integer> queue;
        private Integer maxSize;
        private String name;

        public Consumer(Queue<Integer> queue, Integer maxSize, String name) {
            this.queue = queue;
            this.maxSize = maxSize;
            this.name = name;
        }

        @Override
        public void run() {
            while (true){
                synchronized (queue){
                    while (queue.isEmpty()){
                        System.out.println(Thread.currentThread().getName()+"queue is empty,Consumer thread is waiting for producer thread to put something in queue");

                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println(Thread.currentThread().getName()+"consuming value from queue:"+queue.remove());

                    queue.notifyAll();

                }
            }
        }
    }
