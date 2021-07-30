package com.cgp.dailyrecord.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 当多个线程需要访问某个公共资源的时候，我们知道需要通过加锁来保证资源的访问不会出问题。java提供了两种方式来加锁：
 * 一种是关键字： synchronized，一种是concurrent包下的基于API实现的。
 * synchronized是 JVM底层支持的，而concurrent包则是 jdk实现。
 *
 * 当有线程竞争锁时，当前线程会首先尝试获得锁而不是在队列中进行排队等候，这对于那些已经在队列中排队的线程来说显得不公平，这也是非公平锁的由来
 * 默认情况下为非公平锁。
 * 锁的存储结构就两个东西:"双向链表" + "int类型状态"。ReenTrantLock的实现是一种自旋锁，
 * 通过循环调用CAS操作来实现加锁。它的性能比较好也是因为避免了使线程进入内核态的阻塞状态。想尽办法避免线程进入内核的阻塞状态是我们去分析和理解锁设计的关键钥匙。
 *
 * 构造方法
 * public ReentrantLock()
 * public ReentrantLock(boolean fair)
 *
 * 获得锁
 * public void lock()
 * public void lockInterruptibly()  throws InterruptedException
 * public boolean tryLock()
 * public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException
 *
 * lock
 * 正常的获取锁，如果没有获得到锁，就会被阻塞
 *
 * lockInterruptibly
 * 获取锁，如果没有获得到锁，就会被阻塞
 * 可以被打断
 *
 * tryLock
 * 如果获得到锁，返回true
 * 如果没有获得到锁，返回false
 * timeout：表示等待的时间
 * tryLock()在获取的锁的时候，不会考虑此时是否有其他线程在等待，会破坏公平。
 * 如果你希望遵守公平设置此锁，然后用 tryLock(0, TimeUnit.SECONDS) 这几乎是等效的（它也检测中断）。
 *
 * getOwner
 * 返回持有锁的线程
 *
 * hasQueuedThreads
 * 是否有线程在等待获取锁
 *
 * getQueueLength
 * 获取等待锁的线程数目
 *
 *
 * getQueuedThreads
 * 返回正在等待的线程集合
 *
 * @Author chenguopeng
 * @Date 2021/7/30 17:24
 */
public class ReentrantLockExample1 {

	public static void main(String[] args) {
		ReentrantLock unfairLock = new ReentrantLock();
		ReentrantLock fairLock = new ReentrantLock(true);
	}


}
