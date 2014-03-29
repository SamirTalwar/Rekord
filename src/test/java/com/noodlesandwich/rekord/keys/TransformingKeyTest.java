package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.transformers.Transformer;
import org.junit.Test;

import static com.noodlesandwich.rekord.Transformers.defaultsTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class TransformingKeyTest {
    @Test public void
    transforms_according_to_its_transformer() {
        Key<Badabing, String> key = Key.<Badabing, String>named("key").that(upperCases());

        Properties properties = key.storeTo(new Properties(key), "kablammo");

        assertThat(key.retrieveFrom(properties), is("KABLAMMO"));
    }

    @Test public void
    delegates_to_internal_transformers() {
        Key<Badabing, String> key = Key.<Badabing, String>named("key").that(defaultsTo("nobody loves me")).then(upperCases());

        Properties properties = new Properties(key);

        assertThat(key.retrieveFrom(properties), is("NOBODY LOVES ME"));
    }

    @Test public void
    allows_the_transformer_to_change_the_type() {
        Key<Badabing, String> key = Key.<Badabing, Integer>named("key").that(defaultsTo(88)).then(stringifies());

        Properties emptyProperties = new Properties(key);
        Properties propertiesWithValue = key.storeTo(emptyProperties, "97");

        assertThat(key.retrieveFrom(propertiesWithValue), is("97"));
        assertThat(key.retrieveFrom(emptyProperties), is("88"));
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
