package com.noodlesandwich.rekord;

import org.junit.Test;

import static com.noodlesandwich.rekord.RekordTest.Bread.Brown;
import static com.noodlesandwich.rekord.RekordTest.Filling.Cheese;
import static com.noodlesandwich.rekord.RekordTest.Filling.Lettuce;
import static com.noodlesandwich.rekord.RekordTest.Style.Burger;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class RekordTest {
    @Test public void
    a_Rekord_contains_a_value() {
        Rekord<Sandvich> sandvich = Rekord.<Sandvich>create()
                .with(Sandvich.filling, Cheese)
                .build();
        assertThat(sandvich.get(Sandvich.filling), is(Cheese));
    }

    @Test public void
    a_Rekord_contains_multiple_values_identified_by_a_key() {
        Rekord<Sandvich> sandvich = Rekord.<Sandvich>create()
                .with(Sandvich.filling, Lettuce)
                .with(Sandvich.bread, Brown)
                .with(Sandvich.style, Burger)
                .build();
        assertThat(sandvich.get(Sandvich.filling), is(Lettuce));
        assertThat(sandvich.get(Sandvich.bread), is(Brown));
        assertThat(sandvich.get(Sandvich.style), is(Burger));
    }

    public static interface Sandvich extends RekordType {
        Key<Sandvich, Filling> filling = new Key<>();
        Key<Sandvich, Bread> bread = new Key<>();
        Key<Sandvich, Style> style = new Key<>();
    }

    public static enum Filling {
        Cheese,
        Lettuce
    }

    public static enum Bread {
        Brown
    }

    public static enum Style {
        Burger
    }
}
