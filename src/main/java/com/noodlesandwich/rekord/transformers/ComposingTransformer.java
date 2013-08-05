package com.noodlesandwich.rekord.transformers;

public class ComposingTransformer<A, B, C> implements Transformer<A, C> {
    private final Transformer<? super B, ? extends C> second;
    private final Transformer<? super A, ? extends B> first;

    public ComposingTransformer(Transformer<? super B, ? extends C> second, Transformer<? super A, ? extends B> first) {
        this.second = second;
        this.first = first;
    }

    @Override
    public C transform(A value) {
        return second.transform(first.transform(value));
    }
}
