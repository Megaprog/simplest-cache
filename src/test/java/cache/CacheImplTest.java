package cache;

import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CacheImplTest {

    @Test
    public void testFIFO() throws Exception {
        Cache<Integer, String> cache = new CacheImpl<>(new FIFOAlgorithm(), 2);

        cache.put(2, "Two");
        cache.put(1, "One");

        assertEquals(new HashSet<>(Arrays.asList(1, 2)), cache.keys());
        assertEquals("One", cache.apply(1));
        assertEquals("Two", cache.apply(2));

        cache.put(3, "Three");

        assertEquals(new HashSet<>(Arrays.asList(1, 3)), cache.keys());
        assertEquals("One", cache.apply(1));
        assertEquals("Three", cache.apply(3));
        assertNull(cache.apply(2));
    }

    @Test
    public void testLRU() throws Exception {
        CacheImpl<Integer, String, Instant> cache = new CacheImpl<>(new LRUAlgorithm(), 2);

        cache.put(2, "Two");
        cache.put(1, "One");

        assertEquals(new HashSet<>(Arrays.asList(1, 2)), cache.keys());
        assertEquals("One", cache.apply(1));
        Thread.sleep(1); //needed to make sure second key has time measure newer than first
        assertEquals("Two", cache.apply(2));

        cache.put(3, "Three");

        assertEquals(new HashSet<>(Arrays.asList(2, 3)), cache.keys());
        assertEquals("Two", cache.apply(2));
        assertEquals("Three", cache.apply(3));
        assertNull(cache.apply(1));
    }
}