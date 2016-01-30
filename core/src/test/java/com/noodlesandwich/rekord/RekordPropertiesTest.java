package com.noodlesandwich.rekord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.noodlesandwich.rekord.properties.Property;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Address;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Jar;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Person;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Lettuce;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Burger;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Flat;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Wurst;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class RekordPropertiesTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test public void
    a_Rekord_contains_a_value() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.filling, Cheese);

        assertThat(sandvich, is(Sandvich.rekord
                .with(Sandvich.filling, Cheese)));
    }

    @Test public void
    a_Rekord_contains_multiple_values_identified_by_a_key() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.filling, Lettuce)
                .with(Brown, Sandvich.bread)
                .with(Sandvich.style, Burger);

        assertThat(sandvich, is(Sandvich.rekord
                .with(Sandvich.filling, Lettuce)
                .with(Sandvich.bread, Brown)
                .with(Sandvich.style, Burger)));
    }

    @Test public void
    Rekords_can_be_generic() {
        List<Jar.Cookie> cookies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cookies.add(new Jar.Cookie());
        }

        Rekord<Jar<Jar.Cookie>> cookieJar = Jar.ofCookies()
                .with(Jar.<Jar.Cookie>contents(), cookies);

        assertThat(cookieJar.get(Jar.<Jar.Cookie>contents()), is(tenCookies()));
    }

    @Test public void
    Rekords_can_be_nested() {
        Rekord<Person> hans = Person.rekord
                .with(Person.firstName, "Hans")
                .with(Person.address, Address.rekord
                    .with(Address.houseNumber, 123)
                    .with(Address.street, "Kaiserstraße")
                    .with(Address.city, "Frankfurt")
                    .with(Address.postalCode, "60329"));

        assertThat(hans, is(Person.rekord
                .with(Person.firstName, "Hans")
                .with(Person.address, Address.rekord
                    .with(Address.houseNumber, 123)
                    .with(Address.street, "Kaiserstraße")
                    .with(Address.city, "Frankfurt")
                    .with(Address.postalCode, "60329"))));
    }

    @Test public void
    Rekords_can_return_individual_values_when_indexed_by_the_key() {
        Rekord<Wurst> wurst = Wurst.rekord
                .with(Wurst.curvature, 0.1);

        assertThat(wurst.get(Wurst.curvature), is(0.1));
    }

    @Test public void
    properties_can_be_removed_from_a_Rekord() {
        Rekord<Address> wurst = Address.rekord
                .with(Address.houseNumber, 99)
                .with(Address.city, "London")
                .without(Address.houseNumber);

        assertThat(wurst, is(Address.rekord
                .with(Address.city, "London")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    a_Rekord_will_provide_its_properties_as_an_Iterable() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.filling, Lettuce)
                .with(Sandvich.style, Flat);

        assertThat(sandvich.properties(), Matchers.<Property<Sandvich, ?>>containsInAnyOrder(
                new Property<>(Sandvich.filling, Lettuce),
                new Property<>(Sandvich.style, Flat)));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Matcher<Collection<Jar.Cookie>> tenCookies() {
        return (Matcher<Collection<Jar.Cookie>>) (Matcher) hasSize(10);
    }
}
