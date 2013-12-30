package com.noodlesandwich.rekord.extra;

import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.google.common.base.Joiner;
import com.noodlesandwich.rekord.Kollector;

public final class DomXmlKollector implements Kollector<DomXmlAccumulator, Document> {
    private final String name;
    private final DocumentBuilder documentBuilder;

    public DomXmlKollector(String name) {
        this.name = name;
        try {
            this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DomXmlAccumulator accumulator() {
        Document document = documentBuilder.newDocument();
        Element root = document.createElement(slugify(name));
        document.appendChild(root);
        return new DomXmlAccumulator(root, document);
    }

    public Document finish(DomXmlAccumulator accumulator) {
        return accumulator.document();
    }

    public static String slugify(String name) {
        name = name.replaceAll(" ", "-");
        name = InvalidXmlNameChars.matcher(name).replaceAll("_");
        if (InvalidXmlNameStartChars.matcher(name.substring(0, 1)).matches()) {
            name = "_" + name;
        }
        return name.toLowerCase();
    }

    private static final String XmlNameStartCharsString = Joiner.on("").join(
            ":", "_",
            "A-Z", "a-z",
            "\u00c0-\u00d6", "\u00d8-\u00f6", "\u00f8-\u02ff", "\u0370-\u037D",
            "\u037F-\u1FFF", "\u200C-\u200D", "\u2070-\u218F", "\u2C00-\u2FEF",
            "\u3001-\uD7FF", "\uF900-\uFDCF", "\uFDF0-\uFFFD"
    );
    private static final Pattern InvalidXmlNameStartChars = Pattern.compile("[^" + XmlNameStartCharsString + "]");

    private static final String XmlNameCharsString = Joiner.on("").join(
            XmlNameStartCharsString,
            "\\-", "\\.", "0-9", "\u00b7", "\u0300-\u036f", "\u203f-\u2040"
    );
    private static final Pattern InvalidXmlNameChars = Pattern.compile("[^" + XmlNameCharsString + "]");

    /*
    NameStartChar   ::=
        ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] |
        [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] |
        [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] |
        [#x10000-#xEFFFF]

    NameChar    ::=
        NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]

    Name    ::=
        NameStartChar (NameChar)*
     */
}
