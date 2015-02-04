package cache;

import java.util.concurrent.atomic.AtomicLong;

public class FIFOAlgorithm implements EvictionAlgorithm<Long> {
    private static final AtomicLong counter = new AtomicLong();

    @Override
    public Long calcMeter(Long previousMeter) {
        if (previousMeter != null) {
            return previousMeter;
        }

        return counter.getAndIncrement();
    }
}
