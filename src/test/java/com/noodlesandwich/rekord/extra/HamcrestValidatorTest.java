package com.noodlesandwich.rekord.extra;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.validation.InvalidRekordException;
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
    returns_valid_input_unchanged() throws InvalidRekordException {
        FixedRekord<Box> box = Box.hamcrestValidatingRekord
                .with(Box.number, 8)
                .fix();

        assertThat(box.get(Box.number), is(8));
    }

    @Test public void
    validates_input_immediately() throws InvalidRekordException {
        expectedException.expect(allOf(
                is(instanceOf(InvalidRekordException.class)),
                hasProperty("message", equalTo("<15> was greater than <10>"))));

        Box.hamcrestValidatingRekord
                .with(Box.number, 15)
                .fix();
    }
}
