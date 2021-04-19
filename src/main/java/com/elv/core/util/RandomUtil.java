package com.elv.core.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.elv.core.model.util.RandomCtrl;

/**
 * @author lxh
 * @see org.apache.commons.lang3.RandomStringUtils
 * @since 2020-06-01
 */
public final class RandomUtil {

    private static final Random random;
    private static final List<String> digits;
    private static final List<String> lowerCaseLetters;
    private static final List<String> upperCaseLetters;

    static {
        random = new Random();
        digits = Stream.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9").collect(Collectors.toList());
        lowerCaseLetters = Stream
                .of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                        "u", "v", "w", "x", "y", "z").collect(Collectors.toList());
        upperCaseLetters = Stream
                .of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                        "U", "V", "W", "X", "Y", "Z").collect(Collectors.toList());
    }

    private RandomUtil() {
    }

    public static int randomInt() {
        return random.nextInt();
    }

    public static int randomInt(int bound) {
        if (bound <= 0) {
            bound = 100;
        }
        return random.nextInt(bound);
    }

    public static long randomLong() {
        return random.nextLong();
    }

    public static float randomFloat() {
        return random.nextFloat();
    }

    public static double randomDouble() {
        return randomDouble(1.0, 2);
    }

    public static double randomDouble(double upperBound) {
        return randomDouble(upperBound, 2);
    }

    /**
     * 随机数
     *
     * @param upperBound 上限
     * @param floatPoint 浮点（小数点保留位数）
     * @return double
     */
    public static double randomDouble(double upperBound, int floatPoint) {
        if (upperBound <= 0.0d) {
            upperBound = 1.0d;
        }

        BigDecimal bigDecimal = new BigDecimal((random.nextDouble() * upperBound) + "")
                .setScale(floatPoint, BigDecimal.ROUND_HALF_UP);

        return bigDecimal.doubleValue();
    }

    /**
     * 随机字符串-含数字/字母大小写
     *
     * @param length 字符串长度
     * @return java.lang.String
     */
    public static String randomStr(int length) {
        return randomStr(RandomCtrl.builder().length(length).build());
    }

    /**
     * 随机字符串
     *
     * @param length 字符串长度
     * @param digit  是否含数字
     * @return java.lang.String
     */
    public static String randomStr(int length, boolean digit) {
        return randomStr(RandomCtrl.builder().length(length).digit(digit).build());
    }

    /**
     * 随机字符串-默认含数字/字母大小写
     * <p>百万个随机数，秒出</p>
     *
     * @param ctrl 随机参数控制
     * @return java.lang.String
     */

    public static String randomStr(RandomCtrl ctrl) {
        List<String> list = new ArrayList<>();
        if (ctrl.isLowerCaseLetter()) {
            list.addAll(lowerCaseLetters);
        }

        if (ctrl.isUpperCaseLetter()) {
            list.addAll(upperCaseLetters);
        }

        if (ctrl.isDigit()) {
            list.addAll(digits);
        }

        // 打乱顺序-此处耗性能（约占2/3的执行时间）
        // Collections.shuffle(list);

        int length = ctrl.getLength();
        StringBuilder sb = new StringBuilder();
        while (length > 0) {
            sb.append(list.get(randomInt(list.size())));
            length--;
        }

        return sb.toString();
    }

    /**
     * 随机手机号
     *
     * @return java.lang.String
     */
    public static String randomMobile() {
        return randomPhone(false, true);
    }

    /**
     * 随机手机号或电话号
     *
     * @param countryCode 是否有国别码
     * @param cellPhone   是否是手机号
     * @return java.lang.String
     */

    public static String randomPhone(boolean countryCode, boolean cellPhone) {
        StringBuilder sb = new StringBuilder();
        if (countryCode) {
            sb.append("86-");
        }

        if (cellPhone) {
            sb.append("152");
            sb.append(randomStr(RandomCtrl.builder().length(8).letter(false).build()));
        } else {
            sb.append("010-");
            sb.append(randomStr(RandomCtrl.builder().length(8).letter(false).build()));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Dater beginDate = Dater.now();
        // int loop = 100;
        int loop = 1000000;
        for (int i = 0; i < loop; i++) {
            randomStr(50);
            // System.out.println(randomStr(16));
        }
        Dater endDater = Dater.now();
        System.out.println(endDater.getInstant().toEpochMilli() - beginDate.getInstant().toEpochMilli());

        System.out.println(randomMobile());
    }

}

