package com.noodlesandwich.rekord.properties;

import java.util.HashSet;
import java.util.Set;
import com.noodlesandwich.rekord.implementation.KeySet;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;

public final class PropertyExtraction {
    private PropertyExtraction() { }

    public static <T> Keys<T> keysFrom(PropertyMap<T> properties) {
        Set<Keys<? super T>> keys = new HashSet<>();
        for (Property<? super T, ?> property : properties) {
            Key<? super T, ?> key = property.key();
            if (key.test(properties)) {
                keys.add(key);
            }
        }
        return KeySet.from(keys);
    }

    public static <T> void checkAcceptabilityOf(PropertyMap<T> properties, Keys<T> acceptedKeys) {
        for (Key<? super T, ?> key : keysFrom(properties)) {
            if (!acceptedKeys.contains(key)) {
                throw new UnacceptableKeyException(key);
            }
        }
    }
}
