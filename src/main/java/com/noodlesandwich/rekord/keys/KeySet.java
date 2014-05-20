package com.noodlesandwich.rekord.keys;

import java.util.Set;

public interface KeySet<T> extends Iterable<Key<? super T, ?>> {
    boolean contains(Key<? super T, ?> key);

    KeySet<T> originals();

    Set<Key<? super T, ?>> toSet();
}
