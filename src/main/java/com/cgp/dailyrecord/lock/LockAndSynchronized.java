package com.cgp.dailyrecord.lock;

/**
 * 底层实现：
 *
 * Lock基于 AQS实现，通过state和一个CLH队列来维护锁的获取与释放
 * synchronized需要通过 monitor，经历一个从用户态到内核态的转变过程，更加耗时
 *
 *
 * 其他区别
 * synchronized	 是java内置关键字，在jvm层面,无法判断是否获取锁的状态,会自动释放锁,线程会一直等待下去
 * Lock  是个java类,可以判断是否获取到锁,需在finally中手工释放锁（unlock()方法释放锁），否则容易造成线程死锁,如果尝试获取不到锁，线程可以不用一直等待就结束
 * 总结来说 synchronized的锁可重入、不可中断、非公平。而Lock锁可重入、可判断、可公平（两者皆可）。
 *
 * @Author chenguopeng
 * @Date 2021/7/30 17:32
 */
public class LockAndSynchronized {

}
