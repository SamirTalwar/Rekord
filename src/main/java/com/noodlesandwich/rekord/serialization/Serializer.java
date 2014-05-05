package com.noodlesandwich.rekord.serialization;

public interface Serializer<A, R> {
    Accumulator<A> start(String name);
    R finish(Accumulator<A> accumulator);

    public static interface SerializedProperty<A> {
        A serialized();
    }

    public static interface Accumulator<A> extends SerializedProperty<A> {
        SerializedProperty<A> single(String name, Object value);
        Accumulator<A> collection(String name);
        Accumulator<A> nest(String name);
        void accumulate(String name, SerializedProperty<A> property);
    }
}
