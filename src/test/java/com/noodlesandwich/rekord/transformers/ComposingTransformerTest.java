package com.noodlesandwich.rekord.transformers;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ComposingTransformerTest {
    @Test public void
    composes_two_transformers() {
        Transformer<Integer, String> transformer = new ComposingTransformer<>(convertToString(), add(5));
        assertThat(transformer.transform(10), is("15"));
    }

    private Transformer<Integer, Integer> add(final int amount) {
       return new Transformer<Integer, Integer>() {
           @Override public Integer transform(Integer value) {
               return value + amount;
           }
       };
    }

    private Transformer<Object, String> convertToString() {
        return new Transformer<Object, String>() {
            @Override public String transform(Object value) {
                return value.toString();
            }
        };
    }
}
