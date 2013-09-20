package com.noodlesandwich.rekord;

import static com.noodlesandwich.rekord.Kollector.Accumulator;
import static com.noodlesandwich.rekord.Kollector.Finisher;
import static com.noodlesandwich.rekord.Kollector.Supplier;

public final class Kollectors {
    private Kollectors() { }

    public static <R> Kollector<R> of(final Supplier<Accumulator> accumulatorSupplier, final Finisher<R> finisher) {
        return new KollectorFromFunctions<>(accumulatorSupplier, finisher);
    }

    private static class KollectorFromFunctions<R> implements Kollector<R> {
        private final Supplier<Accumulator> accumulatorSupplier;
        private final Finisher<R> finisher;

        public KollectorFromFunctions(Supplier<Accumulator> accumulatorSupplier, Finisher<R> finisher) {
            this.accumulatorSupplier = accumulatorSupplier;
            this.finisher = finisher;
        }

        @Override
        public Accumulator accumulator() {
            return accumulatorSupplier.get();
        }

        @Override
        public R finish(Accumulator accumulator) {
            return finisher.finish(accumulator);
        }
    }
}
