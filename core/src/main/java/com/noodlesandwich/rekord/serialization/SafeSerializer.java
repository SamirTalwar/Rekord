package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.FixedRekord;

public interface SafeSerializer<S> extends Serializer<S, SafeSerializer.ImpossibleException> {
    @Override
    <T> S serialize(String name, FixedRekord<T> rekord);

    interface SafeAccumulator<A> extends Accumulator<A, ImpossibleException> { }

    final class ImpossibleException extends RuntimeException {
        private static final long serialVersionUID = -7701626244428181507L;

        private ImpossibleException() { }
    }
}
