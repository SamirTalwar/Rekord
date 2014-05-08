package com.noodlesandwich.rekord.transformers;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class IdentityTransformerTest {
    @Test public void
    transforms_any_input_to_itself() {
        Transformer<String, String> transformer = Transformers.identity();
        assertThat(transformer.transformInput("abc"), is("abc"));
    }
}
