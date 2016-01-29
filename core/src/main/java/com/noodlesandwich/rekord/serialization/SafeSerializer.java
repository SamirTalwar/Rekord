package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.FixedRekord;

public interface SafeSerializer<S> extends Serializer<S, ImpossibleException> {
    @Override
    <T> S serialize(String name, FixedRekord<T> rekord);

    interface SafeAccumulator<A> extends Accumulator<A, ImpossibleException> { }
}
