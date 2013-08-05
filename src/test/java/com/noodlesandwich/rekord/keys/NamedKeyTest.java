package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;

public class NamedKeyTest {
    @Test public void
    stringifies_to_its_name() {
        assertThat(Key.named("apple"), hasToString("apple"));
    }
}
