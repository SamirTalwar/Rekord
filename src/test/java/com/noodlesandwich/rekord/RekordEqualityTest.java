package com.noodlesandwich.rekord;

import org.junit.Test;
import com.noodlesandwich.rekord.testobjects.Measurement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bier;

public final class RekordEqualityTest {
    @Test public void
    two_Rekords_with_the_same_keys_and_values_are_equal() {
        Rekord<Bier> bier = Bier.rekord
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm());

        assertThat(bier, is(equalTo(Bier.rekord
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm()))));
    }

    @Test public void
    two_Rekords_with_the_same_keys_but_different_values_are_not_equal() {
        Rekord<Bier> englishBeer = Bier.rekord
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm());

        Rekord<Bier> germanBier = Bier.rekord
                .with(Bier.volume, Measurement.of(1).l())
                .with(Bier.head, Measurement.of(4).cm());

        assertThat(englishBeer, is(not(equalTo(germanBier))));
    }

    @Test public void
    two_Rekords_with_different_keys_are_not_equal() {
        Rekord<Bier> cider = Bier.rekord
                .with(Bier.volume, Measurement.of(568).ml());

        Rekord<Bier> stout = Bier.rekord
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(4).cm());

        assertThat(cider, is(not(equalTo(stout))));
    }

    @Test public void
    a_Rekord_is_not_equal_to_another_type() {
        Rekord<Bier> bier = Bier.rekord
                .with(Bier.volume, Measurement.of(500).ml());

        Object bierObjekt = new Bier() { };
        assertThat(bier, is(not(equalTo(bierObjekt))));
    }

    @Test public void
    two_Rekords_with_the_same_properties_have_the_same_hash_code() {
        Rekord<Bier> bier = Bier.rekord
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm());

        Rekord<Bier> anotherBier = Bier.rekord
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm());

        assertThat(bier.hashCode(), is(equalTo(anotherBier.hashCode())));
    }

    @Test public void
    two_Rekords_with_different_properties_are_likely_to_have_a_different_hash_code() {
        Rekord<Bier> ale = Bier.rekord
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(1).cm());

        Rekord<Bier> pilsner = Bier.rekord
                .with(Bier.volume, Measurement.of(1).l())
                .with(Bier.head, Measurement.of(5).cm());

        assertThat(ale.hashCode(), is(not(equalTo(pilsner.hashCode()))));
    }
}
