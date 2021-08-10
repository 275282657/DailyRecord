package com.cgp.dailyrecord.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * Condition主要是用于线程通信的，也就是和Object类的wait，notify有同样的功能。不过Condition的功能更加多样，Conditon可以绑定锁，实现选择性唤醒。
 * Condition和Lock搭配使用; condition必须使用lock.newCondition();来创建condition。必须存放在Lock中。否则抛出异常。
 *
 * @Author chenguopeng
 * @Date 2021/8/9 17:48
 * 功能描述: A线程打印3次,B线程打印6次,C线程9次，持续两轮
 */
public class ConditionDemo {

	public static void main(String[] args) {
		ShareResource shareResource = new ShareResource();
		new Thread(() -> {
			for (int i = 0; i < 2; i++) {
				shareResource.print3();
			}
		}, "A").start();

		new Thread(() -> {
			for (int i = 0; i < 2; i++) {
				shareResource.print6();
			}
		}, "B").start();

		new Thread(() -> {
			for (int i = 0; i < 2; i++) {
				shareResource.print9();
			}
		}, "C").start();


	}
}

//以前是一个lock只有一个钥匙,现在是一个lock多个钥匙
class ShareResource {
	//标志位,可取值为1,2,3
	private volatile Integer flag = 1;
	private Lock lock = new ReentrantLock();
	private Condition c1 = lock.newCondition();
	private Condition c2 = lock.newCondition();
	private Condition c3 = lock.newCondition();

	public void print3() {
		lock.lock();
		try {
			while (flag != 1) {
				c1.await();
			}
			for (int i = 0; i < 3; i++) {
				System.out.println(Thread.currentThread().getName() + "\t" + (i + 1));
			}
			flag = 2;
			c2.signal();//唤醒2号线程
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void print6() {
		lock.lock();
		try {
			while (flag != 2) {
				c2.await();
			}
			for (int i = 0; i < 6; i++) {
				System.out.println(Thread.currentThread().getName() + "\t" + (i + 1));
			}
			flag = 3;
			c3.signal();//唤醒3号线程
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}


	public void print9() {
		lock.lock();
		try {
			while (flag != 3) {
				c3.await();
			}
			for (int i = 0; i < 9; i++) {
				System.out.println(Thread.currentThread().getName() + "\t" + (i + 1));
			}
			flag = 1;
			c1.signal();//唤醒1号线程
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}


}
