package com.noodlesandwich.rekord.extra;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.RekordType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.extra.Validation.validatesItsInput;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public final class HamcrestValidatorTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test public void
    validates_input_immediately() {
        expectedException.expect(allOf(is(instanceOf(ValidationException.class)),
                                       hasProperty("message", equalTo("<15> was greater than <10>"))));

        Rekord.of(Box.class)
                .with(Box.lessThanTen, 15);
    }

    @Test public void
    returns_valid_input_unchanged() {
        Rekord<Box> box = Rekord.of(Box.class)
                                .with(Box.lessThanTen, 8);

        assertThat(box.get(Box.lessThanTen), is(8));
    }

    @Test public void
    validates_output_on_retrieval() {
        Rekord<Box> box = Rekord.of(Box.class)
                                .with(Box.anyNumber, 100);

        expectedException.expect(allOf(is(instanceOf(ValidationException.class)),
                                       hasProperty("message", equalTo("<100> was greater than <10>"))));

        box.get(Box.lessThanTen);
    }

    private static interface Box extends RekordType {
        Key<Box,Integer> anyNumber = Key.named("any number");
        Key<Box, Integer> lessThanTen = anyNumber.that(validatesItsInput(is(lessThan(10))));
    }
}
