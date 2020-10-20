package com.elv.core.util;

import java.util.Arrays;

/**
 * @author lxh
 * @since 2020-06-16
 */
public final class DigitUtil {

    /**
     * 均摊（尽可能均摊，方差最小）
     *
     * <pre>
     *     shareEqually(1000,7) 将返回[143,143,143,143,143,143,142]，而不是[142,142,142,142,142,142,148]
     * </pre>
     *
     * @param total 总数
     * @param size  均摊数量
     * @return long[]
     */
    public static long[] shareEqually(long total, int size) {
        Assert.isTrue(size <= 0, "DigitUtil#shareEqually invalid size param.");
        long avg = Math.floorDiv(total, size); // 均值
        long last = total - avg * (size - 1); // 最后一个数值
        boolean plusOne = last - avg > size / 2 ? true : false; // 是否加1

        int count = 0; // 计数
        long[] array = new long[size];
        for (int i = 0; i < size; i++) {
            if (i < size - 1) {
                array[i] = (plusOne && i < last - avg) ? avg + 1 : avg;
                count += array[i];
            } else {
                array[i] = total - count;
            }
        }

        return array;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(shareEqually(1000, 19)));
    }
}

