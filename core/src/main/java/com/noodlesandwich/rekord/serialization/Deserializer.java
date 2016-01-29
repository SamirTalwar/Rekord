package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.Rekord;

public interface Deserializer<S, E extends Exception> {
    <T> Rekord<T> deserialize(S serialized, Rekord<T> builder) throws E;
}
