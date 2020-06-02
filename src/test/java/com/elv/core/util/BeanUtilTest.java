package com.elv.core.util;

import com.elv.traning.model.beanCopy.OrderEntity;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.List;

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