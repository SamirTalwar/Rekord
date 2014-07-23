package com.noodlesandwich.rekord.implementation;

import java.util.Objects;
import com.noodlesandwich.rekord.FixedRekord;

public final class FixedRekordHelpers {
    private FixedRekordHelpers() { }

    public static <T> boolean equals(FixedRekord<T> a, Object bObject) {
        if (a == bObject) {
            return true;
        }

        if (!(bObject instanceof FixedRekord)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        FixedRekord<T> b = (FixedRekord<T>) bObject;
        return a.name().equals(b.name()) && a.properties().equals(b.properties());
    }

    public static <T> int hashCode(FixedRekord<T> rekord) {
        return Objects.hash(rekord.name(), rekord.properties());
    }
}
