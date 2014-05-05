package com.noodlesandwich.rekord.serialization;

public interface Serializer<A, R> {
    AccumulatorBuilder<A> start(String name);
    R finish(AccumulatorBuilder<A> accumulator);

    public static interface SerializedProperty<A> {
        A serialized();
    }

    public static interface Builder<A> {
        SerializedProperty<A> single(String name, Object value);
        AccumulatorBuilder<A> collection(String name);
        AccumulatorBuilder<A> nest(String name);
    }

    public static interface Accumulator<A> extends SerializedProperty<A> {
        void accumulate(String name, SerializedProperty<A> property);
    }

    public static interface AccumulatorBuilder<A> extends Accumulator<A>, Builder<A> { }
}
