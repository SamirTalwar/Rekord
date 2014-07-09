package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.test.Measurement;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Bier;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Person;
import static com.noodlesandwich.rekord.validation.RekordMatchers.aRekordOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public final class RekordMatcherTest {
    @Test public void
    matches_a_Rekord_by_equality() {
        Rekord<Bier> bottle = Bier.rekord
                .with(Bier.head, Measurement.of(1).cm())
                .with(Bier.volume, Measurement.of(330).ml());

        assertThat(bottle, is(aRekordOf(Bier.class)
                .with(Bier.head, Measurement.of(1).cm())
                .with(Bier.volume, Measurement.of(330).ml())));
    }

    @Test public void
    reports_when_a_Rekord_does_not_match_another() {
        Rekord<Bier> bottle = Bier.rekord
                .with(Bier.head, Measurement.of(1).cm())
                .with(Bier.volume, Measurement.of(330).ml());

        assertThat(bottle, is(not(aRekordOf(Bier.class)
                .with(Bier.head, Measurement.of(2).cm())
                .with(Bier.volume, Measurement.of(568).ml()))));
    }

    @Test public void
    can_match_using_a_type_parameter_instead_of_a_Class_object() {
        Rekord<Bier> stein = Bier.rekord
                .with(Bier.head, Measurement.of(3).cm())
                .with(Bier.volume, Measurement.of(1).l());

        assertThat(stein, is(RekordMatchers.<Bier>aRekordNamed("Bier")
                .with(Bier.head, Measurement.of(3).cm())
                .with(Bier.volume, Measurement.of(1).l())));
    }

    @Test public void
    does_not_match_if_the_Rekord_has_more_properties_than_expected() {
        Rekord<Bier> stein = Bier.rekord
                .with(Bier.head, Measurement.of(3).cm())
                .with(Bier.volume, Measurement.of(1).l());

        assertThat(stein, is(not(aRekordOf(Bier.class)
                .with(Bier.volume, Measurement.of(1).l()))));
    }

    @Test public void
    matches_using_nested_matchers() {
        Rekord<Person> steve = Person.rekord
                .with(Person.firstName, "Steve")
                .with(Person.lastName, "Humperdick")
                .with(Person.age, 32);

        assertThat(steve, is(aRekordOf(Person.class)
                .with(Person.firstName, equalToIgnoringCase("steVE"))
                .with(Person.lastName, containsString("Hump"))
                .with(Person.age, greaterThan(20))));
    }
}
