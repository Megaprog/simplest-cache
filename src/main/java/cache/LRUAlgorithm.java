package cache;

import java.time.Instant;

public class LRUAlgorithm implements EvictionAlgorithm<Instant> {

    @Override
    public Instant calcMeter(Instant previousMeter) {
        return Instant.now();
    }
}
