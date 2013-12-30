package com.noodlesandwich.rekord.extra;

import org.junit.Test;
import org.w3c.dom.Document;
import com.google.common.base.Joiner;
import com.noodlesandwich.rekord.Rekord;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlmatchers.XmlMatchers.isEquivalentTo;
import static org.xmlmatchers.transform.XmlConverters.the;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Cheese;

public final class DomXmlKollectorTest {
    @Test public void
    a_rekord_with_one_element_is_kollected_into_XML() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Cheese);

        Document document = sandvich.collect(new DomXmlKollector("sandvich"));

        assertThat(the(document), isEquivalentTo(the(lines(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
                "<sandvich>",
                "    <filling>Cheese</filling>",
                "</sandvich>"
        ))));
    }

    private static String lines(String... lines) {
        return Joiner.on('\n').join(lines);
    }
}
