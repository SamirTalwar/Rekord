package com.noodlesandwich.rekord;

import org.junit.Test;

import static com.noodlesandwich.rekord.Key.key;
import static com.noodlesandwich.rekord.RekordTest.Bratwurst.Style.Chopped;
import static com.noodlesandwich.rekord.RekordTest.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.RekordTest.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.RekordTest.Sandvich.Filling.Lettuce;
import static com.noodlesandwich.rekord.RekordTest.Sandvich.Style.Burger;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

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

    @Test public void
    keys_for_supertypes_of_a_RekordType_can_be_used_to_create_Rekord_properties() {
        Rekord<Bratwurst> wurst = Rekord.<Bratwurst>create()
                .with(Wurst.curvature, 0.3)
                .with(Bratwurst.style, Chopped)
                .build();
        assertThat(wurst.get(Wurst.curvature), is(0.3));
        assertThat(wurst.get(Bratwurst.style), is(Chopped));
    }

    @Test public void
    two_Rekords_with_the_same_keys_and_values_are_equal() {
        Rekord<Bier> bier = Rekord.<Bier>create()
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm())
                .build();

        assertThat(bier, is(equalTo(Rekord.<Bier>create()
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm())
                .build())));
    }

    @Test public void
    two_Rekords_with_the_same_keys_but_different_values_are_not_equal() {
        Rekord<Bier> englishBeer = Rekord.<Bier>create()
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm())
                .build();

        Rekord<Bier> germanBier = Rekord.<Bier>create()
                .with(Bier.volume, Measurement.of(1).l())
                .with(Bier.head, Measurement.of(4).cm())
                .build();

        assertThat(englishBeer, is(not(equalTo(germanBier))));
    }

    @Test public void
    two_Rekords_with_different_keys_are_not_equal() {
        Rekord<Bier> cider = Rekord.<Bier>create()
                .with(Bier.volume, Measurement.of(568).ml())
                .build();

        Rekord<Bier> stout = Rekord.<Bier>create()
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(4).cm())
                .build();

        assertThat(cider, is(not(equalTo(stout))));
    }

    @Test public void
    a_Rekord_is_not_equal_to_another_type() {
        Rekord<Bier> bier = Rekord.<Bier>create()
                .with(Bier.volume, Measurement.of(500).ml())
                .build();

        assertThat(bier, is(not(equalTo((Object) new Bier() {}))));
    }

    public static interface Sandvich extends RekordType {
        Key<Sandvich, Filling> filling = key();
        Key<Sandvich, Bread> bread = key();
        Key<Sandvich, Style> style = key();

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

    public static interface Wurst extends RekordType {
        Key<Wurst, Double> curvature = key();
    }

    public static interface Bratwurst extends Wurst {
        Key<Bratwurst, Style> style = key();

        public static enum Style {
            Chopped
        }
    }

    public static interface Bier extends RekordType {
        Key<Bier, Measurement.Volume> volume = key();
        Key<Bier, Measurement.Length> head = key();
    }
}
