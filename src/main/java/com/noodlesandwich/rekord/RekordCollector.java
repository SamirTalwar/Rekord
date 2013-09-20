package com.noodlesandwich.rekord;

public interface RekordCollector<T extends RekordType, R> {
    <V> void accumulate(Key<? super T, V> key, V value);

    R result();
}
