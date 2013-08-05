package com.noodlesandwich.rekord.matchers;

import com.noodlesandwich.rekord.Measurement;
import com.noodlesandwich.rekord.Rekord;
import org.junit.Test;

import static com.noodlesandwich.rekord.Rekords.Bier;
import static com.noodlesandwich.rekord.Rekords.Person;
import static com.noodlesandwich.rekord.matchers.RekordMatchers.aRekordOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class RekordMatchersTest {
    @Test public void
    matches_a_Rekord_by_equality() {
        Rekord<Bier> bottle = Rekord.of(Bier.class)
                .with(Bier.head, Measurement.of(1).cm())
                .with(Bier.volume, Measurement.of(330).ml());

        assertThat(bottle, is(aRekordOf(Bier.class)
                .with(Bier.head, Measurement.of(1).cm())
                .with(Bier.volume, Measurement.of(330).ml())));
    }

    @Test public void
    reports_when_a_Rekord_does_not_match_another() {
        Rekord<Bier> bottle = Rekord.of(Bier.class)
                .with(Bier.head, Measurement.of(1).cm())
                .with(Bier.volume, Measurement.of(330).ml());

        assertThat(bottle, is(not(aRekordOf(Bier.class)
                .with(Bier.head, Measurement.of(2).cm())
                .with(Bier.volume, Measurement.of(568).ml()))));
    }

    @Test public void
    can_match_using_a_type_parameter_instead_of_a_Class_object() {
        Rekord<Bier> stein = Rekord.of(Bier.class)
                .with(Bier.head, Measurement.of(3).cm())
                .with(Bier.volume, Measurement.of(1).l());

        assertThat(stein, is(RekordMatchers.<Bier>aRekordNamed("Bier")
                .with(Bier.head, Measurement.of(3).cm())
                .with(Bier.volume, Measurement.of(1).l())));
    }

    @Test public void
    does_not_match_if_the_Rekord_has_more_properties_than_expected() {
        Rekord<Bier> stein = Rekord.of(Bier.class)
                .with(Bier.head, Measurement.of(3).cm())
                .with(Bier.volume, Measurement.of(1).l());

        assertThat(stein, is(not(aRekordOf(Bier.class)
                .with(Bier.volume, Measurement.of(1).l()))));
    }

    @Test public void
    matches_using_nested_matchers() {
        Rekord<Person> steve = Rekord.of(Person.class)
                .with(Person.firstName, "Steve")
                .with(Person.lastName, "Humperdick")
                .with(Person.age, 32);

        assertThat(steve, is(aRekordOf(Person.class)
                .with(Person.firstName, equalToIgnoringCase("steVE"))
                .with(Person.lastName, containsString("Hump"))
                .with(Person.age, greaterThan(20))));
    }
}
