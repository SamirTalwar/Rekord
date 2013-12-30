package com.noodlesandwich.rekord.extra;

import org.junit.Test;
import org.w3c.dom.Document;
import com.google.common.base.Joiner;
import com.noodlesandwich.rekord.Rekord;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlmatchers.XmlMatchers.isSimilarTo;
import static org.xmlmatchers.transform.XmlConverters.the;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Roll;

public final class DomXmlKollectorTest {
    @Test public void
    a_rekord_with_one_element_is_kollected_into_XML() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Cheese);

        Document document = sandvich.collect(new DomXmlKollector("sandvich"));

        assertThat(the(document), isSimilarTo(the(lines(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
                "<sandvich>",
                "    <filling>Cheese</filling>",
                "</sandvich>"
        ))));
    }
    @Test public void
    a_rekord_with_multiple_elements_is_kollected_into_XML() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.bread, Brown)
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Roll);

        Document document = sandvich.collect(new DomXmlKollector("sandvich"));

        assertThat(the(document), isSimilarTo(the(lines(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
                "<sandvich>",
                "    <bread>Brown</bread>",
                "    <filling>Cheese</filling>",
                "    <style>Roll</style>",
                "</sandvich>"
        ))));
    }

    private static String lines(String... lines) {
        return Joiner.on('\n').join(lines);
    }
}
