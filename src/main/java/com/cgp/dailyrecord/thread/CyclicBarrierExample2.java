package com.cgp.dailyrecord.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author chenguopeng
 * @Date 2021/8/5 14:16
 */
public class CyclicBarrierExample2  implements Runnable{

	private static int counter = 0;
	private final int id = counter++;
	private int strides = 0;
	private static Random rand = new Random(47);
	private static CyclicBarrier barrier;

	public CyclicBarrierExample2(CyclicBarrier b) { barrier = b; }

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				synchronized(this) {
					//赛马每次随机跑几步
					strides += rand.nextInt(3);
				}
				barrier.await();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String tracks() {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < getStrides(); i++) {
			s.append("*");
		}
		s.append(id);
		return s.toString();
	}

	public synchronized int getStrides() { return strides; }
	public String toString() { return "CyclicBarrierExample2 " + id + " "; }

}

 class CyclicBarrierExample2Race implements Runnable {

	private static final int FINISH_LINE = 75;
	private static List<CyclicBarrierExample2> CyclicBarrierExample2s = new ArrayList<>();
	private static ExecutorService exec = Executors.newCachedThreadPool();

	@Override
	public void run() {
		StringBuilder s = new StringBuilder();
		//打印赛道边界
		for(int i = 0; i < FINISH_LINE; i++) {
			s.append("=");
		}
		System.out.println(s);
		//打印赛马轨迹
		for(CyclicBarrierExample2 CyclicBarrierExample2 : CyclicBarrierExample2s) {
			System.out.println(CyclicBarrierExample2.tracks());
		}
		//判断是否结束
		for(CyclicBarrierExample2 CyclicBarrierExample2 : CyclicBarrierExample2s) {
			if(CyclicBarrierExample2.getStrides() >= FINISH_LINE) {
				System.out.println(CyclicBarrierExample2 + "won!");
				exec.shutdownNow();
				return;
			}
		}
		//休息指定时间再到下一轮
		try {
			TimeUnit.MILLISECONDS.sleep(200);
		} catch(InterruptedException e) {
			System.out.println("barrier-action sleep interrupted");
		}
	}

	public static void main(String[] args) {
		CyclicBarrier barrier = new CyclicBarrier(7, new CyclicBarrierExample2Race());
		for(int i = 0; i < 7; i++) {
			CyclicBarrierExample2 CyclicBarrierExample2 = new CyclicBarrierExample2(barrier);
			CyclicBarrierExample2s.add(CyclicBarrierExample2);
			exec.execute(CyclicBarrierExample2);
		}
	}
}
