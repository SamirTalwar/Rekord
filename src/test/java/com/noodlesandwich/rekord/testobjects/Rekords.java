package com.noodlesandwich.rekord.testobjects;

import java.util.Collection;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Rekord;

import static com.noodlesandwich.rekord.extra.Validation.validatesItsInput;
import static com.noodlesandwich.rekord.transformers.Transformers.defaultsTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public final class Rekords {
    private Rekords() { }

    public static interface Sandvich {
        Key<Sandvich, Filling> filling = Key.named("filling");
        Key<Sandvich, Bread> bread = Key.named("bread");
        Key<Sandvich, Style> style = Key.<Sandvich, Style>named("style").that(defaultsTo(Style.Flat));

        Rekord<Sandvich> rekord = Rekord.of(Sandvich.class).accepting(filling, bread, style);

        public static enum Filling {
            Cheese,
            Ham,
            Jam,
            Lettuce
        }

        public static enum Bread {
            Brown,
            White
        }

        public static enum Style {
            Flat,
            Burger,
            Roll
        }
    }

    public static interface Wurst {
        Key<Wurst, Double> curvature = Key.named("curvature");

        Rekord<Wurst> rekord = Rekord.of(Wurst.class).accepting(curvature);
    }

    public static interface Bratwurst extends Wurst {
        Key<Bratwurst, Style> style = Key.named("style");

        Rekord<Bratwurst> rekord = Rekord.of(Bratwurst.class).accepting(Wurst.curvature, style);

        public static enum Style {
            Chopped,
            Whole
        }
    }

    public static interface Bier {
        Key<Bier, Measurement.Volume> volume = Key.named("volume");
        Key<Bier, Measurement.Length> head = Key.named("head");

        Rekord<Bier> rekord = Rekord.of(Bier.class).accepting(volume, head);
    }

    public static interface Person {
        Key<Person, String> firstName = Key.named("first name");
        Key<Person, String> lastName = Key.named("last name");
        Key<Person, Integer> age = Key.named("age");
        Key<Person, Rekord<Person>> favouritePerson = Key.named("favourite person");
        Key<Person, Rekord<Address>> address = Key.named("address");

        Rekord<Person> rekord = Rekord.of(Person.class).accepting(firstName, lastName, age, favouritePerson, address);
    }

    public static interface Address {
        Key<Address, Integer> houseNumber = Key.named("house number");
        Key<Address, String> street = Key.named("street");
        Key<Address, String> city = Key.named("city");
        Key<Address, String> postalCode = Key.named("postal code");

        Rekord<Address> rekord = Rekord.of(Address.class).accepting(houseNumber, street, city, postalCode);
    }

    public static interface Box {
        Key<Box, Integer> anyNumber = Key.named("any number");
        Key<Box, Integer> lessThanTen = anyNumber.that(validatesItsInput(is(lessThan(10))));

        Rekord<Box> rekord = Rekord.of(Box.class).accepting(anyNumber, lessThanTen);
    }

    @SuppressWarnings("UnusedDeclaration")
    public static final class Jar<T extends Jar.Contents> {
        @SuppressWarnings("unchecked")
        public static Rekord<Jar<Cookie>> ofCookies() {
            return Rekord.<Jar<Cookie>>create("Cookie Jar").accepting(contents);
        }

        private static final Key contents = Key.named("contents");
        @SuppressWarnings("unchecked")
        public static <T extends Jar.Contents> Key<Jar<T>, Collection<T>> contents() {
            return contents;
        }

        public static interface Contents { }

        public static final class Cookie implements Jar.Contents { }
    }
}
