package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.Rekords;
import org.junit.Test;

import static com.noodlesandwich.rekord.keys.CopiedKeyTest.Bratwurst.Style.Chopped;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Wurst;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CopiedKeyTest {
    @Test public void
    has_the_same_name_as_the_underlying_key() {
        assertThat(Bratwurst.curvature.name(), is("curvature"));
    }

    @Test public void
    behaves_as_if_it_were_the_underlying_key() {
        Rekord<Bratwurst> bratwurst = Bratwurst.rekord
                .with(Bratwurst.curvature, 0.7)
                .with(Bratwurst.style, Chopped);

        assertThat(bratwurst.get(Bratwurst.curvature), is(0.7));
    }

    public interface Bratwurst {
        Key<Bratwurst, Double> curvature = CopiedKey.from(Wurst.curvature);
        Key<Bratwurst, Style> style = SimpleKey.named("style");

        Rekord<Bratwurst> rekord = Rekords.of(Bratwurst.class).accepting(curvature, style);

        public static enum Style {
            Chopped,
            Whole
        }
    }
}
