package com.noodlesandwich.rekord.functions;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class FunctionTest {
    @Test public void
    has_an_identity_function() {
        assertThat(Functions.<String>identity().apply("beep"), is("beep"));
    }

    @Test public void
    can_be_composed() {
        Function<Integer, String> stringMultiplier = Functions.compose(stringify(), multiplyBy(2));

        assertThat(stringMultiplier.apply(3), is("6"));
    }

    public static Function<Integer, Integer> multiplyBy(final int multiplicand) {
        return new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * multiplicand;
            }
        };
    }

    public static Function<Object, String> stringify() {
        return new Function<Object, String>() {
            @Override
            public String apply(Object input) {
                return input.toString();
            }
        };
    }
}
