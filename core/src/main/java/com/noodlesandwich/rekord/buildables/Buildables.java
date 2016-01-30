package com.noodlesandwich.rekord.buildables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Buildables {
    private Buildables() { }

    public static <T> Buildable<List<T>> arrayList() {
        return new Buildable<List<T>>() {
            @Override public List<T> builder() {
                return new ArrayList<>();
            }
        };
    }

    public static <T> Buildable<Set<T>> hashSet() {
        return new Buildable<Set<T>>() {
            @Override public Set<T> builder() {
                return new HashSet<>();
            }
        };
    }
}
