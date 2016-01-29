package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.Rekord;

public interface SafeDeserializer<S> extends Deserializer<S, ImpossibleException> {
    @Override
    <T> Rekord<T> deserialize(S serialized, Rekord<T> builder);
}
