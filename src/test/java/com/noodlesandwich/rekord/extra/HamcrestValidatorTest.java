package com.noodlesandwich.rekord.extra;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.validation.InvalidRekordException;
import com.noodlesandwich.rekord.validation.ValidatingRekord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.extra.HamcrestValidator.theProperty;
import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.Rekords.Box;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public final class HamcrestValidatorTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    public static final ValidatingRekord<Box> validatingBox = ValidatingRekord.of(Box.class)
            .accepting(Box.number)
            .expecting(theProperty(Box.number, is(lessThan(10))));

    @Test public void
    accepts_a_rekord_with_a_property_that_validates() throws InvalidRekordException {
        FixedRekord<Box> box = validatingBox
                .with(Box.number, 8)
                .fix();

        assertThat(box.get(Box.number), is(8));
    }

    @Test public void
    rejects_a_rekord_when_the_property_does_not_validate() throws InvalidRekordException {
        expectedException.expect(an(InvalidRekordException.class)
                .withTheMessage("<15> was greater than <10>"));

        validatingBox
                .with(Box.number, 15)
                .fix();
    }
}
