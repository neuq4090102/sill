package com.elv.core.tool.text.censor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 敏感词工具
 * <p>
 * TODO : 可以考虑引入NLP分词技术，即以【词】为单位代替以【字】为单位，eg:西藏独立，分解为：{西藏={独立={}}}，取代：{西={藏={独={立={}}}}
 *
 * @author lxh
 * @see "DFA算法"
 * @see "分词工具"
 * @see "IKAnalyzer"
 * @since 2020-07-29
 */
public class SensitiveWordUtil {

    private static Map<String, Object> wordMap;

    static {
        wordMap = new HashMap<>(1);
    }

    private SensitiveWordUtil() {

    }

    /**
     * 初始化敏感词森林
     *
     * @param sensitiveWordSet 敏感词集
     */
    public static void init(Set<String> sensitiveWordSet) {
        if (sensitiveWordSet == null) {
            return;
        }
        wordMap = new HashMap<>(sensitiveWordSet.size());
        for (String sensitiveWord : sensitiveWordSet) {
            Map<String, Object> currentMap = wordMap;
            for (int i = 0; i < sensitiveWord.length(); i++) {
                String word = String.valueOf(sensitiveWord.charAt(i));
                Map<String, Object> subMap = (Map<String, Object>) currentMap.get(word);
                if (subMap == null) {
                    subMap = new HashMap<>();
                    currentMap.put(word, subMap);
                }
                currentMap = subMap;
            }
        }
    }

    /**
     * 是否含有敏感词
     *
     * @param text 文本内容
     * @return boolean
     */
    public static boolean contains(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (matchLength(text, i) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * text中敏感词出现频次汇总
     *
     * @param text 文本内容
     * @return java.util.Map
     */
    public static Map<String, Integer> summary(String text) {
        Map<String, Integer> resultMap = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            int wordLength = matchLength(text, i);
            if (wordLength <= 0) {
                continue;
            }
            String word = text.substring(i, i + wordLength);
            resultMap.put(word, Optional.ofNullable(resultMap.get(word)).orElse(0) + 1);
            i += wordLength - 1;
        }

        return resultMap;
    }

    /**
     * 获取text中出现的敏感词
     *
     * @param text 文本内容
     * @return java.util.Set
     */
    public static Set<String> fetch(String text) {
        return summary(text).keySet();
    }

    /**
     * 替换敏感词
     *
     * @param text 文本内容
     * @param mask 掩码
     * @return java.lang.String
     */

    public static String replace(String text, String mask) {
        for (String word : fetch(text)) {
            text = text.replaceAll(word, mask);
        }
        return text;
    }

    /**
     * 匹配敏感词长度
     *
     * @param text 文本内容
     * @param idx  索引
     * @return int
     */
    private static int matchLength(String text, int idx) {
        int wordLength = 0;
        boolean end = false;
        Map<String, Object> innerWordMap = wordMap;
        for (int i = idx; i < text.length(); i++) {
            String word = String.valueOf(text.charAt(i));
            Object obj = innerWordMap.get(word);
            if (obj == null) {
                break;
            }
            wordLength++;
            innerWordMap = (Map<String, Object>) obj;
            if (innerWordMap == null) {
                break;
            } else if (innerWordMap.size() == 0) {
                end = true;
            }
        }

        if (!end) {
            wordLength = 0;
        }

        return wordLength;
    }

    public static void main(String[] args) {
        Set<String> sensitiveWordSet = Stream.of( //
                "中国人", "中产阶级", "韩国人", "绞肉机", "共产党", "sexy", "西藏独立" //
        ).collect(Collectors.toSet());

        init(sensitiveWordSet);
        System.out.println(wordMap);
        System.out.println(contains("我是一个粉刷匠"));
        System.out.println(contains("我是中国人"));
        String text = "Lily是中国人，very sexy, 是一名共产党员，she爱祖国，接下来就是测试韩国人，韩国人23333";
        System.out.println(fetch(text));
        System.out.println(summary(text));
        System.out.println(replace(text, "***"));
    }

}
