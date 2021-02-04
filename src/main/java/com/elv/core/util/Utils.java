package com.elv.core.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.elv.core.constant.Const;

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

    public static long ipToLong(String ip) {
        String innerIp = Optional.ofNullable(ip).orElse("127.0.0.1");
        long result = 0L;
        for (String ipSegment : innerIp.split("\\.")) {
            Assert.isTrue(!StrUtil.isDigit(ipSegment) || Integer.parseInt(ipSegment) > Const.IP_SEGMENT_MAX,
                    "Utils#ipToLong, IP Param invalid," + ip);
            result = result << 8 | Integer.parseInt(ipSegment);
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(1 << 8 | 1);
        System.out.println(Integer.toBinaryString(1));
        System.out.println(Integer.parseInt("0101", 2));
        System.out.println(ipToLong("208.34.33.33"));
        // daters(Dater.now().offsetDays(-31), Dater.now()).stream()
        //         .forEach(date -> System.out.println(Dater.of(date).getDateStr()));

    }
}
