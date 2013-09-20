package com.noodlesandwich.rekord;

import static com.noodlesandwich.rekord.Kollector.Accumulator;
import static com.noodlesandwich.rekord.Kollector.Finisher;
import static com.noodlesandwich.rekord.Kollector.Supplier;

public final class Kollectors {
    private Kollectors() { }

    public static <T extends RekordType, R> Kollector<T, R> of(final Supplier<Accumulator<T>> accumulatorSupplier, final Finisher<Accumulator<T>, R> finisher) {
        return new KollectorFromFunctions<>(accumulatorSupplier, finisher);
    }

    private static class KollectorFromFunctions<T extends RekordType, R> implements Kollector<T, R> {
        private final Supplier<Accumulator<T>> accumulatorSupplier;
        private final Finisher<Accumulator<T>, R> finisher;

        public KollectorFromFunctions(Supplier<Accumulator<T>> accumulatorSupplier, Finisher<Accumulator<T>, R> finisher) {
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
