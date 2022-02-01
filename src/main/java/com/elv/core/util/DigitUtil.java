package com.elv.core.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

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

    public static String toRmbUpper(String refNum) {
        return RMBUtil.toRmbUpper(refNum);
    }

    private static class RMBUtil {
        private static final String[] uppers = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" }; // 大写
        private static final String[] units = { "仟", "佰", "拾", "万", "仟", "佰", "拾", "亿", "仟", "佰", "拾", "万", "仟", "佰",
                "拾", "元", "角", "分", "厘", "毫" }; // 单位

        /**
         * 转人民币大写
         * <p>
         * 规范简述：
         * 1.阿拉伯数字中间连续有多个"0"时、中文大写金额中间可以只写一个"零"字，如￥6007.14应写成人民币陆仟零柒元壹角肆分；
         * 2.中文大写金额数字到"元"为止的，在"元"之后、应写"整"字;在"角"之后，可以不写"整"字;大写金额数字有"分"的，"分"后面不写"整"字；
         *
         * </p>
         *
         * @param refNum
         * @return java.lang.String
         */
        private static String toRmbUpper(String refNum) {
            // 参数检查
            Assert.notBlank(refNum, "#toUpper");
            Assert.isTrue(!StrUtil.isDigit(refNum), "数字非法");

            // 规范格式（小数位不够则补全，超过max则截取）
            int mode = refNum.startsWith("9999999999999999.9999") ? BigDecimal.ROUND_FLOOR : BigDecimal.ROUND_HALF_UP;
            BigDecimal num = new BigDecimal(refNum).setScale(4, mode);
            Assert.isTrue(num.doubleValue() < 0, "不支持负数");
            Assert.isTrue(num.toString().length() > units.length + 1, "长度非法");

            final String ZERO = uppers[0];
            List<String> upperList = Arrays.asList(uppers);
            String numStr = num.toString().replaceAll("\\.", "");

            Stack<String> stack = new Stack<>();
            for (int i = 0; i < numStr.length(); i++) {
                String upper = uppers[numStr.charAt(numStr.length() - i - 1) - '0'];
                String unit = units[units.length - i - 1];
                String peek = stack.isEmpty() ? "" : stack.peek();
                if (units[11].equals(peek) && units[7].equals(unit)) { // 解决"亿万元"->"亿元"问题
                    stack.pop();
                }
                if (ZERO.equals(upper)) {
                    if (!ZERO.equals(peek) && upperList.contains(peek)) {
                        stack.push(upper);
                    }
                    if (i > 0 && i % 4 == 0) { // i == 4 || i == 8 || i == 12 || i == 16
                        stack.push(unit);
                    }
                    continue;
                }
                stack.push(unit);
                stack.push(upper);
            }

            // 收尾检查
            while (!stack.isEmpty()) {
                String peek = stack.peek();
                if (!ZERO.equals(peek) && upperList.contains(peek)) {
                    break;
                }
                stack.pop();
            }

            // 回显
            if (stack.isEmpty()) {
                return "零元整";
            }

            Collections.reverse(stack);
            boolean hasDecimal = num.remainder(BigDecimal.ONE).doubleValue() != 0; // 是否有小数
            return stack.stream().collect(Collectors.joining("")) + (hasDecimal ? "" : "整");
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(shareEqually(1000, 19)));
        System.out.println(toRmbUpper("234230.03"));
    }
}

