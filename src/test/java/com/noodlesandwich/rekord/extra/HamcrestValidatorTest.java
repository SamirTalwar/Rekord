package com.noodlesandwich.rekord.extra;

import com.noodlesandwich.rekord.Rekord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.Rekords.Box;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public final class HamcrestValidatorTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test public void
    validates_input_immediately() {
        expectedException.expect(allOf(
                is(instanceOf(ValidationException.class)),
                hasProperty("message", equalTo("<15> was greater than <10>"))));

        Box.rekord.with(Box.lessThanTen, 15);
    }

    @Test public void
    returns_valid_input_unchanged() {
        Rekord<Box> box = Box.rekord
                .with(Box.lessThanTen, 8);

        assertThat(box.get(Box.lessThanTen), is(8));
    }

    @Test public void
    validates_output_on_retrieval() {
        Rekord<Box> box = Box.rekord
                .with(Box.anyNumber, 100);

        expectedException.expect(allOf(is(instanceOf(ValidationException.class)),
                                       hasProperty("message", equalTo("<100> was greater than <10>"))));

        box.get(Box.lessThanTen);
    }
}
