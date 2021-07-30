package com.cgp.dailyrecord.thread;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * 用于两个工作线程之间交换数据的封装工具类
 * 简单说就是一个线程在完成一定的事务后想与另一个线程交换数据，则 *第一个先拿出数据的线程会一直等待第二个线程，直到第二个线程拿着数据到来时才能彼此交换对应数据
 *
 * @Author chenguopeng
 * @Date 2021/7/30 16:51
 */
public class ExchangerExample1 {

	public static void main(String[] args) {
		final Exchanger<String> exchanger = new Exchanger<>();


		new Thread(() -> {
			System.out.println(Thread.currentThread().getName() + " start . ");
			try {

				/**
				 * 如果这里睡200ms的话，应该是B线程先拿出数据，然后B线程等待A线程。因为是B先给的数据，
				 * 所以最后A线程会先拿到B给的数据，也就是先打印
				 */
				TimeUnit.MILLISECONDS.sleep(2000);
				String exchange = exchanger.exchange("I am come from T-A");
				System.out.println(Thread.currentThread().getName() + " get value : " + exchange);
				System.out.println(Thread.currentThread().getName() + " end . ");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}, "A").start();


		new Thread(() -> {
			System.out.println(Thread.currentThread().getName() + " start . ");
			try {
				String exchange = exchanger.exchange("I am come from T-B");
				System.out.println(Thread.currentThread().getName() + " get value : " + exchange);
				System.out.println(Thread.currentThread().getName() + " end . ");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}, "B").start();
	}
}
