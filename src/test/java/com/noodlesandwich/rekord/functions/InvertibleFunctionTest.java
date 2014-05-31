package com.noodlesandwich.rekord.functions;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class InvertibleFunctionTest {
    @Test public void
    can_be_constructed_from_a_pair_of_functions() {
        InvertibleFunction<Integer, Integer> incrementor = Functions.invertibleFrom(increment, decrement);

        assertThat(incrementor.applyForward(3), is(4));
        assertThat(incrementor.applyBackward(9), is(8));
    }

    private static final Function<Integer, Integer> increment = new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer input) {
            return input + 1;
        }
    };

    private static final Function<Integer, Integer> decrement = new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer input) {
            return input - 1;
        }
    };
}
