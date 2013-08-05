package com.noodlesandwich.rekord.transformers;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DefaultingTransformerTest {
    @Test public void
    transforms_a_value_to_itself() {
        Transformer<String, String> transformer = new DefaultingTransformer<>("this is a default string");
        assertThat(transformer.transform("some string"), is("some string"));
    }

    @Test public void
    returns_the_default_value_if_the_property_map_does_not_contain_the_key() {
        Transformer<String, String> transformer = new DefaultingTransformer<>("this is a default string");
        assertThat(transformer.transform(null), is("this is a default string"));
    }
}
