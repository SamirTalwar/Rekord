package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.testobjects.Measurement;
import org.junit.Test;

import static com.noodlesandwich.rekord.matchers.RekordMatchers.aRekordOf;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bier;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class EverythingValidatorTest {
    private static final ValidatingRekord<Bier> validatingBier
            = ValidRekord.validating(Bier.rekord).allowing(Validators.<Bier>everything());

    @Test public void
    accepts_everything() throws InvalidRekordException {
        FixedRekord<Bier> bier = validatingBier
                .with(Bier.volume, Measurement.of(1).l())
                .fix();

        assertThat(bier, is(aRekordOf(Bier.class)
                .with(Bier.volume, Measurement.of(1).l())));
    }
}