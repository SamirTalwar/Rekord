package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.Rekords.Box;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class PropertyValidatorTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final ValidatingRekord<Box> validatingBox = ValidatingRekord.of(Box.class)
            .accepting(Box.number)
            .allowing(Validators.theProperty(Box.number, isMoreThanFive()));

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
                .withTheMessage("The value <3> was not more than <5>."));

        validatingBox
                .with(Box.number, 3)
                .fix();
    }

    private static PropertyValidator<Integer> isMoreThanFive() {
        return new PropertyValidator<Integer>() {
            @Override
            public void test(Integer value) throws InvalidRekordException {
                if (value <= 5) {
                    throw new InvalidRekordException(String.format("The value <%s> was not more than <5>.", value));
                }
            }
        };
    }
}
