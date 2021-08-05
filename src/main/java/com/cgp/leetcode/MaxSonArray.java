package com.cgp.leetcode;

/**
 *
 * 最大子序列
 * @Author chenguopeng
 * @Date 2021/8/3 9:37
 */
public class MaxSonArray {


	public static  int getMaxSonArray(int[] a) {
		int left = 0, max = 0;
		for (int i = 0; i < a.length; i++) {
			left += a[i];
			if (left > max) {
				max = left;
			}
		}
		return max;
	}

	public static void main(String[] args) {
		int[] a={1,2,3,4,-5};
		System.out.println(getMaxSonArray(a));
	}
}
