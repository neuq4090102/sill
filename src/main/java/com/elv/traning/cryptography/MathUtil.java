package com.elv.traning.cryptography;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.elv.core.util.Dater;
import com.elv.core.util.JsonUtil;

/**
 * @author lxh
 * @since 2020-06-23
 */
public class MathUtil {

    public static void main(String[] args) {
        // int a = RandomUtil.randomInt(100000000);
        // int b = RandomUtil.randomInt(100000000);
        int a = 425496728;
        int b = 328343286;
        System.out.println("a=" + a + ",b=" + b);
        testDivide(a, b);
        testSubtract(a, b);
        testSubtractPlus(a, b);

        getDivisor(10);

    }

    private static void testDivide(int a, int b) {
        Dater beginDate = Dater.now();

        // int result = tossDivide(a, b);
        Context context = new Context(new TossDivide());
        int result = context.gcd(a, b);
        Dater endDater = Dater.now();
        System.out.print("divide's result = " + result + ", cost:");
        System.out.println((endDater.getInstant().toEpochMilli() - beginDate.getInstant().toEpochMilli()) + "ms");
    }

    private static void testSubtract(int a, int b) {
        Dater beginDate = Dater.now();
        // int result = persistentSubtract(a, b);
        Context context = new Context(new PersistentSubtract());
        int result = context.gcd(a, b);
        Dater endDater = Dater.now();
        System.out.print("subtract's result = " + result + ", cost:");
        System.out.println((endDater.getInstant().toEpochMilli() - beginDate.getInstant().toEpochMilli()) + "ms");
    }

    private static void testSubtractPlus(int a, int b) {
        Dater beginDate = Dater.now();
        // int result = persistentSubtractPlus(a, b);

        Context context = new Context(new PersistentSubtractPlus());
        int result = context.gcd(a, b);

        Dater endDater = Dater.now();
        System.out.print("subtractPlus's result = " + result + ", cost:");
        System.out.println((endDater.getInstant().toEpochMilli() - beginDate.getInstant().toEpochMilli()) + "ms");
    }

    /**
     * 最小公倍数(Least Common multiple)[a,b]
     * <p>a * b = (a,b) * [a,b]</p>
     *
     * @param a 参数a
     * @param b 参数b
     * @return int
     */
    public static int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    /**
     * 最大公约数（Greatest Common Divisor）(a,b)
     *
     * @param a 参数a
     * @param b 参数b
     * @return int
     */
    public static int gcd(int a, int b) {
        return gcd(a, b, GCDSolution.SUBTRACTION_PLUS);
    }

    /**
     * 最大公约数（Greatest Common Divisor）
     *
     * @param a        参数a
     * @param b        参数b
     * @param solution 最大公约数求解方式
     * @return int
     */
    public static int gcd(int a, int b, GCDSolution solution) {
        check(a, b);
        if (solution == GCDSolution.DIVISION) {
            return tossDivide(a, b);
        } else if (solution == GCDSolution.SUBTRACTION) {
            return persistentSubtract(a, b);
        }
        return persistentSubtractPlus(a, b);
    }

    /**
     * 辗转相除法（欧几里得算法）
     *
     * @param a 参数a
     * @param b 参数b
     * @return int
     */
    private static int tossDivide(int a, int b) {
        check(a, b);
        return recursiveDivide(a, b);
    }

    private static int recursiveDivide(int a, int b) {
        int max = a;
        int min = b;
        if (b > a) {
            max = b;
            min = a;
        }

        if (max % min == 0) {
            return min;
        }
        return recursiveDivide(max % min, min);
    }

    /**
     * 更相减损术
     *
     * @param a 参数a
     * @param b 参数b
     * @return int
     */
    private static int persistentSubtract(int a, int b) {
        check(a, b);
        return recursiveSubtract(a, b);
    }

    private static int recursiveSubtract(int a, int b) {

        if (a == b) {
            return a;
        }

        int max = a;
        int min = b;
        if (b > a) {
            max = b;
            min = a;
        }

        return recursiveSubtract(max - min, min);
    }

    /**
     * 更相减损术(优化版)
     *
     * @param a 参数a
     * @param b 参数b
     * @return int
     */
    private static int persistentSubtractPlus(int a, int b) {
        check(a, b);
        return recursiveSubtractPlus(a, b);
    }

    private static int recursiveSubtractPlus(int a, int b) {
        if (a == b) {
            return a;
        }
        if (isEven(a) && isEven(b)) {
            return recursiveSubtractPlus(a >> 1, b >> 1) << 1;
        } else if (isEven(a)) {
            return recursiveSubtractPlus(a >> 1, b);
        } else if (isEven(b)) {
            return recursiveSubtractPlus(a, b >> 1);
        } else {
            int max = a;
            int min = b;
            if (b > a) {
                max = b;
                min = a;
            }
            return persistentSubtractPlus(max - min, min);
        }
    }

    /**
     * 是否是偶数
     *
     * @param num 数字
     * @return boolean
     */
    public static boolean isEven(int num) {
        return (num & 1) == 0;
    }

    /**
     * 是否是奇数
     *
     * @param num 数字
     * @return boolean
     */
    public static boolean isOdd(int num) {
        return (num & 1) == 1;
    }

    /**
     * 因数
     *
     * @param num 数字
     * @return java.util.List
     */
    public static List<Integer> getDivisor(int num) {
        check(num, 1);

        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                list.add(i);
                if (num != i * i) {
                    list.add(num / i);
                }
            }
        }

        list = list.stream().sorted().collect(Collectors.toList());
        System.out.println(num + "'s divisor has:" + JsonUtil.toJson(list));
        return list;
    }

    private static void check(int a, int b) {
        if (a <= 0 || b <= 0) {
            throw new RuntimeException("param invalid, please confirm whether it is positive integer.");
        }
    }

    /**
     * 最大公约数求解方式
     * <p>另外还有【因数分解法】和【短除法】</p>
     */
    public enum GCDSolution {
        DIVISION, // 辗转相除法（欧几里得算法）
        SUBTRACTION, // 更相减损术
        SUBTRACTION_PLUS, // 更相减损术 + 移位
    }

    /**
     * 策略模式实现求GCD
     */
    interface IGcdSolution {
        int gcd(int a, int b);
    }

    static class TossDivide implements IGcdSolution {

        @Override
        public int gcd(int a, int b) {
            return tossDivide(a, b);
        }
    }

    static class PersistentSubtract implements IGcdSolution {

        @Override
        public int gcd(int a, int b) {
            return persistentSubtract(a, b);
        }
    }

    static class PersistentSubtractPlus implements IGcdSolution {

        @Override
        public int gcd(int a, int b) {
            return persistentSubtractPlus(a, b);
        }
    }

    static class Context {
        private IGcdSolution gcdSolution;

        public Context(IGcdSolution gcdSolution) {
            this.gcdSolution = gcdSolution;
        }

        public int gcd(int a, int b) {
            return gcdSolution.gcd(a, b);
        }
    }

}
