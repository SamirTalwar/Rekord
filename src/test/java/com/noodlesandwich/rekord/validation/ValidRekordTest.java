package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.matchers.RekordMatchers.aRekordOf;
import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Ham;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Jam;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Burger;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Roll;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class ValidRekordTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final ValidatingRekord<Sandvich> validatingSandvich
            = ValidatingRekord.of(Sandvich.class)
                .accepting(Sandvich.filling, Sandvich.bread, Sandvich.style)
                .allowing(noBurgers());

    @Test public void
    builds_an_unchangeable_rekord() throws InvalidRekordException {
        FixedRekord<Sandvich> sandvich = validatingSandvich
                .with(Brown, Sandvich.bread)
                .with(Sandvich.filling, Jam)
                .with(Sandvich.style, Roll)
                .fix();

        assertThat(sandvich, is(aRekordOf(Sandvich.class)
                .with(Sandvich.bread, Brown)
                .with(Sandvich.filling, Jam)
                .with(Sandvich.style, Roll)));
    }

    @Test public void
    rejects_invalid_rekords() throws InvalidRekordException {
        ValidatingRekord<Sandvich> invalidSandvich = validatingSandvich
                .with(White, Sandvich.bread)
                .with(Sandvich.filling, Ham)
                .with(Sandvich.style, Burger);

        expectedException.expect(an(InvalidRekordException.class)
                .withTheMessage("Burgers are gross."));

        invalidSandvich.fix();
    }

    private static Validator<Sandvich> noBurgers() {
        return new Validator<Sandvich>() {
            @Override public void test(FixedRekord<Sandvich> rekord) throws InvalidRekordException {
                if (rekord.get(Sandvich.style) == Burger) {
                    throw new InvalidRekordException("Burgers are gross.");
                }
            }
        };
    }
}
