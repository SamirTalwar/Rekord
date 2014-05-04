package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.serialization.Serializer;
import com.noodlesandwich.rekord.testobjects.Measurement;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

import static com.noodlesandwich.rekord.serialization.Serializer.Accumulator;
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

        final Accumulator<String, String> accumulator = accumulator();
        final Serializer<String, String> serializer = serializer();

        context.checking(new Expectations() {{
            oneOf(serializer).nest("Bier"); will(returnValue(accumulator));

            Sequence volume = context.sequence("volume");
            Sequence head = context.sequence("head");
            oneOf(accumulator).accumulate("volume", Measurement.of(500).ml()); inSequence(volume);
            oneOf(accumulator).accumulate("head", Measurement.of(1).cm()); inSequence(head);
            oneOf(accumulator).finish(); will(returnValue("result!")); inSequences(volume, head);
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

        final Accumulator<String, String> accumulator = accumulator();
        final Serializer<String, String> serializer = serializer();

        context.checking(new Expectations() {{
            oneOf(serializer).nest("Sandvich"); will(returnValue(accumulator));

            Sequence bread = context.sequence("bread");
            Sequence filling = context.sequence("filling");
            Sequence style = context.sequence("style");
            oneOf(accumulator).accumulate("bread", White); inSequence(bread);
            oneOf(accumulator).accumulate("filling", Cheese); inSequence(filling);
            oneOf(accumulator).accumulate("style", Burger); inSequence(bread);
            oneOf(accumulator).finish(); will(returnValue("result!")); inSequences(filling, bread, style);
        }});

        String result = sandvich.serialize(serializer);

        context.assertIsSatisfied();
        assertThat(result, is("result!"));
    }

    @Test public void
    serializes_transformed_rekord_keys() {
        final Rekord<Restaurant> restaurant = Restaurant.rekord
                .with(Restaurant.name, "McAwful's")
                .with(Restaurant.mealName, "White Cheese Burger");

        final Accumulator<String, String> restaurantAccumulator = accumulator("restaurant");
        final Accumulator<String, String> mealAccumulator = accumulator("meal");
        final Serializer<String, String> serializer = serializer();

        context.checking(new Expectations() {{
            oneOf(serializer).nest("Restaurant"); will(returnValue(restaurantAccumulator));

            oneOf(restaurantAccumulator).accumulate("name", "McAwful's");

            oneOf(restaurantAccumulator).nest("Sandvich"); will(returnValue(mealAccumulator));
            Sequence bread = context.sequence("bread");
            Sequence filling = context.sequence("filling");
            Sequence style = context.sequence("style");
            oneOf(mealAccumulator).accumulate("bread", White); inSequence(bread);
            oneOf(mealAccumulator).accumulate("filling", Cheese); inSequence(filling);
            oneOf(mealAccumulator).accumulate("style", Burger); inSequence(bread);
            oneOf(restaurantAccumulator).accumulateNested("meal", mealAccumulator); inSequences(filling, bread, style);

            oneOf(restaurantAccumulator).finish(); will(returnValue("result!"));
        }});

        String result = restaurant.serialize(serializer);

        context.assertIsSatisfied();
        assertThat(result, is("result!"));
    }

    @Test public void
    a_serializer_will_accept_keys_of_the_supertype() {
        Rekord<Bratwurst> bratwurst = Bratwurst.rekord
                .with(Wurst.curvature, 0.7)
                .with(Bratwurst.style, Chopped);

        final Accumulator<Integer, Integer> accumulator = accumulator();
        final Serializer<Integer, Integer> serializer = serializer();

        context.checking(new Expectations() {{
            oneOf(serializer).nest("Bratwurst"); will(returnValue(accumulator));

            Sequence curvature = context.sequence("curvature");
            Sequence style = context.sequence("style");
            oneOf(accumulator).accumulate("curvature", 0.7); inSequence(curvature);
            oneOf(accumulator).accumulate("style", Chopped); inSequence(style);
            oneOf(accumulator).finish(); will(returnValue(99)); inSequences(curvature, style);
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

        final Accumulator<String, String> personAccumulator = accumulator("person accumulator");
        final Accumulator<String, String> addressAccumulator = accumulator("address accumulator");
        final Serializer<String, String> serializer = serializer();

        context.checking(new Expectations() {{
            oneOf(serializer).nest("Person"); will(returnValue(personAccumulator));
            oneOf(personAccumulator).accumulate("first name", "Sherlock");
            oneOf(personAccumulator).accumulate("last name", "Holmes");

            oneOf(personAccumulator).nest("Address"); will(returnValue(addressAccumulator));
            oneOf(addressAccumulator).accumulate("house number", 221);
            oneOf(addressAccumulator).accumulate("street", "Baker Street");
            oneOf(personAccumulator).accumulateNested("address", addressAccumulator);

            oneOf(personAccumulator).finish(); will(returnValue("Sherlock Holmes, 221 Baker Street"));
        }});

        String result = person.serialize(serializer);

        context.assertIsSatisfied();
        assertThat(result, is("Sherlock Holmes, 221 Baker Street"));
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
    private <A, R> Accumulator<A, R> accumulator() {
        return context.mock(Accumulator.class);
    }

    @SuppressWarnings("unchecked")
    private <A, R> Accumulator<A, R> accumulator(String name) {
        return context.mock(Accumulator.class, name);
    }
}
