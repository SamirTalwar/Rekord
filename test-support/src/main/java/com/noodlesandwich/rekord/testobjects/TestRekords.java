package com.noodlesandwich.rekord.testobjects;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.Rekords;
import com.noodlesandwich.rekord.buildables.Buildables;
import com.noodlesandwich.rekord.keys.CollectionKey;
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
        CollectionKey<Person, Rekord<Person>, Set<Rekord<Person>>> favouritePeople =
                CollectionKey.named("favourite people")
                        .of(RekordKey.named("favourite person").<Person, Person>builtFrom(Person.rekord))
                        .builtFrom(Buildables.<Rekord<Person>>hashSet());
        CollectionKey<Person, String, List<String>> pets =
                CollectionKey.named("pets")
                        .of(SimpleKey.<Person, String>named("pet"))
                        .builtFrom(Buildables.<String>arrayList());
        RekordKey<Person, Address> address = RekordKey.named("address").builtFrom(Address.rekord);
        RekordKey<Person, Company> company = RekordKey.named("company").builtFrom(Company.rekord);

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
        RekordKey<Company, Address> address = RekordKey.named("address").builtFrom(Address.rekord);

        Rekord<Company> rekord = Rekords.of(Company.class).accepting(name, address);
    }

    public interface Box {
        Key<Box, Boolean> fact = SimpleKey.named("fact");
        Key<Box, Integer> number = SimpleKey.named("number");
        Key<Box, Double> real = SimpleKey.named("real");
        Key<Box, String> text = SimpleKey.named("text");
        CollectionKey<Box, Object, Collection<Object>> stuff =
                CollectionKey.named("stuff")
                        .of(SimpleKey.<Box, Object>named("object"))
                        .<Collection<Object>>builtFrom(Buildables.hashSet());

        Rekord<Box> rekord = Rekords.of(Box.class).accepting(fact, number, real, text, stuff);
    }

    public interface LegoBag {
        CollectionKey<LegoBag, Set<Brick>, List<Set<Brick>>> sets =
                CollectionKey.named("lego sets")
                        .of(CollectionKey.named("bricks")
                                .of(SimpleKey.<LegoBag, Brick>named("brick"))
                                .builtFrom(Buildables.<Brick>hashSet()))
                        .builtFrom(Buildables.<Set<Brick>>arrayList());
        CollectionKey<LegoBag, Rekord<Person>, List<Rekord<Person>>> minifigs =
                CollectionKey.named("minifigs")
                        .of(RekordKey.named("minifig")
                                .<LegoBag, Person>builtFrom(Person.rekord))
                        .builtFrom(Buildables.<Rekord<Person>>arrayList());

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

        private static final Key<Jar<Contents>, List<Contents>> contents
                = CollectionKey.named("contents")
                        .of(SimpleKey.<Jar<Contents>, Contents>named("contents"))
                        .builtFrom(Buildables.<Contents>arrayList());

        @SuppressWarnings("unchecked")
        public static <T extends Jar.Contents> Key<Jar<T>, Collection<T>> contents() {
            return (Key<Jar<T>, Collection<T>>) (Key) contents;
        }

        public interface Contents { }

        public static final class Cookie implements Jar.Contents { }
    }
}
