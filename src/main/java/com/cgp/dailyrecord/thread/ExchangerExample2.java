package com.cgp.dailyrecord.thread;

import java.util.concurrent.Exchanger;

/**
 * 等待另一个线程到达此交换点，然后将给定对象传输给它，接收其对象作为回报。
 * 可以被打断
 * 如果已经有个线程正在等待了，则直接交换数据
 * @Author chenguopeng
 * @Date 2021/7/30 16:57
 */
public class ExchangerExample2 {
	public static void main(String[] args) {


		final Exchanger<Object> exchanger = new Exchanger<>();

		new Thread(() -> {
			Object Aobj = new Object();
			System.out.println("A将会发送：" + Aobj);
			try {
				Object Robj = exchanger.exchange(Aobj);
				System.out.println("A接收的：" + Robj);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}, "A").start();

		new Thread(() -> {
			Object Bobj = new Object();
			System.out.println("B将会发送：" + Bobj);
			try {
				Object Robj = exchanger.exchange(Bobj);
				System.out.println("B接收的：" + Robj);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}, "B").start();
	}
}


