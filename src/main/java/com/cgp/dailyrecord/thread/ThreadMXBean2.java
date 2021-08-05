package com.cgp.dailyrecord.thread;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 在使用JConsole的时候，在线程页下，可以看到一个检测死锁按钮，很好奇它是如何获取死锁线程的。
 * 同时检测死锁算法也是操作系统的重要算法之一，本文在参考了JConsole源码的基础上来实现检测死锁的功能。
 * 1.检测死锁类的属性和方法
 *   //获取ThreadMXBean
 *   private final ThreadMXBean mBean = ManagementFactory.getThreadMXBean();
 *
 *   //处理检测到的死锁线程
 *   public void handleDeadLock(ThreadInfo[] deadLockThreads)；
 *
 *   //得到线程锁定的对象
 *   public void getThreadLock(long selected)；
 *
 *   //得到线程和线程所在的组，K为线程Id，V为所在的组，组以死锁划分，一个死锁为一组
 *   public Map getDeadlockedGroup() throws IOException；
 *
 * 2.获取死锁线程
 * // 查找因为等待获得对象监视器或可拥有同步器而处于死锁状态的线程循环。返回线程ID
 * long[] ids = mBean.findDeadlockedThreads();
 *
 * // 根据死锁线程id得到死锁线程信息，参数2代表的是获取同步监视器，后面获取线程锁定对象时使用
 * ThreadInfo[] threadInfos = mBean.getThreadInfo(ids , true, false);
 *
 * @Author chenguopeng
 * @Date 2021/8/5 9:44
 */
public class ThreadMXBean2 {

	// private final DeadlockHandler deadlockHandler;
	private final long period;
	private final TimeUnit unit;
	private final ThreadMXBean mBean = ManagementFactory.getThreadMXBean();

	private final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);

	public ThreadMXBean2(final long period, final TimeUnit unit) {
		this.period = period;
		this.unit = unit;
	}

	public static void main(String[] args) throws InterruptedException {
		ThreadMXBean2 threadMXBean2=new ThreadMXBean2(1,TimeUnit.MINUTES);
		threadMXBean2.runDeadLock();
		Thread.sleep(1000L);
		threadMXBean2.deadlockCheck.run();
	}

	/**
	 * 启动死锁线程
	 */
	public void runDeadLock(){
		//启动死锁线程
		new Thread(()->{
			System.out.println(Thread.currentThread().getName()+"comming");
			ThreadMXBean2.A a=new ThreadMXBean2.A();
			a.getB();
		}).start();

		new Thread(()->{
			System.out.println(Thread.currentThread().getName()+"comming");
			ThreadMXBean2.B b=new ThreadMXBean2.B();
			b.getA();
		}).start();
	}

	final Runnable deadlockCheck = new Runnable() {

		@Override
		public void run() {
			long[] deadlockedThreads = ThreadMXBean2.this.mBean.findDeadlockedThreads(); /// 查找因为等待获得对象监视器或可拥有同步器而处于死锁状态的线程循环。返回线程ID

			ThreadInfo[] threadInfos = ThreadMXBean2.this.mBean.getThreadInfo(deadlockedThreads, true, false); // 根据死锁线程id得到死锁线程信息

			// 输出死锁信息
			handleDeadLock(threadInfos);

			Map map = new HashMap();
			try {
				map = getDeadlockedThreadIds();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (Object in : map.keySet()) {
				// map.keySet()返回的是所有key的值
				Integer gid = (Integer) map.get(in);// 得到每个key多对用value的值
				System.out.println("线程id:" + in + "     死锁" + gid);
			}
		}

	};

	public void start() {
		this.schedule.schedule(this.deadlockCheck, this.period, this.unit);
	}

	/**
	 * 3.处理死锁线程
	 * @param deadLockThreads
	 */
	// 得到线程的所有信息
	public void handleDeadLock(ThreadInfo[] deadLockThreads) {
		if (deadLockThreads != null) {
			System.err.println("Deadlock detected!");
			for (ThreadInfo threadInfo : deadLockThreads) {
				if (threadInfo != null) {
					// SecurityException
					for (Thread thread : Thread.getAllStackTraces().keySet()) {
						if (thread.getId() == threadInfo.getThreadId()) {
							System.out.println();
							System.out.println("id:" + threadInfo.getThreadId());
							System.out.println("名称:" + threadInfo.getThreadName());
							System.out.print("状态：" + threadInfo.getLockName());
							System.out.println("上的" + threadInfo.getThreadState());
							System.out.println("拥有者:" + threadInfo.getLockOwnerName());
							System.out.println("总阻止数:" + threadInfo.getBlockedCount());
							System.out.println("总等待数:" + threadInfo.getWaitedCount());
							System.out.println("状态：" + threadInfo.toString());

							String name = mBean.getThreadInfo(threadInfo.getLockOwnerId()).getLockName();
							System.out.println("已锁定:" + name);
							int i = 0;

							MonitorInfo[] monitors = threadInfo.getLockedMonitors();

							for (StackTraceElement ste : thread.getStackTrace()) {
								System.err.println("堆栈深度:"+thread.getStackTrace().length);
								System.err.println("堆栈信息:"+ste.toString());

								System.out.println("拼接的堆栈信息:"+ste.getClassName() + ste.getMethodName() + ste.getFileName()
										                   + ste.getLineNumber());
								selectThread(threadInfo.getThreadId());
								if (monitors != null) {
									for (MonitorInfo mi : monitors) {

										//如果不加深度判断,会出现多余的锁定对象,原因不明
										if (mi.getLockedStackDepth() == 0) {
											System.out.println("已锁定1:"+mi.toString());
										}
									}
								} else {
									System.out.println("monitor为空");
								}

							}
						}

					}
					System.out.println("==================");
				}
			}

		} else {
			System.out.println("未检测到死锁线程");
		}

	}

	// 得到线程锁定的对象
	public void selectThread(long selected) {
		final long threadID = selected;
		StringBuilder sb = new StringBuilder();
		ThreadInfo ti = null;
		MonitorInfo[] monitors = null;
		if (mBean.isObjectMonitorUsageSupported()) {
			// VMs that support the monitor usage monitoring
			// ThreadInfo[] infos = mBean.dumpAllThreads(true, false);
			// //返回所有活动线程的线程信息，并带有堆栈跟踪和同步信息 如果为 true，则转储所有锁定的监视器。
			long[] deadlockedThreads = ThreadMXBean2.this.mBean.findDeadlockedThreads(); /// 查找因为等待获得对象监视器或可拥有同步器而处于死锁状态的线程循环。返回线程ID

			ThreadInfo[] infos = ThreadMXBean2.this.mBean.getThreadInfo(deadlockedThreads, true, false); // 根据死锁线程id得到死锁线程信息

			for (ThreadInfo info : infos) {
				if (info.getThreadId() == threadID) {
					ti = info;
					monitors = info.getLockedMonitors();
					break;
				}
			}
			System.out.println("support");
		} else {
			// VM doesn't support monitor usage monitoring
			ti = mBean.getThreadInfo(threadID, Integer.MAX_VALUE);
		}
		if (ti != null) {
			int index = 0;
			if (monitors != null) {
				for (MonitorInfo mi : monitors) {
					if (mi.getLockedStackDepth() == index) {
						System.out.println("已锁定2:" + mi.toString());
					}
				}
				index++;
			}
		}

	}

	/**
	 * 4.得到线程锁定的对象
	 * 首先需要得到线程对象，mBean.getThreadInfo(deadlockedThreads, true, false)方法的第二个参数为true表示获得同步监视器，
	 * @return
	 * @throws IOException
	 */
	// 得到死锁个数, key为线程id，value为所属的死锁组
	public Map getDeadlockedThreadIds() throws IOException {

		long[] ids = mBean.findDeadlockedThreads();
		if (ids == null) {
			return null;
		}
		ThreadInfo[] infos = mBean.getThreadInfo(ids, Integer.MAX_VALUE);

		List<Long[]> dcycles = new ArrayList<Long[]>();
		List<Long> cycle = new ArrayList<Long>();
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		int gid = 1;

		// keep track of which thread is visited
		// one thread can only be in one cycle
		boolean[] visited = new boolean[ids.length];

		int index = -1; // Index into arrays
		while (true) {
			if (index < 0) {
				if (map.size() > 0) {
					// a cycle found
					// dcycles.add(cycle.toArray(new Long[0]));
					gid++;
					// cycle = new ArrayList<Long>();
				}
				// start a new cycle from a non-visited thread
				for (int j = 0; j < ids.length; j++) {
					if (!visited[j]) {
						index = j;
						visited[j] = true;
						break;
					}
				}

				// 当所有线程均被访问过,退出while循环
				if (index < 0) {
					// done
					break;
				}
			}

			// cycle.add(ids[index]);
			map.put(ids[index], gid);
			long nextThreadId = infos[index].getLockOwnerId();

			for (int j = 0; j < ids.length; j++) {
				ThreadInfo ti = infos[j];
				if (ti.getThreadId() == nextThreadId) {
					if (visited[j]) {
						index = -1;
					} else {
						index = j;
						visited[j] = true;
					}
					break;
				}
			}
		}
		// 返回二维数组，第一维度是死锁分组，第二位都是死锁下的线程，length就可获得死锁个数
		// return dcycles.toArray(new Long[0][0]);
		return map;
	}

	class A{
		public  void getB()  {
			ThreadMXBean2.B b=new ThreadMXBean2.B();
			synchronized (ThreadMXBean2.B.class){
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
			ThreadMXBean2.A a=new ThreadMXBean2.A();
			synchronized (ThreadMXBean2.A.class) {
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