package com.noodlesandwich.rekord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.junit.Test;
import com.noodlesandwich.rekord.transformers.Transformer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static com.noodlesandwich.rekord.Transformers.defaultsTo;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Ham;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Jam;

public final class RekordTransformationsTest {
    @Test public void
    a_Rekord_can_store_and_retrieve_values_using_a_transformed_key() {
        Key<Sandvich, Collection<Sandvich.Filling>> fillings = Sandvich
                .filling.that(yieldsThreeTimes(Sandvich.Filling.class));
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.bread, Brown)
                .with(fillings, Collections.singleton(Jam));

        assertThat(sandvich.get(fillings), contains(Jam, Jam, Jam));
    }

    @Test public void
    a_Rekord_can_store_values_using_a_key_and_retrieve_them_with_transformations() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Ham);

        assertThat(sandvich.get(Sandvich.filling.that(yieldsThreeTimes(Sandvich.Filling.class))),
                   contains(Ham, Ham, Ham));
    }

    @Test public void
    a_Rekord_can_store_values_using_a_transformed_key_and_retrieve_them_without_transformations() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.bread.that(defaultsTo(White)), Brown);

        assertThat(sandvich.get(Sandvich.bread), is(Brown));
    }

    private static <T> Transformer<T, Collection<T>> yieldsThreeTimes(@SuppressWarnings("unused") Class<T> type) {
        return new Transformer<T, Collection<T>>() {
            @Override public T transformInput(Collection<T> value) {
                Iterator<T> iterator = value.iterator();
                return iterator.hasNext() ? iterator.next() : null;
            }

            @Override public Collection<T> transformOutput(T value) {
                Collection<T> values = new ArrayList<>(3);
                values.add(value);
                values.add(value);
                values.add(value);
                return values;
            }
        };
    }
}
