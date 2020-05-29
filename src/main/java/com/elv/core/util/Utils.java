package com.elv.core.util;

import com.elv.frame.model.Blur;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author lxh
 * @date 2020-03-23
 */
public class Utils {

    private static final Pattern numPattern = Pattern.compile("^-{0,1}\\d+\\.{0,1}\\d*$");

    private static final Pattern mailPattern = Pattern
            .compile("^[a-zA-Z0-9_\\-\\.]+@[a-zA-Z0-9\\-]+(\\.[a-zA-Z0-9\\-]+){0,2}\\.((com)|(cn)|(net))$");

    public static boolean isNum(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return numPattern.matcher(str.trim()).matches();
    }

    public static boolean isEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        return mailPattern.matcher(email).matches();
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String initialUp(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 字符串脱敏
     * <pre>
     *  脱敏对象str = "abcdefg"
     *
     *  字节编码：
     *   0  1  2  3  4  5  6
     *   a  b  c  d  e  f  g
     *  -7 -6 -5 -4 -3 -2 -1
     *
     *  1.按照比例脱敏
     *      Blur.builder().fromIdx(2).ratio(1.0d).build()   ==> ab*****
     *      Blur.builder().fromIdx(-2).ratio(1.0d).build()  ==> ******g
     *
     *  2.按照固定步长脱敏
     *      Blur.builder().fromIdx(2).stepSize(4).build()   ==> ab*****
     *      Blur.builder().fromIdx(2).stepSize(0).build()   ==> ab*defg
     *      Blur.builder().fromIdx(-2).stepSize(4).build()  ==> a*****g
     *      Blur.builder().fromIdx(-2).stepSize(0).build()  ==> abcde*g
     *
     *  3.按照索引脱敏
     *      3.1.中间数据脱敏
     *      Blur.builder().fromIdx(2).stepSize(4).build()   ==> ab***fg
     *      Blur.builder().fromIdx(4).stepSize(2).build()   ==> ab***fg
     *      Blur.builder().fromIdx(-3).stepSize(-4).build() ==> abc**fg
     *      Blur.builder().fromIdx(-4).stepSize(-3).build() ==> abc**fg
     *
     *      3.2 两头数据脱敏
     *      Blur.builder().fromIdx(-2).stepSize(1).build()  ==> **cde**
     *      Blur.builder().fromIdx(0).stepSize(-2).build()  ==> *bcde**
     *
     *  4.修改掩码符号
     *      Blur.builder().ratio(0.8).mask("#").build()     ==> ######g
     * </pre>
     *
     * @param str
     * @param blur
     * @return
     */
    public static String blur(Object object, Blur blur) {
        if (object == null) {
            return "";
        }
        String str = String.valueOf(object);
        if (StringUtils.isBlank(str)) {
            return str;
        }

        int length = str.length();
        int fromIdx = blur.getFromIdx();
        int toIdx = blur.getToIdx();
        int stepSize = blur.getStepSize();
        double ratio = blur.getRatio();

        //优先使用比例参数
        if (ratio > 0 && ratio <= 1.0d) {
            stepSize = (int) Math.floor(length * ratio); //向下取整，比例换步长
            blur.setResetStepSize(true);
        }

        //其次使用步长参数
        if (blur.isResetStepSize() || stepSize > 0) {
            stepSize = Math.max(0, stepSize);
            toIdx = fromIdx < 0 ?
                    Math.max(fromIdx - stepSize, 0 - length) :
                    Math.min(fromIdx + stepSize, length - 1); //步长换toIdx
        }

        return desensitize(str, fromIdx, toIdx, blur.getMask());
    }

    private static String desensitize(String str, int from, int to, String mask) {
        int length = str.length();
        int fromIdx = from >= length - 1 ? length - 1 : from <= -length ? -length : from;
        int toIdx = to >= length - 1 ? length - 1 : to <= -length ? -length : to;

        //正序排序
        if (fromIdx > toIdx) {
            fromIdx = fromIdx ^ toIdx;
            toIdx = fromIdx ^ toIdx;
            fromIdx = fromIdx ^ toIdx;
        }

        char[] chars = str.toCharArray();
        Set<Integer> idxSet = new HashSet<>();
        for (int i = fromIdx; i < toIdx + 1; i++) {
            if (i <= toIdx - length || i >= fromIdx + length) {
                continue;
            }
            idxSet.add((i + length) % length);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (idxSet.contains(i)) {
                sb.append(mask);
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        System.out.println(isNum("22.33"));
        System.out.println(initialUp("abcDef"));
        System.out.println(blur("abcdefg", Blur.builder().ratio(50).build()));
        System.out.println(blur("abcdefg", Blur.builder().ratio(0.8).mask("#").build()));
        //
        //        for (int i = -7; i <= 6; i++) {
        //            for (int j = 6; j >= -7; j--) {
        //                System.out.println("from=" + i + ",to=" + j + ",==> " + blur("*abcdefg",
        //                        Blur.builder().fromIdx(i).toIdx(j).mask("*").build()));
        //            }
        //        }
    }
}
