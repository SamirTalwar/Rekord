package com.noodlesandwich.rekord;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.RekordTest.Bratwurst.Style.Chopped;
import static com.noodlesandwich.rekord.RekordTest.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.RekordTest.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.RekordTest.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.RekordTest.Sandvich.Filling.Lettuce;
import static com.noodlesandwich.rekord.RekordTest.Sandvich.Style.Burger;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

public final class RekordTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test public void
    a_Rekord_contains_a_value() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Cheese)
                .build();
        assertThat(sandvich.get(Sandvich.filling), is(Cheese));
    }

    @Test public void
    a_Rekord_contains_multiple_values_identified_by_a_key() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
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
        Rekord<Bratwurst> wurst = Rekord.of(Bratwurst.class)
                .with(Wurst.curvature, 0.3)
                .with(Bratwurst.style, Chopped)
                .build();
        assertThat(wurst.get(Wurst.curvature), is(0.3));
        assertThat(wurst.get(Bratwurst.style), is(Chopped));
    }

    @Test public void
    the_key_of_a_Rekord_property_cannot_be_null() {
        expectedException.expect(NullPointerException.class);

        Rekord.of(Bratwurst.class)
                .with(null, "Random value");
    }

    @Test public void
    a_Rekord_can_be_built_from_another_Rekord() {
        Rekord<Sandvich> cheeseSandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.bread, Brown)
                .build();

        Rekord<Sandvich> cheeseBurger = cheeseSandvich.but()
                .with(Sandvich.bread, White)
                .with(Sandvich.style, Burger)
                .build();

        assertThat(cheeseBurger.get(Sandvich.filling), is(Cheese));
        assertThat(cheeseBurger.get(Sandvich.bread), is(White));
        assertThat(cheeseBurger.get(Sandvich.style), is(Burger));
    }

    @Test public void
    after_building_a_Rekord_from_another_Rekord_the_original_does_not_mutate() {
        Rekord<Sandvich> cheeseSandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.bread, Brown)
                .build();

        cheeseSandvich.but()
                .with(Sandvich.bread, White)
                .with(Sandvich.style, Burger)
                .build();

        assertThat(cheeseSandvich.get(Sandvich.filling), is(Cheese));
        assertThat(cheeseSandvich.get(Sandvich.bread), is(Brown));
        assertThat(cheeseSandvich.get(Sandvich.style), is(nullValue()));
    }

    @Test public void
    two_Rekords_with_the_same_keys_and_values_are_equal() {
        Rekord<Bier> bier = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm())
                .build();

        assertThat(bier, is(equalTo(Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm())
                .build())));
    }

    @Test public void
    two_Rekords_with_the_same_keys_but_different_values_are_not_equal() {
        Rekord<Bier> englishBeer = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm())
                .build();

        Rekord<Bier> germanBier = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(1).l())
                .with(Bier.head, Measurement.of(4).cm())
                .build();

        assertThat(englishBeer, is(not(equalTo(germanBier))));
    }

    @Test public void
    two_Rekords_with_different_keys_are_not_equal() {
        Rekord<Bier> cider = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .build();

        Rekord<Bier> stout = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(4).cm())
                .build();

        assertThat(cider, is(not(equalTo(stout))));
    }

    @Test public void
    a_Rekord_is_not_equal_to_another_type() {
        Rekord<Bier> bier = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(500).ml())
                .build();

        assertThat(bier, is(not(equalTo((Object) new Bier() {
        }))));
    }

    @Test public void
    two_Rekords_with_the_same_properties_have_the_same_hash_code() {
        Rekord<Bier> bier = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm())
                .build();

        Rekord<Bier> anotherBier = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm())
                .build();

        assertThat(bier.hashCode(), is(equalTo(anotherBier.hashCode())));
    }

    @Test public void
    two_Rekords_with_different_properties_are_likely_to_have_a_different_hash_code() {
        Rekord<Bier> ale = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(1).cm())
                .build();

        Rekord<Bier> pilsner = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(1).l())
                .with(Bier.head, Measurement.of(5).cm())
                .build();

        assertThat(ale.hashCode(), is(not(equalTo(pilsner.hashCode()))));
    }

    @Test public void
    a_Rekord_is_serializable_as_a_String() {
        Rekord<Bier> delicious = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(3).cm())
                .build();

        assertThat(delicious, hasToString(allOf(startsWith("Bier"), containsString("head=3cm"), containsString("volume=568ml"))));
    }

    public static interface Sandvich extends RekordType {
        Key<Sandvich, Filling> filling = Key.named("filling");
        Key<Sandvich, Bread> bread = Key.named("bread");
        Key<Sandvich, Style> style = Key.named("style");

        public static enum Filling {
            Cheese,
            Lettuce
        }

        public static enum Bread {
            Brown,
            White
        }

        public static enum Style {
            Burger
        }
    }

    public static interface Wurst extends RekordType {
        Key<Wurst, Double> curvature = Key.named("curvature");
    }

    public static interface Bratwurst extends Wurst {
        Key<Bratwurst, Style> style = Key.named("style");

        public static enum Style {
            Chopped
        }
    }

    public static interface Bier extends RekordType {
        Key<Bier, Measurement.Volume> volume = Key.named("volume");
        Key<Bier, Measurement.Length> head = Key.named("head");
    }
}
