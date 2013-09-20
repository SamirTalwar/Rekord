package com.noodlesandwich.rekord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.noodlesandwich.rekord.matchers.RekordMatchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static com.noodlesandwich.rekord.testobjects.Rekords.Address;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst.Style.Chopped;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst.Style.Whole;
import static com.noodlesandwich.rekord.testobjects.Rekords.Jar;
import static com.noodlesandwich.rekord.testobjects.Rekords.Person;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Lettuce;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Burger;
import static com.noodlesandwich.rekord.testobjects.Rekords.Wurst;

public final class RekordPropertiesTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test public void
    a_Rekord_contains_a_value() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Cheese);

        assertThat(sandvich, is(Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Cheese)));
    }

    @Test public void
    a_Rekord_contains_multiple_values_identified_by_a_key() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Lettuce)
                .with(Brown, Sandvich.bread)
                .with(Sandvich.style, Burger);

        assertThat(sandvich, is(Rekord.of(Sandvich.class)
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

        Rekord<Jar<Jar.Cookie>> cookieJar = Rekord.<Jar<Jar.Cookie>>create("cookie jar")
                .with(Jar.<Jar.Cookie>contents(), cookies);

        assertThat(cookieJar, is(RekordMatchers.<Jar<Jar.Cookie>>aRekordNamed("cookie jar")
                .with(Jar.<Jar.Cookie>contents(), tenCookies())));
    }

    @Test public void
    Rekords_can_be_nested() {
        Rekord<Person> hans = Rekord.of(Person.class)
                .with(Person.firstName, "Hans")
                .with(Person.address, Rekord.of(Address.class)
                    .with(Address.houseNumber, 123)
                    .with(Address.street, "Kaiserstraße")
                    .with(Address.city, "Frankfurt")
                    .with(Address.postalCode, "60329"));

        assertThat(hans, is(Rekord.of(Person.class)
                .with(Person.firstName, "Hans")
                .with(Person.address, Rekord.of(Address.class)
                    .with(Address.houseNumber, 123)
                    .with(Address.street, "Kaiserstraße")
                    .with(Address.city, "Frankfurt")
                    .with(Address.postalCode, "60329"))));
    }

    @Test public void
    keys_for_supertypes_of_a_RekordType_can_be_used_to_create_Rekord_properties() {
        Rekord<Bratwurst> wurst = Rekord.of(Bratwurst.class)
                .with(Wurst.curvature, 0.3)
                .with(Bratwurst.style, Chopped);

        assertThat(wurst, is(Rekord.of(Bratwurst.class)
                .with(Wurst.curvature, 0.3)
                .with(Bratwurst.style, Chopped)));
    }

    @Test public void
    Rekords_can_return_individual_values_when_indexed_by_the_key_including_keys_of_supertypes() {
        Rekord<Bratwurst> wurst = Rekord.of(Bratwurst.class)
                .with(Wurst.curvature, 0.1)
                .with(Bratwurst.style, Whole);

        assertThat(wurst.get(Wurst.curvature), is(0.1));
        assertThat(wurst.get(Bratwurst.style), is(Whole));
    }

    @Test public void
    the_key_of_a_Rekord_property_cannot_be_null() {
        expectedException.expect(allOf(instanceOf(NullPointerException.class),
                                 hasProperty("message", equalTo("Cannot construct a Rekord property with a null key."))));

        Rekord.of(Bratwurst.class)
                .with(null, "Random value");
    }

    @Test public void
    the_value_of_a_Rekord_property_cannot_be_null() {
        expectedException.expect(allOf(instanceOf(NullPointerException.class),
                                 hasProperty("message", equalTo("Cannot construct a Rekord property with a null value."))));

        Double nullValue = null;

        Rekord.of(Bratwurst.class)
                .with(Wurst.curvature, nullValue);
    }

    @Test public void
    properties_can_be_removed_from_a_Rekord() {
        Rekord<Bratwurst> wurst = Rekord.of(Bratwurst.class)
                .with(Wurst.curvature, 0.9)
                .with(Bratwurst.style, Whole)
                .without(Wurst.curvature);

        assertThat(wurst, is(Rekord.of(Bratwurst.class)
                .with(Bratwurst.style, Whole)));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Matcher<Collection<Jar.Cookie>> tenCookies() {
        return (Matcher<Collection<Jar.Cookie>>) (Matcher) hasSize(10);
    }
}
