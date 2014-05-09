package com.noodlesandwich.rekord.serialization;

import java.util.Map;
import com.noodlesandwich.rekord.Rekord;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Roll;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

public final class MapSerializerTest {
    @Test public void
    serializes_a_rekord_to_a_map_of_strings_to_objects() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.bread, White)
                .with(Sandvich.style, Roll);

        Map<String, Object> serialized = sandvich.serialize(new MapSerializer());

        assertThat(serialized.size(), is(2));
        assertThat(serialized, hasEntry("bread", (Object) White));
        assertThat(serialized, hasEntry("style", (Object) Roll));
    }
}
