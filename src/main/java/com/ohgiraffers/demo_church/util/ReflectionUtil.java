package com.ohgiraffers.demo_church.util;

import java.lang.reflect.Field;


public class ReflectionUtil {
    public static int getFieldCount(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return fields.length;
    }
}
