package com.elv.core.util;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author lxh
 * @date 2020-06-01
 */
public class JsonUtilTest {

    @Test
    public void testToJson() {
        Dater dater = Mockito.mock(Dater.class); // mock
        Mockito.when(dater.getDateStr()).thenReturn("2020-06-02"); // stub
        System.out.println(dater.getDateStr()); // verify
        Assert.assertEquals("2020-06-02", dater.getDateStr()); // test
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