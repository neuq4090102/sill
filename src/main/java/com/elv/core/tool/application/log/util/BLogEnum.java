package com.elv.core.tool.application.log.util;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lxh
 * @since 2020-11-22
 */
public class BLogEnum {

    public enum ActionEnum {
        ADD(1, "添加"), // 增加
        DELETE(2, "删除"), // 删除
        MODIFY(3, "修改"), // 修改
        OTHER(9, "其他"), // 其他
        ;

        private static Map<Integer, ActionEnum> map;

        static {
            map = Arrays.stream(ActionEnum.values()).collect(Collectors.toMap(key -> key.getAction(), val -> val));
        }

        private final int action;
        private final String desc;

        ActionEnum(int action, String desc) {
            this.action = action;
            this.desc = desc;
        }

        public int getAction() {
            return action;
        }

        public String getDesc() {
            return desc;
        }

        public static ActionEnum itemOf(int action) {
            return map.get(action);
        }
    }
}
