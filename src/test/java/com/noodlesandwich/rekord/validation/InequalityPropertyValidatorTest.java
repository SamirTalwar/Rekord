package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.Rekords.Box;
import static com.noodlesandwich.rekord.validation.Validators.isNotEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public final class InequalityPropertyValidatorTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final ValidatingRekord<Box> validatingBox = ValidatingRekord.of(Box.class)
            .accepting(Box.number)
            .allowing(Validators.theProperty(Box.number, isNotEqualTo(15)));

    @Test public void
    accepts_a_value_not_equal_to_the_expected_value() throws InvalidRekordException {
        FixedRekord<Box> box = validatingBox
                .with(Box.number, 10)
                .fix();

        assertThat(box.get(Box.number), is(10));
    }

    @Test public void
    accepts_a_missing_value() throws InvalidRekordException {
        FixedRekord<Box> box = validatingBox
                .fix();

        assertThat(box.get(Box.number), is(nullValue()));
    }

    @Test public void
    rejects_a_value_equal_to_the_expected_value() throws InvalidRekordException {
        expectedException.expect(an(InvalidRekordException.class)
                .withTheMessage("Expected any value but <15>, but got <15>."));

        validatingBox
                .with(Box.number, 15)
                .fix();
    }
}
