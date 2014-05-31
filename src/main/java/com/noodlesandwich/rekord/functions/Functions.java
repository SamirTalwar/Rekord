package com.noodlesandwich.rekord.functions;

public final class Functions {
    private Functions() { }

    public static <A, B> InvertibleFunction<A, B> invertibleFrom(
            final Function<A, B> applyForward, final Function<B, A> applyBackward
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
}
