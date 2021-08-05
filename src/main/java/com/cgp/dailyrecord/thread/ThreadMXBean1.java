package com.cgp.dailyrecord.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import static javax.swing.text.html.HTML.Tag.A;


/**
 * java 中提供了可以检测死锁的工具类ThreadMXBean,我们可以用它来在项目运行时期使用代码去检测是否有死锁存在.
 * 下面这段代码请参考”理解java线程死锁”这篇文章,我们在其中添加了使用ThreadMXBean获取死锁信息的代码
 *
 * 1.检测死锁类的属性和方法
 *   //获取ThreadMXBean
 *   private final ThreadMXBean mBean = ManagementFactory.getThreadMXBean();
 *
 * 2.获取死锁线程
 *   // 查找因为等待获得对象监视器或可拥有同步器而处于死锁状态的线程循环。返回线程ID
 * long[] ids = mBean.findDeadlockedThreads();
 *
 * // 根据死锁线程id得到死锁线程信息，参数2代表的是获取同步监视器，后面获取线程锁定对象时使用
 * ThreadInfo[] threadInfos = mBean.getThreadInfo(ids , true, false);
 *
 *
 *
 * @Author chenguopeng
 * @Date 2021/8/4 17:41
 */
public class ThreadMXBean1 {

	public static final Object lock1 = new Object();//锁对象1
	public static final Object lock2 = new Object();//锁对象2
	//获取ThreadMXBean
	public static ThreadMXBean mbean = ManagementFactory.getThreadMXBean();

	public static void main(String[] args) {
		ThreadMXBean1 threadMXBean1=	new ThreadMXBean1();
		try {
			threadMXBean1.main1();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	//测试方法
	public void main1() throws InterruptedException {
		//启动死锁线程
		new Thread(()->{
			System.out.println(Thread.currentThread().getName()+"comming");
			A a=new A();
			a.getB();
		}).start();

		new Thread(()->{
			System.out.println(Thread.currentThread().getName()+"comming");
			B b=new B();
			b.getA();
		}).start();
		//等待一段时间再执行死锁检测

		Thread.sleep(1000*10);
//		//获取到所有死锁线程的id
		long[] deadlockedThreads =null;
//		//遍历数组获取所有的死锁线程详细堆栈信息并打印
		while ((deadlockedThreads = mbean.findDeadlockedThreads())!=null){
			for (long pid : deadlockedThreads) {
				//此方法获取不带有堆栈跟踪信息的线程数据
				//hreadInfo threadInfo = mbean.getThreadInfo(pid);
				//第二个参数指定转储多少项堆栈跟踪信息,设置为Integer.MAX_VALUE可以转储所有的堆栈跟踪信息
				ThreadInfo threadInfo = mbean.getThreadInfo(pid, Integer.MAX_VALUE);
				System.out.println(threadInfo);
			}
		}
	}

	class A{
		public  void getB()  {
			 B b=new B();
			synchronized (ThreadMXBean1.B.class){
				System.out.println(Thread.currentThread().getName());
				try {
					b.getA();
					Thread.sleep(1000*10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class B {
		public void getA()  {
			A a=new A();
			synchronized (ThreadMXBean1.A.class) {
				System.out.println(Thread.currentThread().getName());
				try {
					a.getB();
					Thread.sleep(1000*10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
