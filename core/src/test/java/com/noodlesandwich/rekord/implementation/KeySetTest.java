package com.noodlesandwich.rekord.implementation;

import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.keys.SimpleKey;
import com.noodlesandwich.rekord.testobjects.TestRekords.Wurst;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class KeySetTest {
    private static final Keys<Wurst> keys = KeySet.from(Wurst.curvature, Wurst.color);
    private static final Key<Wurst, Object> someOtherKey = SimpleKey.named("something else");

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test public void
    a_key_set_can_look_up_keys_by_name() {
        assertThat(keys.<Wurst.Color>keyNamed("color"), is(Wurst.color));
    }

    @Test public void
    a_key_set_will_throw_an_exception_if_looking_up_a_non_existent_key() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The key \"frob\" does not exist.");

        keys.keyNamed("frob");
    }

    @Test public void
    a_key_set_can_verify_that_it_contains_a_key() {
        assertThat(keys.contains(Wurst.curvature), is(true));
    }

    @Test public void
    a_key_set_can_verify_that_it_does_not_contain_a_key() {
        assertThat(keys.contains(someOtherKey), is(false));
    }
}
