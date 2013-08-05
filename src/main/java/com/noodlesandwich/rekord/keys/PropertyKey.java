package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;

public class PropertyKey<T, V> extends Key<T, V> {
    @Override
    public V retrieveFrom(Properties<T> properties, Key<T, V> surrogateKey) {
        return properties.get(surrogateKey);
    }

    @Override
    public String toString() {
        return "";
    }
}
