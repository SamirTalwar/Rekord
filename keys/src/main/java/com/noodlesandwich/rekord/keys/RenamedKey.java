package com.noodlesandwich.rekord.keys;

public final class RenamedKey<T, V> extends ForwardingKey<T, V> {

    private RenamedKey(String name, Key<T, V> key) {
        super(name, key);
    }

    public static <T, V> UnnamedRenamedKey<T, V> renaming(Key<T, V> key) {
        return new UnnamedRenamedKey<>(key);
    }

    public static final class UnnamedRenamedKey<T, V> {
        private final Key<T, V> key;

        private UnnamedRenamedKey(Key<T, V> key) {
            this.key = key;
        }

        public Key<T, V> to(String name) {
            return new RenamedKey<>(name, key);
        }
    }
}
