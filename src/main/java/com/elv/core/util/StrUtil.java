package com.elv.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.elv.core.model.util.BlurCtrl;

/**
 * @author lxh
 * @since 2020-06-09
 */
public final class StrUtil {

    private static final Pattern digitPattern;
    private static final Pattern mailPattern;

    static {
        digitPattern = Pattern.compile("^-{0,1}\\d+\\.{0,1}\\d*$");
        mailPattern = Pattern
                .compile("^[a-zA-Z0-9_\\-\\.]+@[a-zA-Z0-9\\-]+(\\.[a-zA-Z0-9\\-]+){0,2}\\.((com)|(cn)|(net))$");
    }

    private StrUtil() {
    }

    public static boolean isEmpty(final CharSequence cs) {
        return StringUtils.isEmpty(cs);
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isAllEmpty(final CharSequence... css) {
        return StringUtils.isAllEmpty(css);
    }

    public static boolean isAnyEmpty(final CharSequence... css) {
        return StringUtils.isAnyEmpty(css);
    }

    public static boolean isBlank(final CharSequence cs) {
        return StringUtils.isBlank(cs);
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isAllBlank(final CharSequence... css) {
        return StringUtils.isAllBlank(css);
    }

    public static boolean isAnyBlank(final CharSequence... css) {
        return StringUtils.isAnyBlank(css);
    }

    /**
     * 是否是数字
     *
     * @param str 参数
     * @return boolean
     * @see Character#isDigit(char)
     */
    public static boolean isDigit(String str) {
        if (isBlank(str)) {
            return false;
        }
        return digitPattern.matcher(str).matches();
    }

    /**
     * 是否都是数字
     *
     * @param params 参数集
     * @return boolean
     */
    public static boolean allIsDigit(String... params) {
        for (String param : params) {
            if (!isDigit(param)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是邮箱
     *
     * @param email 邮箱参数
     * @return boolean
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        return mailPattern.matcher(email).matches();
    }

    /**
     * 是否是手机
     *
     * @param mobile 手机号
     * @return boolean
     */
    public static boolean isMobile(String mobile) {
        // TODO
        return PhoneUtil.isPhone(mobile);
    }

    /**
     * 首字母大写
     *
     * @param str 字符串
     * @return java.lang.String
     */
    public static String initialUp(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String UUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultVal) {
        return StringUtils.defaultIfBlank(str, defaultVal);
    }

    public static <T extends CharSequence> T defaultIfEmpty(final T str, final T defaultVal) {
        return StringUtils.defaultIfEmpty(str, defaultVal);
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
     *      Blur.builder().fromIdx(2).ratio(1.0d).build()   = ab*****
     *      Blur.builder().fromIdx(-2).ratio(1.0d).build()  = ******g
     *
     *  2.按照固定步长脱敏
     *      Blur.builder().fromIdx(2).stepSize(4).build()   = ab*****
     *      Blur.builder().fromIdx(2).stepSize(0).build()   = ab*defg
     *      Blur.builder().fromIdx(-2).stepSize(4).build()  = a*****g
     *      Blur.builder().fromIdx(-2).stepSize(0).build()  = abcde*g
     *
     *  3.按照索引脱敏
     *      3.1.中间数据脱敏
     *      Blur.builder().fromIdx(2).stepSize(4).build()   = ab***fg
     *      Blur.builder().fromIdx(4).stepSize(2).build()   = ab***fg
     *      Blur.builder().fromIdx(-3).stepSize(-4).build() = abc**fg
     *      Blur.builder().fromIdx(-4).stepSize(-3).build() = abc**fg
     *
     *      3.2 两头数据脱敏
     *      Blur.builder().fromIdx(-2).stepSize(1).build()  = **cde**
     *      Blur.builder().fromIdx(0).stepSize(-2).build()  = *bcde**
     *
     *  4.修改掩码符号
     *      Blur.builder().ratio(0.8).mask("#").build()     = ######g
     * </pre>
     *
     * @param object   对象
     * @param blurCtrl 脱敏参数
     * @return java.lang.String
     */
    public static String blur(Object object, BlurCtrl blurCtrl) {
        if (object == null) {
            return "";
        }
        String str = String.valueOf(object);
        if (StringUtils.isBlank(str)) {
            return str;
        }

        int length = str.length();
        int fromIdx = blurCtrl.getFromIdx();
        int toIdx = blurCtrl.getToIdx();
        int stepSize = blurCtrl.getStepSize();
        double ratio = blurCtrl.getRatio();

        //优先使用比例参数
        if (ratio > 0 && ratio <= 1.0d) {
            stepSize = (int) Math.floor(length * ratio); //向下取整，比例换步长
            blurCtrl.setResetStepSize(true);
        }

        //其次使用步长参数
        if (blurCtrl.isResetStepSize() || stepSize > 0) {
            stepSize = Math.max(0, stepSize);
            toIdx = fromIdx < 0 ?
                    Math.max(fromIdx - stepSize, 0 - length) :
                    Math.min(fromIdx + stepSize, length - 1); //步长换toIdx
        }

        return desensitize(str, fromIdx, toIdx, blurCtrl.getMask());
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

    public static String toUnicode(String str) {
        return UnicodeUtil.toUnicode(str);
    }

    public static String unicodeToStr(String unicode) {
        return UnicodeUtil.toStr(unicode);
    }

    private static class UnicodeUtil {
        static String toUnicode(String str) {
            if (str == null) {
                return "\\u000";
            }

            StringBuilder sb = new StringBuilder();
            for (char item : str.toCharArray()) {
                sb.append("\\u").append(Integer.toHexString(item));
            }

            return sb.toString();
        }

        static String toStr(String unicode) {
            if (unicode == null) {
                return "";
            } else if (!unicode.startsWith("\\u")) {
                return unicode;
            }

            StringBuilder sb = new StringBuilder();
            String[] hexArr = unicode.split("\\\\u");
            Arrays.stream(hexArr).filter(hex -> isNotBlank(hex)).forEach(hex -> {
                sb.append((char) Integer.parseInt(hex, 16));
            });

            return sb.toString();
        }
    }

    /**
     * 转List(默认去重)
     *
     * @param str       入参
     * @param delimiter 分隔符
     * @param clazz     要转化的类对象
     * @param <T>       范型
     * @return java.util.List
     */
    public static <T> List<T> splitToList(String str, String delimiter, Class<T> clazz) {
        return splitToList(str, delimiter, clazz, true);
    }

    /**
     * 转List
     *
     * @param str       入参
     * @param delimiter 分隔符
     * @param clazz     要转化的类对象
     * @param distinct  是否去重
     * @param <T>       范型
     * @return java.util.List
     */
    public static <T> List<T> splitToList(String str, String delimiter, Class<T> clazz, boolean distinct) {
        if (distinct) {
            return new ArrayList<>(splitToSet(str, delimiter, clazz));
        } else {
            List<T> results = new ArrayList<>();
            add(str, delimiter, clazz, results);
            return results;
        }
    }

    /**
     * 转Set
     *
     * @param str       入参
     * @param delimiter 分隔符
     * @param clazz     要转化的类对象
     * @param <T>       范型
     * @return java.util.List
     */
    public static <T> Set<T> splitToSet(String str, String delimiter, Class<T> clazz) {
        Set<T> results = new HashSet<>();
        add(str, delimiter, clazz, results);
        return results;
    }

    private static <T> void add(String str, String delimiter, Class<T> clazz, Collection<T> results) {
        if (isBlank(str)) {
            return;
        }
        try {
            for (String s : str.split(delimiter)) {
                if (isNotBlank(s)) {
                    results.add(clazz.getConstructor(String.class).newInstance(s.trim()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("StrUtil#add error.", e);
        }
    }

    public static String leftPad(final String str, final int size, final String padChar) {
        return StringUtils.leftPad(str, size, padChar);
    }

    public static String rightPad(final String str, final int size, final String padChar) {
        return StringUtils.rightPad(str, size, padChar);
    }

}
