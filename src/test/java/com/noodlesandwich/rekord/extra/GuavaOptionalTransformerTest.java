package com.noodlesandwich.rekord.extra;

import com.google.common.base.Optional;
import com.noodlesandwich.rekord.transformers.Transformer;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class GuavaOptionalTransformerTest {
    private final Transformer<String, Optional<String>> transformer = GuavaTransformers.asOptional();

    @Test public void
    transforms_a_value_to_an_Optional_of_the_value() {
        MatcherAssert.assertThat(transformer.transform("Well, hello!"), is(Optional.of("Well, hello!")));
    }

    @Test public void
    transforms_a_null_value_to_an_absent_Optional_value() {
        MatcherAssert.assertThat(transformer.transform(null), is(Optional.<String>absent()));
    }
}
