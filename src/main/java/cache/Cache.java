/**
 * Copyright (C) 2012 Tomas Shestakov.
 */

package cache;

import java.util.Set;
import java.util.function.Function;

/**
 * Interface of some very abstract Cache which allow to get and put some object by unique key.
 * @param <K> type of the key
 * @param <V> type of the value
 */
public interface Cache<K, V> extends Function<K, V> {

    /**
     * Store object in cache or replays old value by new if object with specified key already exists.
     * @param key object unique key
     * @param value object to store
     */
    void put(K key, V value);

    /**
     * Remove object with specified key.
     * @param key object unique key
     */
    void remove(K key);

    /**
     * @return all keys in cache
     */
    Set<K> keys();

    /**
     * Remove all objects in cache.
     */
    void clear();
}
