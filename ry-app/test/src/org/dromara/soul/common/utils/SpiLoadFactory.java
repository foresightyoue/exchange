package org.dromara.soul.common.utils;

import java.util.Iterator;
import java.util.ServiceLoader;

public class SpiLoadFactory {
    /**
     * load loadFirst.
     * @param clazz className
     * @param <S> java.util.ServiceLoader<S/>
     * @return java.util.ServiceLoader<S/>
     */
    public static <S> S loadFirst(final Class<S> clazz) {
        final ServiceLoader<S> loader = loadAll(clazz);
        final Iterator<S> iterator = loader.iterator();
        if (!iterator.hasNext()) {
            throw new IllegalStateException(String.format(
                    "No implementation defined in /META-INF/services/%s, please check whether the file exists and has the right implementation class!",
                    clazz.getName()));
        }
        return iterator.next();
    }

    /**
     * load all.
     *
     * @param clazz className
     * @return java.util.ServiceLoader<S/>
     */
    public static <S> ServiceLoader<S> loadAll(final Class<S> clazz) {
        return ServiceLoader.load(clazz);
    }
}
