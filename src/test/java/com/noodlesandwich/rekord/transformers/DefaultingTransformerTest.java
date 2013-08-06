package com.noodlesandwich.rekord.transformers;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class DefaultingTransformerTest {
    @Test public void
    transforms_any_input_to_itself() {
        Transformer<Integer, Integer> transformer = new DefaultingTransformer<>(999);
        assertThat(transformer.transformInput(36), is(36));
    }

    @Test public void
    transforms_a_null_input_to_itself() {
        Transformer<Integer, Integer> transformer = new DefaultingTransformer<>(999);
        assertThat(transformer.transformInput(null), is(nullValue()));
    }

    @Test public void
    transforms_a_value_to_itself() {
        Transformer<String, String> transformer = new DefaultingTransformer<>("this is a default string");
        assertThat(transformer.transformOutput("some string"), is("some string"));
    }

    @Test public void
    returns_the_default_value_if_the_property_map_does_not_contain_the_key() {
        Transformer<String, String> transformer = new DefaultingTransformer<>("this is a default string");
        assertThat(transformer.transformOutput(null), is("this is a default string"));
    }
}
