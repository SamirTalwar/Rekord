package com.noodlesandwich.rekord.transformers;

public class ComposingTransformer<A, B, C> implements Transformer<A, C> {
    private final Transformer<B, C> second;
    private final Transformer<A, B> first;

    public ComposingTransformer(Transformer<B, C> second, Transformer<A, B> first) {
        this.second = second;
        this.first = first;
    }

    @Override
    public A transformInput(C value) {
        return first.transformInput(second.transformInput(value));
    }

    @Override
    public C transformOutput(A value) {
        return second.transformOutput(first.transformOutput(value));
    }
}
