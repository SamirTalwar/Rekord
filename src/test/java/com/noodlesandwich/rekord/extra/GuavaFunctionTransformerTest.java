package com.noodlesandwich.rekord.extra;

import org.junit.Test;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.noodlesandwich.rekord.transformers.Transformer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class GuavaFunctionTransformerTest {
    private final Transformer<String, Integer> transformer = GuavaTransformers.fromFunctions(
            Functions.toStringFunction(),
            new ParseInteger());

    @Test public void
    delegates_to_a_Guava_function_for_input() {
        assertThat(transformer.transformInput(987), is("987"));
    }

    @Test public void
    delegates_to_a_Guava_function_for_output() {
        assertThat(transformer.transformOutput("123"), is(123));
    }

    private static final class ParseInteger implements Function<String, Integer> {
        @Override public Integer apply(String input) {
            return Integer.parseInt(input);
        }
    }
}
