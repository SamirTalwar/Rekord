package com.noodlesandwich.rekord;

import java.util.Arrays;
import java.util.Set;
import com.noodlesandwich.rekord.extra.StringKollector;
import com.noodlesandwich.rekord.keys.RekordKey;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

public final class Rekord<T> {
    private final String name;
    private final Properties properties;

    private Rekord(String name, Properties properties) {
        this.name = name;
        this.properties = properties;
    }

    public static <T> UnkeyedRekord<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T> UnkeyedRekord<T> create(String name) {
        return new UnkeyedRekord<>(name);
    }

    @SuppressWarnings("unchecked")
    public <V> V get(Key<? super T, V> key) {
        return key.retrieveFrom(properties);
    }

    public boolean containsKey(Key<T, ?> key) {
        return properties.contains(key);
    }

    public Set<Key<? super T, ?>> keys() {
        return properties.keys();
    }

    public <V> Rekord<T> with(Key<? super T, V> key, V value) {
        if (key == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null key.");
        }

        return new Rekord<>(name, key.storeTo(properties, value));
    }

    public <V> Rekord<T> with(V value, Key<? super T, V> key) {
        return with(key, value);
    }

    public Rekord<T> without(Key<? super T, ?> key) {
        return new Rekord<>(name, properties.without(key));
    }

    @SuppressWarnings("unchecked")
    public <A extends Kollector.Accumulator<R>, R> R collect(Kollector<A, R> collector) {
        A accumulator = collector.accumulatorNamed(name);
        for (Key<? super T, ?> key : properties.<T>keys()) {
            Object value = key.retrieveFrom(properties);
            if (key instanceof RekordKey) {
                accumulator.accumulateRekord(key, ((Rekord<?>) value).collect(collector));
            } else {
                accumulator.accumulate((Key<?, Object>) key, value);
            }
        }
        return accumulator.finish();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (!(o instanceof Rekord)) {
            return false;
        }

        return properties.equals(((Rekord<T>) o).properties);
    }

    @Override
    public int hashCode() {
        return properties.hashCode();
    }

    @Override
    public String toString() {
        return collect(new StringKollector());
    }

    public static final class UnkeyedRekord<T> {
        private final String name;

        public UnkeyedRekord(String name) {
            this.name = name;
        }

        @SafeVarargs
        public final Rekord<T> accepting(Key<? super T, ?>... keys) {
            return accepting(OrderedPSet.from(Arrays.<Key<?, ?>>asList(keys)));
        }

        public final Rekord<T> accepting(PSet<Key<?, ?>> keys) {
            return new Rekord<>(name, new Properties(keys));
        }
    }
}
