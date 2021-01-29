package com.elv.core.tool.application.citizen;

import com.elv.core.util.Assert;
import com.elv.core.util.StrUtil;

/**
 * 18位身份证工具
 *
 * @author lxh
 * @see <a href="http://www.gb688.cn/bzgk/gb/newGbInfo?hcno=080D6FBF2BB468F9007657F26D60013E">公民身份号码</a>
 * @since 2021-01-20
 */
public class IDCardUtil {

    /**
     * 身份证长度
     */
    private static final int ID_LENGTH = 18;

    /**
     * 基数
     */
    private static final int ID_BASE = 11;

    /**
     * 前17位系数
     */
    private static final int[] ID_COEFFICIENTS = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

    /**
     * 余数校验位（余数0表示1，余数1表示0，余数2表示X...）
     */
    private static final String[] ID_REMAINDERS = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };

    private IDCardUtil() {
    }

    public static void main(String[] args) {
        // String id = "53010219200508011X";
        String id = "341221202101200288";
        parse(id);
        System.out.println(valid(id));

        // for (int i = 0; i < 5; i++) {
        //     System.out.println(generate());
        // }
    }

    public static void parse(String id) {
        System.out.println(id.length());
    }

    public static boolean valid(String id) {
        if (id == null || id.length() != ID_LENGTH) {
            return false;
        }
        return fetchCheckDigit(id.substring(0, ID_LENGTH - 1)).equals(id.substring(ID_LENGTH - 1));
    }

    private static String fetchCheckDigit(String id) {
        Assert.isTrue(!StrUtil.isDigit(id), "身份证入参异常：非数字");
        Assert.isTrue(id.length() != ID_COEFFICIENTS.length, "身份证入参异常：长度不合法");

        int sum = 0;
        char[] chars = id.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sum += (chars[i] - 48) * ID_COEFFICIENTS[i]; // 48表示'0'的ASCII码的十进制值
        }

        return ID_REMAINDERS[sum % ID_BASE];
    }

}
