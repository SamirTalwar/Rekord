package com.noodlesandwich.rekord.functions;

public interface Function<A, B> {
    B apply(A input);
}
