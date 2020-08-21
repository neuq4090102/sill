package com.elv.web.constant;

import com.elv.core.util.StrUtil;

/**
 * @author lxh
 * @since 2020-08-20
 */
public class RequestEnum {

    /**
     * @see <a href="https://github.com/hellysmile/fake-useragent"></a>
     */
    public enum AgentEnum {
        Windows("Windows"), // PC
        Linux("Linux"), // PC
        Mac("Macintosh"), // PC
        Android("Android"), //
        iPad("iPad"), //
        iPhone("iPhone"), //
        WeChat("MicroMessenger"), //
        Other(""), //
        ;

        private final String keyWord;

        AgentEnum(String keyWord) {
            this.keyWord = keyWord;
        }

        public String getKeyWord() {
            return keyWord;
        }

        public static String fetch(String userAgent) {
            for (AgentEnum item : AgentEnum.values()) {
                String keyWord = item.getKeyWord();
                if (StrUtil.isBlank(keyWord)) {
                    continue;
                }

                if (userAgent.toLowerCase().contains(keyWord.toLowerCase())) {
                    return item.name();
                }
            }

            return Other.name();
        }

    }

    public static void main(String[] args) {
        System.out.println(AgentEnum.fetch("Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)"));
    }
}
