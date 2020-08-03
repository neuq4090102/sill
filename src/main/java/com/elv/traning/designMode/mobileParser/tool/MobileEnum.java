package com.elv.traning.designMode.mobileParser.tool;

import com.elv.core.util.StrUtil;

/**
 * @author lxh
 * @since 2020-06-30
 */
public class MobileEnum {

    public enum ServiceProviderEnum {
        CHINA_MOBILE("中国移动", "移动"),  //
        CHINA_UNICOM("中国联通", "联通"), //
        CHINA_TELECOM("中国电信", "电信"), //
        ;

        private final String name;
        private final String alias;

        ServiceProviderEnum(String name, String alias) {
            this.name = name;
            this.alias = alias;
        }

        public String getName() {
            return name;
        }

        public String getAlias() {
            return alias;
        }

        public static String fetch(String param) {
            if (StrUtil.isBlank(param)) {
                return "";
            }
            for (ServiceProviderEnum item : ServiceProviderEnum.values()) {
                if (item.getName().contains(param) || param.contains(item.getAlias())) {
                    return item.getName();
                }
            }
            return "";
        }
    }
}
