package com.cgp.dailyrecord.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CountDownLatch 是一次性的。 CyclicBarrier 是可循环利用的
 * CountDownLatch 参与的线程的职责是不一样的，有的在倒计时，有的在等待倒计时结束。 CyclicBarrier 参与的线程职责是一样的
 *
 * 栅栏类似于闭锁（CountDownLatch），它能阻塞一组线程直到某个事件的发生。栅栏与闭锁的关键区别在于， 所有的线程必须同时到达栅栏位置，才能继续执行。闭锁用于等待事件，而 栅栏用于等待其他线程。
 *
 * CyclicBarrier可以使一定数量的线程反复地在栅栏位置处汇集。 当线程到达栅栏位置时将调用await方法，这个方法将阻塞直到所有线程都到达栅栏位置。
 * 如果所有线程都到达栅栏位置，那么栅栏将打开，此时所有的线程都将被释放，而栅栏将被重置以便下次使用。
 *
 * CountDownLatch 是一次性的。 CyclicBarrier 是可循环利用的
 * CountDownLatch 参与的线程的职责是不一样的，有的在倒计时，有的在等待倒计时结束。 CyclicBarrier 参与的线程职责是一样的
 * @Author chenguopeng
 * @Date 2021/7/30 16:33
 */
public class CyclicBarrierExample1 {

	public static void main(String[] args) {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> System.out.println("收集到7颗龙珠,召唤神龙"));

		for (int i = 0; i < 7; i++) {
			final int temp = i + 1;
			new Thread(() ->{
				System.out.println(Thread.currentThread().getName() + "\t收集到第" + temp + "颗龙珠");
				try {
					int await = cyclicBarrier.await();
					System.out.println("还剩几个:" + await);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			},"线程" + String.valueOf(i)).start();
		}
	}
}

