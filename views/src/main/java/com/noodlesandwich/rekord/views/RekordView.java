package com.noodlesandwich.rekord.views;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.RekordTemplate;
import com.noodlesandwich.rekord.implementation.KeySet;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.serialization.Serializer;
import com.noodlesandwich.rekord.serialization.StringSerializer;

public final class RekordView<T, U> {
    private final String name;
    private final RekordTemplate<U> viewedRekordTemplate;
    private final Keys<T> acceptedKeys;

    public RekordView(Class<T> type, RekordTemplate<U> viewedRekordTemplate, Keys<T> acceptedKeys) {
        this(type.getSimpleName(), viewedRekordTemplate, acceptedKeys);
    }

    public RekordView(String name, RekordTemplate<U> viewedRekordTemplate, Keys<T> acceptedKeys) {
        this.name = name;
        this.viewedRekordTemplate = viewedRekordTemplate;
        this.acceptedKeys = acceptedKeys;
    }

    public static <T> UnviewableRekordView<T> of(Class<T> type) {
        return new UnviewableRekordView<>(type);
    }

    public FixedRekord<T> over(Rekord<U> rekord) {
        return new TransformedRekord<>(name, viewedRekordTemplate, acceptedKeys, rekord);
    }

    private static class TransformedRekord<T, U> implements FixedRekord<T> {
        private final String name;
        private final Keys<T> acceptedKeys;
        private final Rekord<U> rekord;

        public TransformedRekord(
                String name, RekordTemplate<U> viewedRekordTemplate, Keys<T> acceptedKeys, Rekord<U> rekord) {
            this.name = name;
            this.acceptedKeys = acceptedKeys;
            this.rekord = rekord;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public boolean has(Key<T, ?> key) {
            return rekord.has(key.<U>forAnotherRekord());
        }

        @Override
        public <V> V get(Key<T, V> key) {
            return rekord.get(key.<U>forAnotherRekord());
        }

        @Override
        public Keys<T> keys() {
            Set<Keys<T>> keys = new HashSet<>();
            for (Key<T, ?> key : acceptedKeys) {
                if (rekord.has(key.<U>forAnotherRekord())) {
                    keys.add(key);
                }
            }
            return KeySet.from(keys);
        }

        @Override
        public Keys<T> acceptedKeys() {
            return acceptedKeys;
        }

        @Override
        public Properties<T> properties() {
            return new Properties<T>() {
                @Override
                public boolean has(Key<T, ?> key) {
                    return key.<U>forAnotherRekord().test(rekord.properties());
                }

                @Override
                public <V> V get(Key<T, V> key) {
                    return key.<U>forAnotherRekord().get(rekord.properties());
                }

                @Override
                public Properties<T> set(Property<T, ?> property) {
                    return rekord.properties().set(property.<U>forAnotherRekord());
                }

                @Override
                public Properties<T> remove(Key<T, ?> key) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Iterator<Property<T, ?>> iterator() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public <R, E extends Exception> R serialize(Serializer<R, E> serializer) throws E {
            return serializer.serialize(name, this);
        }

        @Override
        public String toString() {
            return serialize(new StringSerializer());
        }
    }

    public static final class UnviewableRekordView<T> {
        private final Class<T> type;

        public UnviewableRekordView(Class<T> type) {
            this.type = type;
        }

        public <U> UnkeyedRekordView<T, U> viewing(RekordTemplate<U> rekord) {
            return new UnkeyedRekordView<>(type, rekord);
        }
    }

    public static final class UnkeyedRekordView<T, U> {
        private final Class<T> type;
        private final RekordTemplate<U> viewedRekordTemplate;

        public UnkeyedRekordView(Class<T> type, RekordTemplate<U> viewedRekordTemplate) {
            this.type = type;
            this.viewedRekordTemplate = viewedRekordTemplate;
        }

        // CHECKSTYLE:OFF
        @SuppressWarnings("varargs")
        @SafeVarargs
        public final RekordView<T, U> accepting(Keys<T>... keys) {
            return accepting(KeySet.from(keys));
        }
        // CHECKSTYLE:ON

        public RekordView<T, U> accepting(Keys<T> keys) {
            return new RekordView<>(type, viewedRekordTemplate, keys);
        }
    }
}
