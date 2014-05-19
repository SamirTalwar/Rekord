package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.serialization.Serialization;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class IterableKey<T, V, C extends Iterable<V>> extends OriginalKey<T, C> {
    private final Key<T, V> contents;

    private IterableKey(String name, Key<T, V> contents) {
        super(name);
        this.contents = contents;
    }

    public static IterableKeyBuilder named(String name) {
        return new IterableKeyBuilder(name);
    }

    public static final class IterableKeyBuilder {
        private final String name;

        public IterableKeyBuilder(String name) {
            this.name = name;
        }

        public <T, V, C extends Iterable<V>> IterableKey<T, V, C> of(Key<T, V> contents) {
            return new IterableKey<>(name, contents);
        }
    }

    @Override
    public <A, E extends Exception> void accumulate(final C iterable, Serializer.Accumulator<A, E> accumulator) throws E {
        accumulator.addIterable(name(), new Serializer.Accumulation() {
            @Override
            public <A2, E2 extends Exception> void accumulateIn(Serializer.Accumulator<A2, E2> iterableAccumulator) throws E2 {
                Serialization.serialize(iterable).with(contents).into(iterableAccumulator);
            }
        });
    }
}
