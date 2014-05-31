package com.noodlesandwich.rekord.functions;

public final class Functions {
    private Functions() { }

    public static <A, B> InvertibleFunction<A, B> invertibleFrom(
            final Function<A, B> applyForward,
            final Function<B, A> applyBackward
    ) {
        return new InvertibleFunction<A, B>() {
            @Override
            public B applyForward(A input) {
                return applyForward.apply(input);
            }

            @Override
            public A applyBackward(B input) {
                return applyBackward.apply(input);
            }
        };
    }

    public static <A, B, C> Function<A, C> compose(
            final Function<? super B, ? extends C> b,
            final Function<? super A, ? extends B> a
    ) {
        return new Function<A, C>() {
            @Override
            public C apply(A input) {
                return b.apply(a.apply(input));
            }
        };
    }
}
