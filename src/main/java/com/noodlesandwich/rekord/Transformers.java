package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.transformers.ComposingTransformer;
import com.noodlesandwich.rekord.transformers.DefaultingTransformer;
import com.noodlesandwich.rekord.transformers.IdentityTransformer;
import com.noodlesandwich.rekord.transformers.Transformer;

public final class Transformers {
    public static <T> Transformer<T, T> identity() {
        return new IdentityTransformer<>();
    }

    public static <T> Transformer<T, T> defaultsTo(T value) {
        return new DefaultingTransformer<>(value);
    }

    public static <A, B, C> Transformer<A, C> compose(Transformer<B, C> second, Transformer<A, B> first) {
        return new ComposingTransformer<>(second, first);
    }
}
