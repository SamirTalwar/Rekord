package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.matchers.RekordMatchers.aRekordOf;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst.Style.Chopped;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst.Style.Whole;
import static com.noodlesandwich.rekord.validation.Validators.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public final class BooleanValidatorTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final ValidatingRekord<Bratwurst> validatingBratwurst
            = ValidRekord.validating(Bratwurst.rekord).allowing(when(chopped()));

    @Test public void
    allows_a_rekord_which_passes_the_test() throws InvalidRekordException {
        FixedRekord<Bratwurst> bratwurst = validatingBratwurst
                .with(Bratwurst.style, Chopped)
                .fix();

        assertThat(bratwurst, is(aRekordOf(Bratwurst.class)
                .with(Bratwurst.style, Chopped)));
    }

    @Test public void
    rejects_a_rekord_which_fails_the_test_with_a_standard_exception() throws InvalidRekordException {
        ValidatingRekord<Bratwurst> invalidBratwurst = validatingBratwurst
                .with(Bratwurst.style, Whole);

        expectedException.expect(allOf(
                instanceOf(InvalidRekordException.class),
                hasProperty("message", equalTo("The rekord was invalid."))));

        invalidBratwurst.fix();
    }

    private static BooleanValidator<Bratwurst> chopped() {
        return new BooleanValidator<Bratwurst>() {
            @Override public boolean test(FixedRekord<Bratwurst> rekord) {
                return rekord.get(Bratwurst.style) == Chopped;
            }
        };
    }
}