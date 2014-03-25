package com.noodlesandwich.rekord;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Ham;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Burger;

public final class RekordBuildersTest {
    @Test public void
    a_Rekord_can_be_built_from_another_Rekord() {
        Rekord<Sandvich> cheeseSandvich = Sandvich.rekord
                .with(Sandvich.filling, Cheese)
                .with(Brown, Sandvich.bread);

        Rekord<Sandvich> cheeseBurger = cheeseSandvich
                .with(White, Sandvich.bread)
                .with(Sandvich.style, Burger);

        assertThat(cheeseBurger, is(Sandvich.rekord
                .with(Cheese, Sandvich.filling)
                .with(White, Sandvich.bread)
                .with(Sandvich.style, Burger)));
    }

    @Test public void
    after_building_a_Rekord_from_another_Rekord_the_original_does_not_mutate() {
        Rekord<Sandvich> sandvichBuilder = Sandvich.rekord
                .with(White, Sandvich.bread);

        Rekord<Sandvich> cheeseSandvich = sandvichBuilder.with(Sandvich.filling, Cheese);
        Rekord<Sandvich> hamSandvich = sandvichBuilder.with(Sandvich.filling, Ham);

        assertThat(cheeseSandvich, is(Sandvich.rekord
                .with(White, Sandvich.bread)
                .with(Sandvich.filling, Cheese)));

        assertThat(hamSandvich, is(Sandvich.rekord
                .with(White, Sandvich.bread)
                .with(Sandvich.filling, Ham)));

        assertThat(cheeseSandvich, is(not(equalTo(hamSandvich))));
    }
}
