package com.noodlesandwich.rekord.serialization;

public interface SafeSerializer<R> extends Serializer<R, SafeSerializer.ImpossibleException> {
    public static interface SafeAccumulator<A> extends Accumulator<A, ImpossibleException> { }

    public static final class ImpossibleException extends RuntimeException {
        private static final long serialVersionUID = -7701626244428181507L;

        private ImpossibleException() { }
    }
}
