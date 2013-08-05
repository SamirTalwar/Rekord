package com.noodlesandwich.rekord.extra;

import com.google.common.base.Function;
import com.noodlesandwich.rekord.transformers.Transformer;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GuavaFunctionTransformerTest {
    @Test public void
    delegates_to_a_Guava_function() {
        Transformer<String, Integer> transformer = GuavaTransformers.fromFunction(new ParseInteger());
        assertThat(transformer.transform("123"), is(123));
    }

    private static class ParseInteger implements Function<String, Integer> {
        @Override public Integer apply(String input) {
            return Integer.parseInt(input);
        }
    }
}
