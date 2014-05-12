package com.noodlesandwich.rekord.testobjects;

import java.util.Collection;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.IterableKey;
import com.noodlesandwich.rekord.keys.RekordKey;
import com.noodlesandwich.rekord.keys.SimpleKey;

import static com.noodlesandwich.rekord.transformers.Transformers.defaultsTo;

public final class Rekords {
    private Rekords() { }

    public static interface Sandvich {
        Key<Sandvich, Bread> bread = SimpleKey.named("bread");
        Key<Sandvich, Filling> filling = SimpleKey.named("filling");
        Key<Sandvich, Style> style = SimpleKey.<Sandvich, Style>named("style").that(defaultsTo(Style.Flat));

        Rekord<Sandvich> rekord = Rekord.of(Sandvich.class).accepting(filling, bread, style);

        public static enum Bread {
            Brown,
            White
        }

        public static enum Filling {
            Cheese,
            Ham,
            Jam,
            Lettuce
        }

        public static enum Style {
            Flat,
            Burger,
            Roll
        }
    }

    public static interface Wurst {
        Key<Wurst, Double> curvature = SimpleKey.named("curvature");

        Rekord<Wurst> rekord = Rekord.of(Wurst.class).accepting(curvature);
    }

    public static interface Bratwurst extends Wurst {
        Key<Bratwurst, Style> style = SimpleKey.named("style");

        Rekord<Bratwurst> rekord = Rekord.of(Bratwurst.class).accepting(Wurst.curvature, style);

        public static enum Style {
            Chopped,
            Whole
        }
    }

    public static interface Bier {
        Key<Bier, Measurement.Volume> volume = SimpleKey.named("volume");
        Key<Bier, Measurement.Length> head = SimpleKey.named("head");

        Rekord<Bier> rekord = Rekord.of(Bier.class).accepting(volume, head);
    }

    public static interface Person {
        Key<Person, String> firstName = SimpleKey.named("first name");
        Key<Person, String> lastName = SimpleKey.named("last name");
        Key<Person, Integer> age = SimpleKey.named("age");
        Key<Person, Iterable<FixedRekord<Person>>> favouritePeople = IterableKey.named("favourite people").of(RekordKey.<Person, Person>named("favourite person"));
        Key<Person, Iterable<String>> pets = IterableKey.named("pets").of(SimpleKey.<Person, String>named("pet"));
        Key<Person, FixedRekord<Address>> address = RekordKey.named("address");

        Rekord<Person> rekord = Rekord.of(Person.class).accepting(firstName, lastName, age, favouritePeople, pets, address);
    }

    public static interface Address {
        Key<Address, Integer> houseNumber = SimpleKey.named("house number");
        Key<Address, String> street = SimpleKey.named("street");
        Key<Address, String> city = SimpleKey.named("city");
        Key<Address, String> postalCode = SimpleKey.named("postal code");

        Rekord<Address> rekord = Rekord.of(Address.class).accepting(houseNumber, street, city, postalCode);
    }

    public static interface Box {
        Key<Box, Integer> number = SimpleKey.named("number");
    }

    public static interface LegoBag {
        Key<LegoBag, Collection<Collection<Brick>>> sets = IterableKey.named("lego sets").of(IterableKey.named("bricks").<LegoBag, Brick, Collection<Brick>>of(SimpleKey.<LegoBag, Brick>named("brick")));

        Rekord<LegoBag> rekord = Rekord.of(LegoBag.class).accepting(sets);

        public static enum Brick {
            Red,
            Green,
            Blue
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public static final class Jar<T extends Jar.Contents> {
        @SuppressWarnings("unchecked")
        public static Rekord<Jar<Cookie>> ofCookies() {
            return Rekord.<Jar<Cookie>>create("Cookie Jar").accepting(Jar.<Cookie>contents());
        }

        private static final Key<Jar<Contents>, Iterable<Contents>> contents
                = IterableKey.named("contents").of(SimpleKey.<Jar<Contents>, Contents>named("contents"));

        @SuppressWarnings("unchecked")
        public static <T extends Jar.Contents> Key<Jar<T>, Collection<T>> contents() {
            return (Key<Jar<T>, Collection<T>>) (Key) contents;
        }

        public static interface Contents { }

        public static final class Cookie implements Jar.Contents { }
    }
}
