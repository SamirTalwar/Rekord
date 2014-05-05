package com.noodlesandwich.rekord;

import com.google.common.collect.ImmutableList;
import com.noodlesandwich.rekord.serialization.RekordSerializer;
import com.noodlesandwich.rekord.testobjects.Measurement;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

import static com.noodlesandwich.rekord.serialization.RekordSerializer.SerializedProperty;
import static com.noodlesandwich.rekord.serialization.RekordSerializer.Serializer;
import static com.noodlesandwich.rekord.testobjects.Rekords.Address;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bier;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst.Style.Chopped;
import static com.noodlesandwich.rekord.testobjects.Rekords.Person;
import static com.noodlesandwich.rekord.testobjects.Rekords.Restaurant;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Burger;
import static com.noodlesandwich.rekord.testobjects.Rekords.Wurst;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public final class RekordSerializationTest {
    private final Mockery context = new Mockery();

    @Test public void
    a_Rekord_can_be_serialized() {
        Rekord<Bier> bier = Bier.rekord
                .with(Bier.volume, Measurement.of(500).ml())
                .with(Bier.head, Measurement.of(1).cm());

        final RekordSerializer<String, String> bierSerializer = rekordSerializer();
        final Serializer<String> serializer = serializer();
        final SerializedProperty<String> volume = property("volume");
        final SerializedProperty<String> head = property("head");

        context.checking(new Expectations() {{
            oneOf(bierSerializer).start("Bier"); will(returnValue(serializer));

            Sequence volumeSequence = context.sequence("volume");
            Sequence headSequence = context.sequence("head");
            oneOf(serializer).single("volume", Measurement.of(500).ml()); will(returnValue(volume)); inSequence(volumeSequence);
            oneOf(serializer).accumulate("volume", volume); inSequence(volumeSequence);
            oneOf(serializer).single("head", Measurement.of(1).cm()); will(returnValue(head)); inSequence(headSequence);
            oneOf(serializer).accumulate("head", head); inSequence(headSequence);
            oneOf(bierSerializer).finish(serializer); will(returnValue("result!")); inSequences(volumeSequence, headSequence);
        }});

        String result = bier.serialize(bierSerializer);

        context.assertIsSatisfied();
        assertThat(result, is("result!"));
    }

    @Test public void
    serializes_transformed_keys() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.bread, White)
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Burger);

        final RekordSerializer<String, String> sandvichSerializer = rekordSerializer();
        final Serializer<String> serializer = serializer();
        final SerializedProperty<String> bread = property("bread");
        final SerializedProperty<String> filling = property("filling");
        final SerializedProperty<String> style = property("style");

        context.checking(new Expectations() {{
            oneOf(sandvichSerializer).start("Sandvich"); will(returnValue(serializer));

            Sequence breadSequence = context.sequence("bread");
            Sequence fillingSequence = context.sequence("filling");
            Sequence styleSequence = context.sequence("style");
            oneOf(serializer).single("bread", White); will(returnValue(bread)); inSequence(breadSequence);
            oneOf(serializer).single("filling", Cheese); will(returnValue(filling)); inSequence(fillingSequence);
            oneOf(serializer).single("style", Burger); will(returnValue(style)); inSequence(styleSequence);
            oneOf(serializer).accumulate("bread", bread); inSequence(breadSequence);
            oneOf(serializer).accumulate("filling", filling); inSequence(fillingSequence);
            oneOf(serializer).accumulate("style", style); inSequence(styleSequence);
            oneOf(sandvichSerializer).finish(serializer); will(returnValue("result!")); inSequences(fillingSequence, breadSequence, styleSequence);
        }});

        String result = sandvich.serialize(sandvichSerializer);

        context.assertIsSatisfied();
        assertThat(result, is("result!"));
    }

    @Test public void
    a_serializer_will_accept_keys_of_the_supertype() {
        Rekord<Bratwurst> bratwurst = Bratwurst.rekord
                .with(Wurst.curvature, 0.7)
                .with(Bratwurst.style, Chopped);

        final RekordSerializer<Integer, Integer> bratwurstSerializer = rekordSerializer();
        final Serializer<Integer> serializer = serializer();
        final SerializedProperty<Integer> curvature = property("curvature");
        final SerializedProperty<Integer> style = property("style");

        context.checking(new Expectations() {{
            oneOf(bratwurstSerializer).start("Bratwurst"); will(returnValue(serializer));

            Sequence curvatureSequence = context.sequence("curvature");
            Sequence styleSequence = context.sequence("style");
            oneOf(serializer).single("curvature", 0.7); will(returnValue(curvature)); inSequence(curvatureSequence);
            oneOf(serializer).single("style", Chopped); will(returnValue(style)); inSequence(styleSequence);
            oneOf(serializer).accumulate("curvature", curvature); inSequence(curvatureSequence);
            oneOf(serializer).accumulate("style", style); inSequence(styleSequence);
            oneOf(bratwurstSerializer).finish(serializer); will(returnValue(99)); inSequences(curvatureSequence, styleSequence);
        }});

        int result = bratwurst.serialize(bratwurstSerializer);

        context.assertIsSatisfied();
        assertThat(result, is(99));
    }

    @Test public void
    serializes_nested_rekords() {
        final Rekord<Person> holmes = Person.rekord
                .with(Person.firstName, "Sherlock")
                .with(Person.lastName, "Holmes")
                .with(Person.address, Address.rekord
                        .with(Address.houseNumber, 221)
                        .with(Address.street, "Baker Street"));

        final RekordSerializer<String, String> rekordSerializer = rekordSerializer();
        final Serializer<String> personSerializer = serializer("person serializer");
        final Serializer<String> addressSerializer = serializer("address serializer");
        final SerializedProperty<String> firstName = property("first name");
        final SerializedProperty<String> lastName = property("last name");
        final SerializedProperty<String> houseNumber = property("house number");
        final SerializedProperty<String> street = property("street");

        context.checking(new Expectations() {{
            oneOf(rekordSerializer).start("Person"); will(returnValue(personSerializer));
            oneOf(personSerializer).single("first name", "Sherlock"); will(returnValue(firstName));
            oneOf(personSerializer).single("last name", "Holmes"); will(returnValue(lastName));
            oneOf(personSerializer).accumulate("first name", firstName);
            oneOf(personSerializer).accumulate("last name", lastName);

            oneOf(personSerializer).map("Address"); will(returnValue(addressSerializer));
            oneOf(addressSerializer).single("house number", 221); will(returnValue(houseNumber));
            oneOf(addressSerializer).single("street", "Baker Street"); will(returnValue(street));
            oneOf(addressSerializer).accumulate("house number", houseNumber);
            oneOf(addressSerializer).accumulate("street", street);
            oneOf(personSerializer).accumulate("address", addressSerializer);

            oneOf(rekordSerializer).finish(personSerializer); will(returnValue("Sherlock Holmes, 221 Baker Street"));
        }});

        String result = holmes.serialize(rekordSerializer);

        context.assertIsSatisfied();
        assertThat(result, is("Sherlock Holmes, 221 Baker Street"));
    }

    @Test public void
    serializes_collections_of_rekords() {
        final Rekord<Person> watson = Person.rekord
                .with(Person.firstName, "John")
                .with(Person.lastName, "Watson");
        final Rekord<Person> holmes = Person.rekord
                .with(Person.firstName, "Sherlock")
                .with(Person.lastName, "Holmes")
                .with(Person.favouritePeople, ImmutableList.of(watson));

        final RekordSerializer<String, String> rekordSerializer = rekordSerializer();
        final Serializer<String> holmesSerializer = serializer("Holmes serializer");
        final Serializer<String> favouritePeopleSerializer = serializer("Favourite People serializer");
        final Serializer<String> watsonSerializer = serializer("Watson serializer");
        final SerializedProperty<String> holmesFirstName = property("Holmes' first name");
        final SerializedProperty<String> holmesLastName = property("Holmes' last name");
        final SerializedProperty<String> watsonFirstName = property("Watson's first name");
        final SerializedProperty<String> watsonLastName = property("Watson's last name");

        context.checking(new Expectations() {{
            oneOf(rekordSerializer).start("Person"); will(returnValue(holmesSerializer));
            oneOf(holmesSerializer).single("first name", "Sherlock"); will(returnValue(holmesFirstName));
            oneOf(holmesSerializer).single("last name", "Holmes"); will(returnValue(holmesLastName));
            oneOf(holmesSerializer).accumulate("first name", holmesFirstName);
            oneOf(holmesSerializer).accumulate("last name", holmesLastName);

            oneOf(holmesSerializer).collection("favourite people"); will(returnValue(favouritePeopleSerializer));
            oneOf(favouritePeopleSerializer).map("Person"); will(returnValue(watsonSerializer));
            oneOf(watsonSerializer).single("first name", "John"); will(returnValue(watsonFirstName));
            oneOf(watsonSerializer).single("last name", "Watson"); will(returnValue(watsonLastName));
            oneOf(watsonSerializer).accumulate("first name", watsonFirstName);
            oneOf(watsonSerializer).accumulate("last name", watsonLastName);
            oneOf(favouritePeopleSerializer).accumulate("favourite person", watsonSerializer);
            oneOf(holmesSerializer).accumulate("favourite people", favouritePeopleSerializer);

            oneOf(rekordSerializer).finish(holmesSerializer); will(returnValue("Sherlock Holmes, 221 Baker Street"));
        }});

        String result = holmes.serialize(rekordSerializer);

        context.assertIsSatisfied();
        assertThat(result, is("Sherlock Holmes, 221 Baker Street"));
    }

    @Test public void
    serializes_transformed_rekord_keys() {
        final Rekord<Restaurant> restaurant = Restaurant.rekord
                .with(Restaurant.name, "McAwful's")
                .with(Restaurant.mealName, "White Cheese Burger");

        final RekordSerializer<String, String> rekordSerializer = rekordSerializer();
        final Serializer<String> restaurantSerializer = serializer("restaurant");
        final Serializer<String> mealSerializer = serializer("meal");
        final SerializedProperty<String> name = property("name");
        final SerializedProperty<String> bread = property("bread");
        final SerializedProperty<String> filling = property("filling");
        final SerializedProperty<String> style = property("style");

        context.checking(new Expectations() {{
            Sequence nameSequence = context.sequence("name");
            Sequence mealSequence = context.sequence("meal");
            Sequence breadSequence = context.sequence("bread");
            Sequence fillingSequence = context.sequence("filling");
            Sequence styleSequence = context.sequence("style");

            oneOf(rekordSerializer).start("Restaurant"); will(returnValue(restaurantSerializer));

            oneOf(restaurantSerializer).single("name", "McAwful's"); will(returnValue(name)); inSequence(nameSequence);
            oneOf(restaurantSerializer).accumulate("name", name); inSequence(nameSequence);

            oneOf(restaurantSerializer).map("Sandvich"); will(returnValue(mealSerializer)); inSequence(mealSequence);
            oneOf(mealSerializer).single("bread", White); will(returnValue(bread)); inSequence(breadSequence);
            oneOf(mealSerializer).single("filling", Cheese); will(returnValue(filling)); inSequence(fillingSequence);
            oneOf(mealSerializer).single("style", Burger); will(returnValue(style)); inSequence(styleSequence);
            oneOf(mealSerializer).accumulate("bread", bread); inSequence(breadSequence);
            oneOf(mealSerializer).accumulate("filling", filling); inSequence(fillingSequence);
            oneOf(mealSerializer).accumulate("style", style); inSequence(styleSequence);
            oneOf(restaurantSerializer).accumulate("meal", mealSerializer); inSequences(mealSequence, fillingSequence, breadSequence, styleSequence);

            oneOf(rekordSerializer).finish(restaurantSerializer); will(returnValue("result!")); inSequences(nameSequence, mealSequence);
        }});

        String result = restaurant.serialize(rekordSerializer);

        context.assertIsSatisfied();
        assertThat(result, is("result!"));
    }

    @Test public void
    a_Rekord_is_serializable_as_a_String() {
        Rekord<Bier> delicious = Bier.rekord
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(3).cm());

        assertThat(delicious, hasToString(allOf(startsWith("Bier"), containsString("head: 3cm"), containsString("volume: 568ml"))));
    }

    @SuppressWarnings("unchecked")
    private <A, R> RekordSerializer<A, R> rekordSerializer() {
        return context.mock(RekordSerializer.class);
    }

    @SuppressWarnings("unchecked")
    private <A> Serializer<A> serializer() {
        return context.mock(Serializer.class);
    }

    @SuppressWarnings("unchecked")
    private <A> Serializer<A> serializer(String name) {
        return context.mock(Serializer.class, name);
    }

    @SuppressWarnings("unchecked")
    private <A> SerializedProperty<A> property(String name) {
        return context.mock(SerializedProperty.class, name);
    }
}
