package com.elv.core.tool.application.citizen.util;

import com.elv.core.model.util.RandomCtrl;
import com.elv.core.util.Assert;
import com.elv.core.util.Dater;
import com.elv.core.util.RandomUtil;
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
    }

    /**
     * 解析身份证号
     *
     * @param id
     */
    public static void parse(String id) {
    }

    /**
     * 验证身份证
     *
     * @param id
     * @return boolean
     */
    public static boolean valid(String id) {
        if (id == null || id.length() != ID_LENGTH) {
            return false;
        }
        return fetchCheckDigit(id.substring(0, ID_LENGTH - 1)).equals(id.substring(ID_LENGTH - 1));
    }

    /**
     * 获取身份证校验码（最后一位）
     *
     * @param idTop17 身份证前17位
     * @return java.lang.String
     */
    private static String fetchCheckDigit(String idTop17) {
        Assert.isTrue(!StrUtil.isDigit(idTop17), "身份证入参异常：非数字");
        Assert.isTrue(idTop17.length() != ID_COEFFICIENTS.length, "身份证入参异常：长度不合法");

        int sum = 0;
        char[] chars = idTop17.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sum += (chars[i] - 48) * ID_COEFFICIENTS[i]; // 48表示'0'的ASCII码的十进制值
        }

        return ID_REMAINDERS[sum % ID_BASE];
    }

    public static String generate() {
        String addressNo = "341221";
        StringBuilder sb = new StringBuilder();
        sb.append(addressNo);
        sb.append(Dater.now().offsetYears(-20).getDateStr().replaceAll("-", ""));
        sb.append(RandomUtil.randomStr(RandomCtrl.builder().onlyDigit().length(3).build()));
        sb.append(fetchCheckDigit(sb.toString()));
        return sb.toString();
    }

}
