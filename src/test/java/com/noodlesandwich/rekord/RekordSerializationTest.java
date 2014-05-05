package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.serialization.Serializer;
import com.noodlesandwich.rekord.testobjects.Measurement;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

import static com.noodlesandwich.rekord.serialization.Serializer.Accumulator;
import static com.noodlesandwich.rekord.serialization.Serializer.SerializedProperty;
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

        final Serializer<String, String> serializer = serializer();
        final Accumulator<String> accumulator = accumulator();
        final SerializedProperty<String> volume = property("volume");
        final SerializedProperty<String> head = property("head");

        context.checking(new Expectations() {{
            oneOf(serializer).start("Bier"); will(returnValue(accumulator));

            Sequence volumeSequence = context.sequence("volume");
            Sequence headSequence = context.sequence("head");
            oneOf(accumulator).single("volume", Measurement.of(500).ml()); will(returnValue(volume)); inSequence(volumeSequence);
            oneOf(accumulator).accumulate("volume", volume); inSequence(volumeSequence);
            oneOf(accumulator).single("head", Measurement.of(1).cm()); will(returnValue(head)); inSequence(headSequence);
            oneOf(accumulator).accumulate("head", head); inSequence(headSequence);
            oneOf(serializer).finish(accumulator); will(returnValue("result!")); inSequences(volumeSequence, headSequence);
        }});

        String result = bier.serialize(serializer);

        context.assertIsSatisfied();
        assertThat(result, is("result!"));
    }

    @Test public void
    serializes_transformed_keys() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.bread, White)
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Burger);

        final Serializer<String, String> serializer = serializer();
        final Accumulator<String> accumulator = accumulator();
        final SerializedProperty<String> bread = property("bread");
        final SerializedProperty<String> filling = property("filling");
        final SerializedProperty<String> style = property("style");

        context.checking(new Expectations() {{
            oneOf(serializer).start("Sandvich"); will(returnValue(accumulator));

            Sequence breadSequence = context.sequence("bread");
            Sequence fillingSequence = context.sequence("filling");
            Sequence styleSequence = context.sequence("style");
            oneOf(accumulator).single("bread", White); will(returnValue(bread)); inSequence(breadSequence);
            oneOf(accumulator).single("filling", Cheese); will(returnValue(filling)); inSequence(fillingSequence);
            oneOf(accumulator).single("style", Burger); will(returnValue(style)); inSequence(styleSequence);
            oneOf(accumulator).accumulate("bread", bread); inSequence(breadSequence);
            oneOf(accumulator).accumulate("filling", filling); inSequence(fillingSequence);
            oneOf(accumulator).accumulate("style", style); inSequence(styleSequence);
            oneOf(serializer).finish(accumulator); will(returnValue("result!")); inSequences(fillingSequence, breadSequence, styleSequence);
        }});

        String result = sandvich.serialize(serializer);

        context.assertIsSatisfied();
        assertThat(result, is("result!"));
    }

    @Test public void
    a_serializer_will_accept_keys_of_the_supertype() {
        Rekord<Bratwurst> bratwurst = Bratwurst.rekord
                .with(Wurst.curvature, 0.7)
                .with(Bratwurst.style, Chopped);

        final Serializer<Integer, Integer> serializer = serializer();
        final Accumulator<Integer> accumulator = accumulator();
        final SerializedProperty<Integer> curvature = property("curvature");
        final SerializedProperty<Integer> style = property("style");

        context.checking(new Expectations() {{
            oneOf(serializer).start("Bratwurst"); will(returnValue(accumulator));

            Sequence curvatureSequence = context.sequence("curvature");
            Sequence styleSequence = context.sequence("style");
            oneOf(accumulator).single("curvature", 0.7); will(returnValue(curvature)); inSequence(curvatureSequence);
            oneOf(accumulator).single("style", Chopped); will(returnValue(style)); inSequence(styleSequence);
            oneOf(accumulator).accumulate("curvature", curvature); inSequence(curvatureSequence);
            oneOf(accumulator).accumulate("style", style); inSequence(styleSequence);
            oneOf(serializer).finish(accumulator); will(returnValue(99)); inSequences(curvatureSequence, styleSequence);
        }});

        int result = bratwurst.serialize(serializer);

        context.assertIsSatisfied();
        assertThat(result, is(99));
    }

    @Test public void
    serializes_nested_rekords() {
        final Rekord<Person> person = Person.rekord
                .with(Person.firstName, "Sherlock")
                .with(Person.lastName, "Holmes")
                .with(Person.address, Address.rekord
                        .with(Address.houseNumber, 221)
                        .with(Address.street, "Baker Street"));

        final Serializer<String, String> serializer = serializer();
        final Accumulator<String> personAccumulator = accumulator("person accumulator");
        final Accumulator<String> addressAccumulator = accumulator("address accumulator");
        final SerializedProperty<String> firstName = property("first name");
        final SerializedProperty<String> lastName = property("last name");
        final SerializedProperty<String> houseNumber = property("house number");
        final SerializedProperty<String> street = property("street");

        context.checking(new Expectations() {{
            oneOf(serializer).start("Person"); will(returnValue(personAccumulator));
            oneOf(personAccumulator).single("first name", "Sherlock"); will(returnValue(firstName));
            oneOf(personAccumulator).single("last name", "Holmes"); will(returnValue(lastName));
            oneOf(personAccumulator).accumulate("first name", firstName);
            oneOf(personAccumulator).accumulate("last name", lastName);

            oneOf(personAccumulator).nest("Address"); will(returnValue(addressAccumulator));
            oneOf(addressAccumulator).single("house number", 221); will(returnValue(houseNumber));
            oneOf(addressAccumulator).single("street", "Baker Street"); will(returnValue(street));
            oneOf(addressAccumulator).accumulate("house number", houseNumber);
            oneOf(addressAccumulator).accumulate("street", street);
            oneOf(personAccumulator).accumulate("address", addressAccumulator);

            oneOf(serializer).finish(personAccumulator); will(returnValue("Sherlock Holmes, 221 Baker Street"));
        }});

        String result = person.serialize(serializer);

        context.assertIsSatisfied();
        assertThat(result, is("Sherlock Holmes, 221 Baker Street"));
    }

    @Test public void
    serializes_transformed_rekord_keys() {
        final Rekord<Restaurant> restaurant = Restaurant.rekord
                .with(Restaurant.name, "McAwful's")
                .with(Restaurant.mealName, "White Cheese Burger");

        final Serializer<String, String> serializer = serializer();
        final Accumulator<String> restaurantAccumulator = accumulator("restaurant");
        final Accumulator<String> mealAccumulator = accumulator("meal");
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

            oneOf(serializer).start("Restaurant"); will(returnValue(restaurantAccumulator));

            oneOf(restaurantAccumulator).single("name", "McAwful's"); will(returnValue(name)); inSequence(nameSequence);
            oneOf(restaurantAccumulator).accumulate("name", name); inSequence(nameSequence);

            oneOf(restaurantAccumulator).nest("Sandvich"); will(returnValue(mealAccumulator)); inSequence(mealSequence);
            oneOf(mealAccumulator).single("bread", White); will(returnValue(bread)); inSequence(breadSequence);
            oneOf(mealAccumulator).single("filling", Cheese); will(returnValue(filling)); inSequence(fillingSequence);
            oneOf(mealAccumulator).single("style", Burger); will(returnValue(style)); inSequence(styleSequence);
            oneOf(mealAccumulator).accumulate("bread", bread); inSequence(breadSequence);
            oneOf(mealAccumulator).accumulate("filling", filling); inSequence(fillingSequence);
            oneOf(mealAccumulator).accumulate("style", style); inSequence(styleSequence);
            oneOf(restaurantAccumulator).accumulate("meal", mealAccumulator); inSequences(mealSequence, fillingSequence, breadSequence, styleSequence);

            oneOf(serializer).finish(restaurantAccumulator); will(returnValue("result!")); inSequences(nameSequence, mealSequence);
        }});

        String result = restaurant.serialize(serializer);

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
    private <A, R> Serializer<A, R> serializer() {
        return context.mock(Serializer.class);
    }

    @SuppressWarnings("unchecked")
    private <A> Accumulator<A> accumulator() {
        return context.mock(Accumulator.class);
    }

    @SuppressWarnings("unchecked")
    private <A> Accumulator<A> accumulator(String name) {
        return context.mock(Accumulator.class, name);
    }

    @SuppressWarnings("unchecked")
    private <A> SerializedProperty<A> property(String name) {
        return context.mock(SerializedProperty.class, name);
    }
}
