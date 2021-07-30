package com.cgp.dailyrecord.thread;

/**
 * 简介
 * countDownLatch这个类使一个线程等待其他线程各自执行完毕后再执行。
 * 是通过一个 state（相当于计数器）的东西来实现的，计数器的初始值是 线程的数量或者任务的数量。
 * 每当一个线程执行完毕后，计数器的值就-1，当计数器的值为0时，表示所有线程都执行完毕，然后在闭锁上等待的线程就可以恢复工作了。
 * CountDownLatch的方便之处在于，你可以在一个线程中使用， 也可以在多个线程上使用，一切只依据状态值，这样便不会受限于任何的场景。
 * <p>
 * 使用场景
 * 可能刚从数据库读取了一批数据
 * 利用并发处理这批数据
 * 当所有的数据处理完成后，再去执行后面的操作
 * <p>
 * 第一种：可以利用 join 的方法，但是在线程池中，比较麻烦。
 * 第二种：利用线程池的awaitTermination，阻塞一段时间。
 * 当使用awaitTermination时，主线程会处于一种等待的状态，等待线程池中所有的线程都运行完毕后才继续运行。
 * 第三种：利用CountDownLatch，每当任务完成一个，就计数器减一。
 * <p>
 * countdown只会阻塞调用者，其它线程干完任务就可以干其他事。这里的调用者线程就是main线程。
 *
 * @Author chenguopeng
 * @Date 2021/7/30 16:02
 */

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch是一次性的，计算器的值只能在构造方法中初始化一次，之后没有任何机制再次对其设置值，当CountDownLatch使用完毕后，它不能再次被使用。
 *
 * @Author: youthlql-吕
 * @Date: 2019/9/26 10:05
 * <p>
 * 功能描述:
 */
public class CountDownLatchExample1 {

	public static void main(String[] args) throws InterruptedException {
		int threadNum = 6;
		CountDownLatch countDownLatch = new CountDownLatch(threadNum);

		for (int i = 0; i < threadNum; i++) {
			new Thread(() -> {
				//计数器-1
				try {
					Thread.sleep(1000 * 20);
					System.out.println("\t\t" + Thread.currentThread().getName() + "处理完毕~~~");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					countDownLatch.countDown();
				}
				System.out.println("非调用者线程-" + Thread.currentThread().getName() + "-还可以干点其他事");
			}, Country.forEach_Country(i + 1).getCountryName()).start();
		}
		if (!countDownLatch.await(2, TimeUnit.SECONDS)){
			System.out.println("子线程执行超时,子线程继续执行,释放主线程");
		}else {
			System.out.println("子线程执行完毕");
		}
		//设置超时时间，如果超时后，子线程继续执行，但是会释放主线程
//		countDownLatch.await(10000, TimeUnit.DAYS);
		System.out.println("-----------------------------");
		System.out.println("\t 所有任务都已经处理完毕，可以往后执行了！");
	}
}

enum Country {

	ONE(1, "1号任务"),
	TWO(2, "2号任务"),
	THREE(3, "3号任务"),
	FOUR(4, "4号任务"),
	FIVE(5, "5号任务"),
	SIX(6, "6号任务");

	private Integer index;
	private String countryName;


	public static Country forEach_Country(Integer index) {
		Country[] values = Country.values();
		for (Country c : values) {
			if (c.getIndex() == index) {
				return c;
			}
		}
		return null;
	}

	Country(Integer index, String countryName) {
		this.index = index;
		this.countryName = countryName;
	}

	public Integer getIndex() {
		return index;
	}

	public String getCountryName() {
		return countryName;
	}

}

