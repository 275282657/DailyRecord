package com.cgp.dailyrecord.thread;

import java.util.concurrent.CountDownLatch;

/**
 * 使用场景二
 * 多个线程协同工作
 * 多个线程需要等待其他线程的工作之后，再进行其后续工作。
 * 被唤醒后继续执行其他操作
 * <p>
 * 线程1，2执行某项工作完成后，依赖线程3执行之后才能继续执行
 *
 * @Author chenguopeng
 * @Date 2021/7/30 16:05
 */
public class CountDownLatchExample2 {

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 主要工作中.");
            try {
                Thread.sleep(1000);
                latch.await();
                System.out.println(Thread.currentThread().getName() + " 已完成主要工作，开始其他工作.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 主要工作中");
            try {
                Thread.sleep(1000);
                latch.await();
                System.out.println(Thread.currentThread().getName() + " 已完成主要工作，开始其他工作.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            System.out.println("同步等待其他数据");
            try {
                Thread.sleep(5000);
                System.out.println("数据已经准备完毕");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }).start();

        System.out.println("没有影响到我主线程");

    }

}

