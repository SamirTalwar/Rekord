package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.SimpleKey;
import com.noodlesandwich.rekord.transformers.Transformer;
import org.junit.Test;

import static com.noodlesandwich.rekord.transformers.Transformers.defaultsTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public final class TransformingPropertyTest {
    @Test public void
    transforms_according_to_its_transformer() {
        Key<Badabing, String> key = SimpleKey.<Badabing, String>named("key").that(upperCases());

        Rekord<Badabing> rekord = Rekord.of(Badabing.class).accepting(key)
                .with(key.of("kablammo"));

        assertThat(rekord.get(key), is("KABLAMMO"));
    }

    @Test public void
    delegates_to_internal_transformers() {
        Key<Badabing, String> key = SimpleKey.<Badabing, String>named("key").that(defaultsTo("nobody loves me")).then(upperCases());

        Rekord<Badabing> rekord = Rekord.of(Badabing.class).accepting(key);

        assertThat(rekord.get(key), is("NOBODY LOVES ME"));
    }

    @Test public void
    allows_the_transformer_to_change_the_type() {
        Key<Badabing, Integer> key = SimpleKey.named("key");
        Key<Badabing, String> transformedKey = key.that(defaultsTo(88)).then(stringifies());

        Rekord<Badabing> emptyRekord = Rekord.of(Badabing.class).accepting(key);
        Rekord<Badabing> rekordWithValue = emptyRekord.with(key.of(45));
        Rekord<Badabing> rekordWithTransformedValue = emptyRekord.with(transformedKey.of("97"));

        assertThat(emptyRekord.get(key), is(nullValue()));
        assertThat(emptyRekord.get(transformedKey), is("88"));

        assertThat(rekordWithValue.get(key), is(45));
        assertThat(rekordWithValue.get(transformedKey), is("45"));

        assertThat(rekordWithTransformedValue.get(key), is(97));
        assertThat(rekordWithTransformedValue.get(transformedKey), is("97"));
    }

    private static interface Badabing { }

    private static Transformer<String, String> upperCases() {
        return new Transformer<String, String>() {
            @Override public String transformInput(String value) {
                return value;
            }

            @Override public String transformOutput(String value) {
                return value.toUpperCase();
            }
        };
    }

    private static Transformer<Integer, String> stringifies() {
        return new Transformer<Integer, String>() {
            @Override public Integer transformInput(String value) {
                return Integer.parseInt(value);
            }

            @Override public String transformOutput(Integer value) {
                return value.toString();
            }
        };
    }
}
