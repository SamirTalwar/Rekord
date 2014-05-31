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

    @Test public void
    can_be_composed() {
        InvertibleFunction<Integer, String> stringIncrementor =
                Functions.compose(stringify, Functions.invertibleFrom(increment, decrement));
                Functions.compose(stringify, Functions.invertibleFrom(increment, decrement));

        assertThat(stringIncrementor.applyForward(12), is("13"));
        assertThat(stringIncrementor.applyBackward("1"), is(0));
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

    private static final InvertibleFunction<Integer, String> stringify = new InvertibleFunction<Integer, String>() {
        @Override
        public String applyForward(Integer input) {
            return input.toString();
        }

        @Override
        public Integer applyBackward(String input) {
            return Integer.parseInt(input);
        }
    };
}
