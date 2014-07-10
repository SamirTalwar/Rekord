package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Rekord;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.test.ExceptionMatcher.a;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Ham;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Burger;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Flat;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Roll;
import static com.noodlesandwich.rekord.validation.RekordMatchers.hasKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public final class DefaultedKeyTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final Key<Sandvich, Style> styleDefaultingToFlat
            = DefaultedKey.wrapping(Sandvich.style).defaultingTo(Flat);
    private static final Key<Sandvich, Style> pointlessKey
            = DefaultedKey.wrapping(styleDefaultingToFlat).defaultingTo(Roll);

    @Test public void
    delegates_to_the_underlying_key_if_there_is_a_stored_value() {
        Rekord<Sandvich> whiteRoll = Sandvich.rekord
                .with(Sandvich.bread, White)
                .with(styleDefaultingToFlat, Roll);

        assertThat(whiteRoll.get(styleDefaultingToFlat), is(Roll));
    }

    @Test public void
    provides_the_default_value_if_there_is_no_value_available() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.bread, White);

        assertThat(sandvich.get(styleDefaultingToFlat), is(Flat));
    }

    @Test public void
    delegates_its_existence_test_to_the_underlying_key() {
        assertThat(Sandvich.rekord, not(hasKey(styleDefaultingToFlat)));
    }

    @Test public void
    delegates_retrieval_to_the_underlying_key() {
        Rekord<Sandvich> hamSandvich = Sandvich.rekord
                .with(Sandvich.filling, Ham);

        assertThat(hamSandvich.get(pointlessKey), is(Flat));
    }

    @Test public void
    the_underlying_key_can_be_used_for_retrieval() {
        Rekord<Sandvich> whiteRoll = Sandvich.rekord
                .with(Sandvich.filling, Cheese)
                .with(styleDefaultingToFlat, Burger);

        assertThat(whiteRoll.get(Sandvich.style), is(Burger));
    }

    @Test public void
    the_underlying_key_can_be_used_for_storage() {
        Rekord<Sandvich> whiteRoll = Sandvich.rekord
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Burger);

        assertThat(whiteRoll.get(styleDefaultingToFlat), is(Burger));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    provides_the_underlying_key_when_iterating() {
        assertThat(styleDefaultingToFlat, Matchers.<Key<? super Sandvich, ?>>contains(Sandvich.style));
    }

    @Test public void
    rejects_a_null_key() {
        expectedException.expect(a(NullPointerException.class)
                .withTheMessage("The underlying key of a DefaultedKey must not be null."));

        DefaultedKey.wrapping(null);
    }

    @Test public void
    rejects_a_null_default_value() {
        expectedException.expect(a(NullPointerException.class)
                .withTheMessage("The default value of a DefaultedKey must not be null."));

        DefaultedKey.wrapping(Sandvich.bread).defaultingTo(null);
    }
}
