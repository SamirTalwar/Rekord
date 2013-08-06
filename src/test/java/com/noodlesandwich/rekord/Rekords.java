package com.noodlesandwich.rekord;

import java.util.Collection;

import static com.noodlesandwich.rekord.Transformers.defaultsTo;

public final class Rekords {
    private Rekords() { }

    public static interface Sandvich extends RekordType {
        Key<Sandvich, Filling> filling = Key.named("filling");
        Key<Sandvich, Bread> bread = Key.named("bread");
        Key<Sandvich, Style> styleWithoutTransformers = Key.named("style");
        Key<Sandvich, Style> style = styleWithoutTransformers.that(defaultsTo(Style.Flat));

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

    public static interface Wurst extends RekordType {
        Key<Wurst, Double> curvature = Key.named("curvature");
    }

    public static interface Bratwurst extends Wurst {
        Key<Bratwurst, Style> style = Key.named("style");

        public static enum Style {
            Chopped,
            Whole
        }
    }

    public static interface Bier extends RekordType {
        Key<Bier, Measurement.Volume> volume = Key.named("volume");
        Key<Bier, Measurement.Length> head = Key.named("head");
    }

    public static interface Person extends RekordType {
        Key<Person, String> firstName = Key.named("first name");
        Key<Person, String> lastName = Key.named("last name");
        Key<Person, Integer> age = Key.named("age");
        Key<Person, Rekord<Address>> address = Key.named("address");
    }

    public static interface Address extends RekordType {
        Key<Address, Integer> houseNumber = Key.named("house number");
        Key<Address, String> street = Key.named("street");
        Key<Address, String> city = Key.named("city");
        Key<Address, String> postalCode = Key.named("postal code");
    }

    @SuppressWarnings("UnusedDeclaration")
    public static final class Jar<T extends Jar.Contents> implements RekordType {
        private static final Key contents = Key.named("contents");
        @SuppressWarnings("unchecked")
        public static <T extends Jar.Contents> Key<Jar<T>, Collection<T>> contents() {
            return contents;
        }

        public static interface Contents { }

        public static final class Cookie implements Jar.Contents { }
    }
}
