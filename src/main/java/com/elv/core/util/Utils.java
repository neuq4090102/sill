package com.elv.core.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 综合工具类
 *
 * @author lxh
 * @since 2020-03-23
 */
public class Utils {

    private Utils() {
    }

    public static List<Date> daters(Dater startDater, Dater endDater) {
        List<Date> dates = new ArrayList<>();

        Dater initDater = startDater.start();
        while (!initDater.isAfter(endDater.start())) {
            dates.add(initDater.getDate());
            initDater = initDater.offsetDays(1);
        }
        return dates;
    }

    public static void main(String[] args) {
        daters(Dater.now().offsetDays(-31), Dater.now()).stream()
                .forEach(date -> System.out.println(Dater.of(date).getDateStr()));

    }
}
