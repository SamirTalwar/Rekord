package com.noodlesandwich.rekord.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bier;
import static com.noodlesandwich.rekord.validation.Validators.UnspecifiedInvalidRekordException;

public final class NothingValidatorTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final ValidatingRekord<Bier> invalidBier
            = ValidatingRekord.validating(Bier.rekord).expecting(Validators.<Bier>toAlwaysFail());

    @Test public void
    rejects_a_rekord_which_fails_the_test_with_a_standard_exception() throws InvalidRekordException {
        expectedException.expect(an(UnspecifiedInvalidRekordException.class).withoutAMessage());

        invalidBier.fix();
    }
}