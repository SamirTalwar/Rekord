package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.SimpleKey;
import com.noodlesandwich.rekord.properties.UnacceptableKeyException;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Ham;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Jam;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Burger;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Roll;
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
        ValidRekord<Sandvich> sandvich = validatingSandvich
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
        expectedException.expect(an(InvalidRekordException.class)
                .withTheMessage("Expected that burgers are gross, but the sandvich style was <Burger>."));

        validatingSandvich
                .with(White, Sandvich.bread)
                .with(Sandvich.filling, Ham)
                .with(Sandvich.style, Burger)
                .fix();
    }

    @Test public void
    a_Rekord_must_know_all_possible_keys_in_advance() {
        expectedException.expect(an(UnacceptableKeyException.class)
                .withTheMessage("The key \"size\" is not a valid key for this Rekord."));

        Key<Sandvich, Integer> size = SimpleKey.named("size");

        validatingSandvich
                .with(size, 4);
    }

    private static Matcher<Rekord<Sandvich>> noBurgers() {
        return new TypeSafeDiagnosingMatcher<Rekord<Sandvich>>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("burgers are gross");
            }

            @Override
            protected boolean matchesSafely(Rekord<Sandvich> rekord, Description mismatchDescription) {
                Sandvich.Style style = rekord.get(Sandvich.style);
                mismatchDescription.appendText("the sandvich style was ").appendValue(style);
                return style != Burger;
            }
        };
    }
}
