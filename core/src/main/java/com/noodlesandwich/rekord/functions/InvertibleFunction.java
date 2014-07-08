package com.noodlesandwich.rekord.functions;

public interface InvertibleFunction<A, B> {
    B applyForward(A input);

    A applyBackward(B input);
}
