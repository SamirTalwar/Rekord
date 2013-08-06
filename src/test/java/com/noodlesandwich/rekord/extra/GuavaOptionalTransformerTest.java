package com.noodlesandwich.rekord.extra;

import com.google.common.base.Optional;
import com.noodlesandwich.rekord.transformers.Transformer;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class GuavaOptionalTransformerTest {
    @Test public void
    transforms_an_Optional_input_of_a_value_to_that_value() {
        Transformer<Integer, Optional<Integer>> transformer = GuavaTransformers.asOptional();
        assertThat(transformer.transformInput(Optional.of(720)), is(720));
    }

    @Test public void
    transforms_an_absent_Optional_input_to_null() {
        Transformer<Integer, Optional<Integer>> transformer = GuavaTransformers.asOptional();
        assertThat(transformer.transformInput(Optional.<Integer>absent()), is(nullValue()));
    }

    @Test public void
    transforms_a_value_to_an_Optional_of_the_value() {
        Transformer<String, Optional<String>> transformer = GuavaTransformers.asOptional();
        assertThat(transformer.transformOutput("Well, hello!"), is(Optional.of("Well, hello!")));
    }

    @Test public void
    transforms_a_null_value_to_an_absent_Optional_value() {
        Transformer<String, Optional<String>> transformer = GuavaTransformers.asOptional();
        assertThat(transformer.transformOutput(null), is(Optional.<String>absent()));
    }
}
