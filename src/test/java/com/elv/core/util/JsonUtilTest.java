package com.elv.core.util;

import com.elv.traning.model.beanCopy.OrderRoomEntity;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author lxh
 * @date 2020-06-01
 */
public class JsonUtilTest {

    @Test
    public void testToJson() {
        assertNotNull(JsonUtil.toJson("abc"));
    }

    @Test
    public void testToObject() {
    }

    @Test
    public void testTestToObject() {
    }

    @Test
    public void testToList() {
    }

    @Test
    public void testIsJson() {
    }
}