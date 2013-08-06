package com.noodlesandwich.rekord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import com.noodlesandwich.rekord.matchers.RekordMatchers;
import com.noodlesandwich.rekord.transformers.Transformer;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.Rekords.Address;
import static com.noodlesandwich.rekord.Rekords.Bier;
import static com.noodlesandwich.rekord.Rekords.Bratwurst;
import static com.noodlesandwich.rekord.Rekords.Bratwurst.Style.Chopped;
import static com.noodlesandwich.rekord.Rekords.Bratwurst.Style.Whole;
import static com.noodlesandwich.rekord.Rekords.Jar;
import static com.noodlesandwich.rekord.Rekords.Jar.Cookie;
import static com.noodlesandwich.rekord.Rekords.Person;
import static com.noodlesandwich.rekord.Rekords.Sandvich;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Filling.Ham;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Filling.Jam;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Filling.Lettuce;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Style.Burger;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Style.Roll;
import static com.noodlesandwich.rekord.Rekords.Wurst;
import static com.noodlesandwich.rekord.Transformers.defaultsTo;
import static com.noodlesandwich.rekord.matchers.RekordMatchers.hasKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public final class RekordTest {
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
        List<Cookie> cookies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cookies.add(new Cookie());
        }

        Rekord<Jar<Cookie>> cookieJar = Rekord.<Jar<Cookie>>create("cookie jar")
                .with(Jar.<Cookie>contents(), cookies);

        assertThat(cookieJar, is(RekordMatchers.<Jar<Cookie>>aRekordNamed("cookie jar")
                .with(Jar.<Cookie>contents(), tenCookies())));
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

    @Test public void
    a_Rekord_can_be_built_from_another_Rekord() {
        Rekord<Sandvich> cheeseSandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Cheese)
                .with(Brown, Sandvich.bread);

        Rekord<Sandvich> cheeseBurger = cheeseSandvich
                .with(White, Sandvich.bread)
                .with(Sandvich.style, Burger);

        assertThat(cheeseBurger, is(Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Cheese)
                .with(White, Sandvich.bread)
                .with(Sandvich.style, Burger)));
    }

    @Test public void
    after_building_a_Rekord_from_another_Rekord_the_original_does_not_mutate() {
        Rekord<Sandvich> sandvichBuilder = Rekord.of(Sandvich.class)
                .with(White, Sandvich.bread);

        Rekord<Sandvich> cheeseSandvich = sandvichBuilder.with(Sandvich.filling, Cheese);
        Rekord<Sandvich> hamSandvich = sandvichBuilder.with(Sandvich.filling, Ham);

        assertThat(cheeseSandvich, is(Rekord.of(Sandvich.class)
                .with(White, Sandvich.bread)
                .with(Sandvich.filling, Cheese)));

        assertThat(hamSandvich, is(Rekord.of(Sandvich.class)
                .with(White, Sandvich.bread)
                .with(Sandvich.filling, Ham)));

        assertThat(cheeseSandvich, is(not(equalTo(hamSandvich))));
    }

    @Test public void
    a_Rekord_can_tell_which_keys_are_being_used() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Jam)
                .with(Sandvich.bread, Brown);

        assertThat(sandvich, allOf(
                hasKey(Sandvich.filling),
                hasKey(Sandvich.bread),
                not(hasKey(Sandvich.style))));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    the_keys_of_the_properties_of_a_Rekord_can_be_retrieved() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Roll);

        assertThat(sandvich.keys(), Matchers.<Key<? super Sandvich, ?>>containsInAnyOrder(Sandvich.filling, Sandvich.style));
    }

    @Test public void
    a_Rekord_can_store_and_retrieve_values_using_a_transformed_key() {
        Key<Sandvich, Collection<Sandvich.Filling>> fillings = Sandvich.filling.that(yieldsThreeTimes(Sandvich.Filling.class));
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.bread, Brown)
                .with(fillings, Collections.singleton(Jam));

        assertThat(sandvich.get(fillings), contains(Jam, Jam, Jam));
    }

    @Test public void
    a_Rekord_can_store_values_using_a_key_and_retrieve_them_with_transformations() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Ham);

        assertThat(sandvich.get(Sandvich.filling.that(yieldsThreeTimes(Sandvich.Filling.class))),
                   contains(Ham, Ham, Ham));
    }

    @Test public void
    a_Rekord_can_store_values_using_a_transformed_key_and_retrieve_them_without_transformations() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.bread, Brown);

        assertThat(sandvich.get(Sandvich.bread.that(defaultsTo(White))),
                   is(Brown));
    }

    @Test public void
    two_Rekords_with_the_same_keys_and_values_are_equal() {
        Rekord<Bier> bier = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm());

        assertThat(bier, is(equalTo(Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm()))));
    }

    @Test public void
    two_Rekords_with_the_same_keys_but_different_values_are_not_equal() {
        Rekord<Bier> englishBeer = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm());

        Rekord<Bier> germanBier = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(1).l())
                .with(Bier.head, Measurement.of(4).cm());

        assertThat(englishBeer, is(not(equalTo(germanBier))));
    }

    @Test public void
    two_Rekords_with_different_keys_are_not_equal() {
        Rekord<Bier> cider = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml());

        Rekord<Bier> stout = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(4).cm());

        assertThat(cider, is(not(equalTo(stout))));
    }

    @Test public void
    a_Rekord_is_not_equal_to_another_type() {
        Rekord<Bier> bier = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(500).ml());

        assertThat(bier, is(not(equalTo((Object) new Bier() {
        }))));
    }

    @Test public void
    two_Rekords_with_the_same_properties_have_the_same_hash_code() {
        Rekord<Bier> bier = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm());

        Rekord<Bier> anotherBier = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(2).cm());

        assertThat(bier.hashCode(), is(equalTo(anotherBier.hashCode())));
    }

    @Test public void
    two_Rekords_with_different_properties_are_likely_to_have_a_different_hash_code() {
        Rekord<Bier> ale = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(1).cm());

        Rekord<Bier> pilsner = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(1).l())
                .with(Bier.head, Measurement.of(5).cm());

        assertThat(ale.hashCode(), is(not(equalTo(pilsner.hashCode()))));
    }

    @Test public void
    a_Rekord_is_serializable_as_a_String() {
        Rekord<Bier> delicious = Rekord.of(Bier.class)
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(3).cm());

        assertThat(delicious, hasToString(allOf(startsWith("Bier"), containsString("head=3cm"), containsString("volume=568ml"))));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Matcher<Collection<Cookie>> tenCookies() {
        return (Matcher<Collection<Cookie>>) (Matcher) hasSize(10);
    }

    private static <T> Transformer<T, Collection<T>> yieldsThreeTimes(@SuppressWarnings("unused") Class<T> type) {
        return new Transformer<T, Collection<T>>() {
            @Override public T transformInput(Collection<T> value) {
                Iterator<T> iterator = value.iterator();
                return iterator.hasNext() ? iterator.next() : null;
            }

            @Override public Collection<T> transformOutput(T value) {
                Collection<T> values = new ArrayList<>(3);
                values.add(value);
                values.add(value);
                values.add(value);
                return values;
            }
        };
    }
}
