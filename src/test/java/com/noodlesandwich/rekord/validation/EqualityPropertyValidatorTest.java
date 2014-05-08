package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.Rekords.Box;
import static com.noodlesandwich.rekord.validation.Validators.isEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class EqualityPropertyValidatorTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final ValidatingRekord<Box> validatingBox = ValidatingRekord.of(Box.class)
            .accepting(Box.number)
            .allowing(Validators.theProperty(Box.number, isEqualTo(7)));

    @Test public void
    accepts_a_value_equal_to_the_expected_value() throws InvalidRekordException {
        FixedRekord<Box> box = validatingBox
                .with(Box.number, 7)
                .fix();

        assertThat(box.get(Box.number), is(7));
    }

    @Test public void
    rejects_a_value_not_equal_to_the_expected_value() throws InvalidRekordException {
        expectedException.expect(an(InvalidRekordException.class)
                .withTheMessage("Expected the value <7>, but got <6>."));

        validatingBox
                .with(Box.number, 6)
                .fix();
    }
}
