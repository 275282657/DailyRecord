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
 *
 *
 * CyclicBarrier所有的成员变量
 * //同步操作锁
 * private final ReentrantLock lock = new ReentrantLock();
 * //线程拦截器
 * private final Condition trip = lock.newCondition();
 * //每次拦截的线程数
 * private final int parties;
 * //换代前执行的任务
 * private final Runnable barrierCommand;
 * //表示栅栏的当前代
 * private Generation generation = new Generation();
 * //计数器
 * private int count;
 *
 * 可以看到CyclicBarrier内部是通过条件队列trip来对线程进行阻塞的，并且其内部维护了两个int型的变量parties和count，parties表示每次拦截的线程数，该值在构造时进行赋值。count
 * 是内部计数器，它的初始值和parties相同，以后随着每次await方法的调用而减1，直到减为0就将所有线程唤醒。CyclicBarrier有一个静态内部类Generation
 * ，该类的对象代表栅栏的当前代，就像玩游戏时代表的本局游戏，利用它可以实现循环等待。barrierCommand表示换代前执行的任务，当count减为0
 * 时表示本局游戏结束，需要转到下一局。在转到下一局游戏之前会将所有阻塞的线程唤醒，在唤醒所有线程之前你可以通过指定barrierCommand来执行自己的任务
 *
 * //静态内部类Generation
 * private static class Generation {
 *   boolean broken = false;
 * }
 *
 * @Author chenguopeng
 * @Date 2021/7/30 16:33
 */
public class CyclicBarrierExample1 {

	public static void main(String[] args) throws InterruptedException {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> System.out.println("收集到7颗龙珠,召唤神龙"));

		for (int i = 0; i < 7; i++) {
			final int temp = i + 1;
			new Thread(() ->{
				"".equals("");
				System.out.println(Thread.currentThread().getName() + "\t收集到第" + temp + "颗龙珠");
				try {
					Thread.sleep(500);
					int await = cyclicBarrier.await();
					int Wait = cyclicBarrier.getNumberWaiting();
					System.out.println(Thread.currentThread().getName()+"还剩几个:" + await);
					System.out.println(Thread.currentThread().getName()+"还剩几个在等待:" + Wait);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			},"线程" + String.valueOf(i)).start();
		}
	}
}

