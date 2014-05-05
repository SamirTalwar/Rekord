package com.noodlesandwich.rekord.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.Rekords.Bier;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;

public final class NothingValidatorTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final ValidatingRekord<Bier> invalidBier
            = ValidRekord.validating(Bier.rekord).allowing(Validators.<Bier>nothing());

    @Test public void
    rejects_a_rekord_which_fails_the_test_with_a_standard_exception() throws InvalidRekordException {
        expectedException.expect(allOf(
                instanceOf(InvalidRekordException.class),
                hasProperty("message", equalTo("The rekord was invalid."))));

        invalidBier.fix();
    }
}