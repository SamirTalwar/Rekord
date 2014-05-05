package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Rekord;
import org.junit.Test;

import static com.noodlesandwich.rekord.matchers.RekordMatchers.aRekordOf;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Jam;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Burger;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Roll;
import static com.noodlesandwich.rekord.validation.ValidRekord.ValidatingRekord;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ValidRekordTest {
    @Test public void
    builds_an_unchangeable_rekord() {
        ValidatingRekord<Sandvich> validatingSandvich = ValidRekord.of(Sandvich.class)
                .accepting(Sandvich.filling, Sandvich.bread, Sandvich.style)
                .allowing(noBurgers());

        FixedRekord<Sandvich> sandvich = validatingSandvich
                .with(Brown, Sandvich.bread)
                .with(Sandvich.filling, Jam)
                .with(Sandvich.style, Roll)
                .fix();

        assertThat(sandvich, is(aRekordOf(Sandvich.class)
                .with(Sandvich.bread, Brown)
                .with(Sandvich.filling, Jam)
                .with(Sandvich.style, Roll)));
    }

    private static ValidRekord.Validator<Sandvich> noBurgers() {
        return new ValidRekord.Validator<Sandvich>() {
            @Override public boolean test(Rekord<Sandvich> rekord) {
                return rekord.get(Sandvich.style) != Burger;
            }
        };
    }


}