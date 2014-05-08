package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Ham;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Jam;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Burger;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Roll;
import static com.noodlesandwich.rekord.validation.RekordMatchers.aRekordOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class ValidatingRekordTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final ValidatingRekord<Sandvich> validatingSandvich
            = ValidatingRekord.of(Sandvich.class)
                .accepting(Sandvich.filling, Sandvich.bread, Sandvich.style)
                .expecting(noBurgers());

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
                .withTheMessage("Expected that burgers are gross, but The sandvich style was <Burger>."));

        invalidSandvich.fix();
    }

    private static Matcher<FixedRekord<Sandvich>> noBurgers() {
        return new TypeSafeDiagnosingMatcher<FixedRekord<Sandvich>>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("burgers are gross");
            }

            @Override
            protected boolean matchesSafely(FixedRekord<Sandvich> rekord, Description mismatchDescription) {
                Sandvich.Style style = rekord.get(Sandvich.style);
                mismatchDescription.appendText("The sandvich style was ").appendValue(style);
                return style != Burger;
            }
        };
    }
}
