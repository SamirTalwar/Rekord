package com.noodlesandwich.rekord;

public final class Rekords {
    private Rekords() { }

    public static interface Sandvich extends RekordType {
        Key<Sandvich, Sandvich.Filling> filling = Key.named("filling");
        Key<Sandvich, Sandvich.Bread> bread = Key.named("bread");
        Key<Sandvich, Sandvich.Style> style = Key.named("style");

        public static enum Filling {
            Cheese,
            Ham,
            Lettuce
        }

        public static enum Bread {
            Brown,
            White
        }

        public static enum Style {
            Burger,
            Roll
        }
    }

    public static interface Wurst extends RekordType {
        Key<Wurst, Double> curvature = Key.named("curvature");
    }

    public static interface Bratwurst extends Wurst {
        Key<Bratwurst, Bratwurst.Style> style = Key.named("style");

        public static enum Style {
            Chopped,
            Whole
        }
    }

    public static interface Bier extends RekordType {
        Key<Bier, Measurement.Volume> volume = Key.named("volume");
        Key<Bier, Measurement.Length> head = Key.named("head");
    }
}
