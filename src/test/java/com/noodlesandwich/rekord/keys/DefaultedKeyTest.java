package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Rekord;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Flat;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Roll;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class DefaultedKeyTest {
    private static final Key<Sandvich, Style> styleDefaultingToFlat
            = DefaultedKey.wrapping(Sandvich.style).defaultingTo(Flat);

    @Test public void
    delegates_to_the_provided_key_if_there_is_a_stored_value() {
        Rekord<Sandvich> whiteRoll = Sandvich.rekord
                .with(Sandvich.bread, White)
                .with(Sandvich.style, Roll);

        assertThat(whiteRoll.get(styleDefaultingToFlat), is(Roll));
    }

    @Test public void
    provides_the_default_value_if_there_is_no_value_available() {
        Rekord<Sandvich> whiteRoll = Sandvich.rekord
                .with(Sandvich.bread, White);

        assertThat(whiteRoll.get(styleDefaultingToFlat), is(Flat));
    }
}
