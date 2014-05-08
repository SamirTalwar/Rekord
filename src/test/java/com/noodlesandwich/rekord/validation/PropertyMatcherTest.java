package com.noodlesandwich.rekord.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.Rekords.Box;
import static com.noodlesandwich.rekord.validation.RekordMatchers.hasProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public final class PropertyMatcherTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final ValidatingRekord<Box> validatingBox = ValidatingRekord.of(Box.class)
            .accepting(Box.number)
            .expecting(hasProperty(Box.number, is(greaterThan(5))));

    @Test public void
    accepts_a_rekord_with_a_property_that_validates() throws InvalidRekordException {
        ValidRekord<Box> box = validatingBox
                .with(Box.number, 8)
                .fix();

        assertThat(box.get(Box.number), is(8));
    }

    @Test public void
    rejects_a_rekord_when_the_property_does_not_validate() throws InvalidRekordException {
        expectedException.expect(an(InvalidRekordException.class)
                .withTheMessage("Expected that the rekord has the property <number> with the value is a value greater than <5>, but the value was <3>."));

        validatingBox
                .with(Box.number, 3)
                .fix();
    }
}
