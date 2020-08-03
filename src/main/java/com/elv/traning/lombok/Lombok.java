package com.elv.traning.lombok;

import com.elv.traning.model.lombok.LomBokEntity;

/**
 * @author lxh
 * @since 2020-04-16
 */
public class Lombok {

    public static void main(String[] args) {

        LomBokEntity lomBokEntity = new LomBokEntity();
        //即使lomBokEntity未对name赋值，也不报错
        System.out.println(lomBokEntity);

        testBuilder();

    }

    private static void testBuilder() {
        //此处如果不对name赋值就报错
        // LomBokEntity lomBokEntity = LomBokEntity.builder().adultEnum(BooleanEnum.YES).build();
        // LomBokEntity lomBokEntity = Builder.of(LomBokEntity::new).with(LomBokEntity::setAdultEnum,BooleanEnum.YES).build();

        // System.out.println(lomBokEntity);
    }
}
