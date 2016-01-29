package com.noodlesandwich.rekord.serialization;

public interface SafeSerializer<S> extends Serializer<S, SafeSerializer.ImpossibleException> {
    interface SafeAccumulator<A> extends Accumulator<A, ImpossibleException> { }

    final class ImpossibleException extends RuntimeException {
        private static final long serialVersionUID = -7701626244428181507L;

        private ImpossibleException() { }
    }
}
