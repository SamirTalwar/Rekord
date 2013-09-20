package com.noodlesandwich.rekord;

import static com.noodlesandwich.rekord.RekordCollector.Accumulator;
import static com.noodlesandwich.rekord.RekordCollector.Finisher;
import static com.noodlesandwich.rekord.RekordCollector.Supplier;

public final class RekordCollectors {
    private RekordCollectors() { }

    public static <T extends RekordType, R> RekordCollector<T, R> of(final Supplier<Accumulator<T>> accumulatorSupplier, final Finisher<Accumulator<T>, R> finisher) {
        return new RekordCollectorFromFunctions<>(accumulatorSupplier, finisher);
    }

    private static class RekordCollectorFromFunctions<T extends RekordType, R> implements RekordCollector<T, R> {
        private final Supplier<Accumulator<T>> accumulatorSupplier;
        private final Finisher<Accumulator<T>, R> finisher;

        public RekordCollectorFromFunctions(Supplier<Accumulator<T>> accumulatorSupplier, Finisher<Accumulator<T>, R> finisher) {
            this.accumulatorSupplier = accumulatorSupplier;
            this.finisher = finisher;
        }

        @Override
        public Accumulator<T> accumulator() {
            return accumulatorSupplier.get();
        }

        @Override
        public R finish(Accumulator<T> accumulator) {
            return finisher.finish(accumulator);
        }
    }
}
