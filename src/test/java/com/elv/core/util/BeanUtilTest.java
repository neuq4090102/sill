package com.elv.core.util;

import java.lang.reflect.Field;
import java.util.List;

import org.testng.annotations.Test;

import com.elv.traning.model.beanCopy.OrderEntity;

/**
 * @author lxh
 * @date 2020-06-01
 */
public class BeanUtilTest {

    @Test
    public void testGetAllFields() {
        List<Field> allFields = BeanUtil.getAllFields(OrderEntity.class);
        allFields.stream().forEach(field -> System.out.println(field.getName()));
    }
}