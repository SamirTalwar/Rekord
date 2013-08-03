package com.noodlesandwich.rekord;

public class Rekord<T extends RekordType> {
    private final Object value;

    public Rekord(Object value) {
        this.value = value;
    }

    public static <T extends RekordType> RekordBuilder<T> create() {
        return new RekordBuilder<>();
    }

    @SuppressWarnings("unchecked")
    public <V> V get(Key<T, V> key) {
        return (V) value;
    }

    public static final class RekordBuilder<T extends RekordType> {
        private Object value;

        public <V> RekordBuilder<T> with(Key<T, V> key, V value) {
            this.value = value;
            return this;
        }

        public Rekord<T> build() {
            return new Rekord<>(value);
        }
    }
}
