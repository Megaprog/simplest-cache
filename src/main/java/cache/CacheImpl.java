/**
 * Copyright (C) 2012 Tomas Shestakov.
 */

package cache;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Single threaded implementation of {@link Cache}.
 * Requires "Strategies": {@link cache.EvictionAlgorithm}
 * @param <K> type of the key
 * @param <V> type of the value
 */
public class CacheImpl<K, V, M extends Comparable<M>> implements Cache<K, V> {

    protected final Map<K, ValueAndMeasure<K, V, M>> objects = createObjectsMap();

    protected final EvictionAlgorithm<M> algorithm;
    protected final int maxElements;

    /**
     * Creates instance of Cache.
     * @param algorithm implements {@link EvictionAlgorithm} elements displacement strategy.
     * @param maxElements max number elements in cache.
     * If number of elements exceed this parameter  be removed by EvictionAlgorithm.
     */
    public CacheImpl(EvictionAlgorithm<M> algorithm, int maxElements) {
        this.algorithm = algorithm;
        this.maxElements = maxElements;
    }

    @Override
    public V apply(K key) {
        final ValueAndMeasure<K, V, M> vm = objects.get(key);
        if (vm == null) {
            return null;
        }

        applyAlgorithm(key);
        return vm.getValue();
    }

    @Override
    public void put(K key, V value) {
        //check if needed remove some key
        if (objects.size() >= maxElements && !objects.containsKey(key)) {
            //remove first key
            objects.remove(objects.values().parallelStream().map(ValueAndMeasure::getMeasure).sorted(measureComparator).findFirst().orElseThrow(IllegalStateException::new).getKey());
        }

        objects.put(key, new ValueAndMeasure<K, V, M>(value, new Measure<>(key)));
        applyAlgorithm(key);
    }

    @Override
    public void remove(K key) {
        objects.remove(key);
    }

    @Override
    public Set<K> keys() {
        return objects.keySet();
    }

    @Override
    public void clear() {
        objects.clear();
    }

    protected void applyAlgorithm(K key) {
        objects.compute(key, (k, vm) -> {
            vm.getMeasure().setMeter(algorithm.calcMeter(vm.getMeasure().getMeter()));
            return vm;
        });
    }

    protected Map<K, ValueAndMeasure<K, V, M>> createObjectsMap() {
        return new HashMap<>();
    }

    protected final Comparator<Measure<K, M>> measureComparator = (o1, o2) -> o1.getMeter().compareTo(o2.getMeter());

    protected static class Measure<K, M extends Comparable<M>> {
        private final K key;
        private M meter;

        public Measure(K key) {
            this.key = key;
        }

        public K getKey() {
            return key;
        }

        public M getMeter() {
            return meter;
        }

        public void setMeter(M meter) {
            this.meter = meter;
        }

        @Override
        public String toString() {
            return "Measure{" +
                    "key=" + key +
                    ", meter=" + meter +
                    '}';
        }
    }

    protected static class ValueAndMeasure<K, V, M extends Comparable<M>> {
        private V value;
        private Measure<K, M> measure;

        public ValueAndMeasure(V value, Measure<K, M> measure) {
            this.value = value;
            this.measure = measure;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Measure<K, M> getMeasure() {
            return measure;
        }

        public void setMeasure(Measure<K, M> measure) {
            this.measure = measure;
        }

        @Override
        public String toString() {
            return "ValueAndMeasure{" +
                    "value=" + value +
                    ", measure=" + measure +
                    '}';
        }
    }
}
