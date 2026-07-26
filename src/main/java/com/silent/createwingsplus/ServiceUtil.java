package com.silent.createwingsplus;

import java.util.ServiceLoader;

public class ServiceUtil {

    public static <T> T load(final Class<T> tClass) {
        return ServiceLoader.load(tClass, tClass.getClassLoader()).findFirst().orElseThrow(() -> new RuntimeException("Unable to find %s implementation".formatted(tClass.getName())));
    }
}
