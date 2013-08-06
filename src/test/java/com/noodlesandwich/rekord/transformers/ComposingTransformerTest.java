package com.noodlesandwich.rekord.transformers;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class ComposingTransformerTest {
    @Test public void
    composes_two_transformers_for_input() {
        Transformer<Integer, String> transformer = new ComposingTransformer<>(convertToString(), add(5));
        assertThat(transformer.transformInput("80"), is(75));
    }

    @Test public void
    composes_two_transformers_for_output() {
        Transformer<Integer, String> transformer = new ComposingTransformer<>(convertToString(), add(5));
        assertThat(transformer.transformOutput(10), is("15"));
    }

    private Transformer<Integer, Integer> add(final int amount) {
       return new Transformer<Integer, Integer>() {
           @Override
           public Integer transformInput(Integer value) {
               return value - amount;
           }

           @Override public Integer transformOutput(Integer value) {
               return value + amount;
           }
       };
    }

    private Transformer<Integer, String> convertToString() {
        return new Transformer<Integer, String>() {
            @Override
            public Integer transformInput(String value) {
                return Integer.parseInt(value);
            }

            @Override public String transformOutput(Integer value) {
                return value.toString();
            }
        };
    }
}
