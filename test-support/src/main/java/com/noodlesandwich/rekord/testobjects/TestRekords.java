package com.noodlesandwich.rekord.testobjects;

import java.util.Collection;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.Rekords;
import com.noodlesandwich.rekord.keys.BuildableKey;
import com.noodlesandwich.rekord.keys.IterableKey;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.RekordKey;
import com.noodlesandwich.rekord.keys.SimpleKey;
import com.noodlesandwich.rekord.test.Measurement;

public final class TestRekords {
    private TestRekords() { }

    public interface Sandvich {
        Key<Sandvich, Bread> bread = SimpleKey.named("bread");
        Key<Sandvich, Filling> filling = SimpleKey.named("filling");
        Key<Sandvich, Style> style = SimpleKey.named("style");

        Rekord<Sandvich> rekord = Rekords.of(Sandvich.class).accepting(filling, bread, style);

        enum Bread {
            Brown,
            White
        }

        enum Filling {
            Cheese,
            Ham,
            Jam,
            Lettuce
        }

        enum Style {
            Flat,
            Burger,
            Roll
        }
    }

    public interface Wurst {
        Key<Wurst, Double> curvature = SimpleKey.named("curvature");
        Key<Wurst, Color> color = SimpleKey.named("color");

        Rekord<Wurst> rekord = Rekords.of(Wurst.class).accepting(curvature);

        enum Color {
            White,
            Brown,
            Red
        }
    }

    public interface Bier {
        Key<Bier, Measurement.Volume> volume = SimpleKey.named("volume");
        Key<Bier, Measurement.Length> head = SimpleKey.named("head");

        Rekord<Bier> rekord = Rekords.of(Bier.class).accepting(volume, head);
    }

    public interface Person {
        Key<Person, String> firstName = SimpleKey.named("first name");
        Key<Person, String> lastName = SimpleKey.named("last name");
        Key<Person, Integer> age = SimpleKey.named("age");
        Key<Person, Iterable<Rekord<Person>>> favouritePeople = IterableKey.named("favourite people").of(RekordKey.named("favourite person").<Person, Person>builtFrom(Person.rekord));
        Key<Person, Iterable<String>> pets = IterableKey.named("pets").of(SimpleKey.<Person, String>named("pet"));
        BuildableKey<Person, Rekord<Address>> address = RekordKey.named("address").builtFrom(Address.rekord);
        BuildableKey<Person, Rekord<Company>> company = RekordKey.named("company").builtFrom(Company.rekord);

        Rekord<Person> rekord = Rekords.of(Person.class).accepting(firstName, lastName, age, favouritePeople, pets, address, company);
    }

    public interface Address {
        Key<Address, Integer> houseNumber = SimpleKey.named("house number");
        Key<Address, String> street = SimpleKey.named("street");
        Key<Address, String> city = SimpleKey.named("city");
        Key<Address, String> postalCode = SimpleKey.named("postal code");
        Key<Address, Country> country = SimpleKey.named("country");

        Rekord<Address> rekord = Rekords.of(Address.class).accepting(houseNumber, street, city, postalCode, country);
    }

    public interface Company {
        Key<Company, String> name = SimpleKey.named("name");
        BuildableKey<Company, Rekord<Address>> address = RekordKey.named("address").builtFrom(Address.rekord);

        Rekord<Company> rekord = Rekords.of(Company.class).accepting(name, address);
    }

    public interface Box {
        Key<Box, Boolean> fact = SimpleKey.named("fact");
        Key<Box, Integer> number = SimpleKey.named("number");
        Key<Box, Double> real = SimpleKey.named("real");
        Key<Box, String> text = SimpleKey.named("text");
        Key<Box, Collection<Object>> stuff = IterableKey.named("stuff").of(SimpleKey.<Box, Object>named("object"));

        Rekord<Box> rekord = Rekords.of(Box.class).accepting(fact, number, real, text, stuff);
    }

    public interface LegoBag {
        Key<LegoBag, Collection<Collection<Brick>>> sets = IterableKey.named("lego sets").of(IterableKey.named("bricks").<LegoBag, Brick, Collection<Brick>>of(SimpleKey.<LegoBag, Brick>named("brick")));
        Key<LegoBag, Iterable<Rekord<Person>>> minifigs = IterableKey.named("minifigs").of(RekordKey.named("minifig").<LegoBag, Person>builtFrom(Person.rekord));

        Rekord<LegoBag> rekord = Rekords.of(LegoBag.class).accepting(sets, minifigs);

        enum Brick {
            Red,
            Green,
            Blue
        }
    }

    public enum Country {
        Australia,
        Austria,
        Canada,
        Germany,
        NewZealand,
        Switzerland,
        UnitedKingdom,
        UnitedStates
    }

    @SuppressWarnings("UnusedDeclaration")
    public static final class Jar<T extends Jar.Contents> {
        @SuppressWarnings("unchecked")
        public static Rekord<Jar<Cookie>> ofCookies() {
            return Rekords.<Jar<Cookie>>create("Cookie Jar").accepting(Jar.<Cookie>contents());
        }

        private static final Key<Jar<Contents>, Iterable<Contents>> contents
                = IterableKey.named("contents").of(SimpleKey.<Jar<Contents>, Contents>named("contents"));

        @SuppressWarnings("unchecked")
        public static <T extends Jar.Contents> Key<Jar<T>, Collection<T>> contents() {
            return (Key<Jar<T>, Collection<T>>) (Key) contents;
        }

        public interface Contents { }

        public static final class Cookie implements Jar.Contents { }
    }
}
