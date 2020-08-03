package com.elv.traning.sensitiveWord.dfa;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lxh
 * @since 2020-07-28
 * <p>
 * 1.DFA算法
 * 2.利用NPL分词技术
 * 3.求两个字符串的最大匹配长度(仅限于较短的字符串)
 */
public class SensitiveWordDrill {

    public static Map<String, Object> init(Set<String> sensitiveWordSet) {
        if (sensitiveWordSet == null) {
            return new HashMap<>();
        }
        Map<String, Object> wordMap = new HashMap<>(sensitiveWordSet.size());
        for (String sensitiveWord : sensitiveWordSet) {
            Map<String, Object> currMap = wordMap;
            for (int i = 0; i < sensitiveWord.length(); i++) {
                String word = String.valueOf(sensitiveWord.charAt(i));
                Map<String, Object> subMap = (Map<String, Object>) currMap.get(word);
                if (subMap == null) {
                    subMap = new HashMap<>();
                    currMap.put(word, subMap);
                }
                currMap = subMap;
            }
        }

        return wordMap;
    }

    public static Map<String, Integer> match(String text, Map<String, Object> wordMap) {
        Map<String, Integer> resultMap = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            int wordLength = check(text, i, wordMap);
            if (wordLength <= 0) {
                continue;
            }
            String word = text.substring(i, i + wordLength);
            resultMap.put(word, Optional.ofNullable(resultMap.get(word)).orElse(0) + 1);
            i += wordLength - 1;
        }

        return resultMap;
    }

    private static int check(String text, int idx, Map<String, Object> wordMap) {
        int wordLength = 0;
        boolean end = false;
        for (int i = idx; i < text.length(); i++) {
            String word = String.valueOf(text.charAt(i));
            Object obj = wordMap.get(word);
            if (obj == null) {
                break;
            }
            wordLength++;
            wordMap = (Map<String, Object>) obj;
            if (wordMap == null) {
                break;
            } else if (wordMap.size() == 0) {
                end = true;
            }
        }

        if (!end) {
            wordLength = 0;
        }

        return wordLength;
    }

    /**
     * 1.a[i][j] = 0, i=0 or j=0
     * 2.a[i][j] = a[i-1][j-1]+1, s1[i-1] = s2[j-1]
     * 3.a[i][j] = max(a[i-1][j],a[i][j-1]),  s1[i-1] != s2[j-1]
     *
     * @param str1
     * @param str2
     */
    private static void maxMatch(String str1, String str2) {
        int lenth1 = str1.length();
        int lenth2 = str2.length();

        int[][] array = new int[lenth1][lenth2];
        System.out.print("  ");
        for (int i = 0; i < lenth2; i++) {
            System.out.print(str2.charAt(i) + " ");
        }
        System.out.println();

        for (int i = 0; i < lenth1; i++) {
            char c1 = str1.charAt(i);
            System.out.print(c1 + " ");
            for (int j = 0; j < lenth2; j++) {
                if (str2.charAt(j) == c1) {
                    array[i][j] = 1;
                }
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }

    }

    public static void main(String[] args) {
        Set<String> sensitiveWordSet = Stream.of( //
                "中国人", "中产阶级", "韩国人", "绞肉机", "共产党", "sexy" //
        ).collect(Collectors.toSet());

        Map<String, Object> init = init(sensitiveWordSet);
        System.out.println(init);
        System.out.println(match("Lily是中国人，very sexy, 是一名共产党员，she爱祖国，每50个人中就有一个鄙视韩国人，韩国人23333", init));

        String abc = "abc";
        System.out.println(abc.replaceAll("a", "3"));
        System.out.println(abc);
    }



}
