package com.noodlesandwich.rekord;

import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Ham;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Burger;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Roll;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

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

    @Test public void
    two_rekords_can_be_merged_with_the_second_clobbering_the_first() {
        Rekord<Sandvich> whiteRoll = Sandvich.rekord
                .with(White, Sandvich.bread)
                .with(Sandvich.style, Roll);

        Rekord<Sandvich> cheeseBurger = Sandvich.rekord
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Burger);

        Rekord<Sandvich> whiteCheeseBurger = whiteRoll.merge(cheeseBurger);

        assertThat(whiteCheeseBurger, is(Sandvich.rekord
                .with(Sandvich.bread, White)
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Burger)));
    }
}
