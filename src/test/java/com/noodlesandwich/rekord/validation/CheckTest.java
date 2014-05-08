package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst.Style.Chopped;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst.Style.Whole;
import static com.noodlesandwich.rekord.validation.RekordMatchers.aRekordOf;
import static com.noodlesandwich.rekord.validation.RekordMatchers.that;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class CheckTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final ValidatingRekord<Bratwurst> validatingBratwurst
            = ValidatingRekord.validating(Bratwurst.rekord).expecting(that(itIsChopped()));

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

        expectedException.expect(an(InvalidRekordException.class).withTheMessage("Expected that an unspecified validation would pass, but it failed."));

        invalidBratwurst.fix();
    }

    private static Check<Bratwurst> itIsChopped() {
        return new Check<Bratwurst>() {
            @Override public boolean check(FixedRekord<Bratwurst> rekord) {
                return rekord.get(Bratwurst.style) == Chopped;
            }
        };
    }
}