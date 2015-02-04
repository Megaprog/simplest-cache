/**
 * Copyright (C) 2012 Tomas Shestakov.
 */

package cache;

/**
 * Algorithm of elements displacement for {@link Cache}.
 * Gets old meter and return a new one
 */
public interface EvictionAlgorithm<M extends Comparable<M>> {

    /**
     * Gets old meter and return a new one.
     * @param previousMeter previous meter. Null value means first time usage.
     * @return new meter after applying algorithm to old one.
     */
    M calcMeter(M previousMeter);
}
