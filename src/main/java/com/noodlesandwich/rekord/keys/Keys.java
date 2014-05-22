package com.noodlesandwich.rekord.keys;

import java.util.Set;

public interface Keys<T> extends Iterable<Key<? super T, ?>> {
    boolean contains(Key<? super T, ?> key);

    Keys<T> originals();

    Set<Key<? super T, ?>> toSet();
}
