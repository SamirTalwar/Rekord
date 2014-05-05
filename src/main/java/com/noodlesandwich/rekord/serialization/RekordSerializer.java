package com.noodlesandwich.rekord.serialization;

public interface RekordSerializer<A, R> {
    Serializer<A> start(String name);
    R finish(Serializer<A> serializer);

    public static interface SerializedProperty<A> {
        A serialized();
    }

    public static interface Builder<A> {
        SerializedProperty<A> single(String name, Object value);
        Serializer<A> collection(String name);
        Serializer<A> nest(String name);
    }

    public static interface Accumulator<A> extends SerializedProperty<A> {
        void accumulate(String name, SerializedProperty<A> property);
    }

    public static interface Serializer<A> extends Accumulator<A>, Builder<A> { }
}
