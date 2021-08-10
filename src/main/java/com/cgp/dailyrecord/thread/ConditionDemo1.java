package com.cgp.dailyrecord.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition版生产者消费者
 *
 * @Author chenguopeng
 * @Date 2021/8/9 17:55
 * 功能描述: 使用Lock和Condition实现
 */
public class ConditionDemo1 {
	public static void main(String[] args) {
		Consumer4 consumer = new Consumer4();

		//生产者线程A
		new Thread(() ->{
			for (int i = 0; i < 10; i++) {
				try {
					consumer.increment();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		},"生产者A").start();

		new Thread(() ->{
			for (int i = 0; i < 10; i++) {
				try {
					consumer.decrement();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		},"消费者B").start();

		new Thread(() ->{
			for (int i = 0; i < 10; i++) {
				try {
					consumer.increment();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		},"生产者C").start();

		new Thread(() ->{
			for (int i = 0; i < 10; i++) {
				try {
					consumer.decrement();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		},"消费者D").start();
	}
}
class Consumer4{
	private volatile Integer num = 0;
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();

	public  void increment() throws InterruptedException {
		lock.lock();
		try {
			while(num != 0){
				condition.await();
			}
			num++;
			System.out.println(Thread.currentThread().getName() + "\t" + num);
			condition.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	public  void decrement() throws InterruptedException {
		lock.lock();
		try {
			while(num == 0){
				condition.await();
			}
			num--;
			System.out.println(Thread.currentThread().getName() + "\t" + num);
			condition.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

}

