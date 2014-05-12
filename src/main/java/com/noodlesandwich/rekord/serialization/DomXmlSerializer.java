package com.noodlesandwich.rekord.serialization;

import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import com.noodlesandwich.rekord.FixedRekord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class DomXmlSerializer implements Serializer<Document> {
    private final DocumentBuilder documentBuilder;

    public DomXmlSerializer() {
        try {
            this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> Document serialize(FixedRekord<T> rekord) {
        Document document = documentBuilder.newDocument();
        NodeCreator nodeCreator = new NodeCreator(document);
        Element root = nodeCreator.elementNamed(rekord.name());
        Serialization.serialize(rekord).into(new DomXmlAccumulator(root, nodeCreator));
        document.appendChild(root);
        return document;
    }

    private static final class DomXmlAccumulator implements Accumulator<Element> {
        private final Element element;
        private final NodeCreator nodeCreator;

        public DomXmlAccumulator(Element element, NodeCreator nodeCreator) {
            this.element = element;
            this.nodeCreator = nodeCreator;
        }

        @Override
        public <V> void addValue(String name, V value) {
            Element child = nodeCreator.elementNamed(name);
            child.appendChild(nodeCreator.textNodeContaining(value.toString()));
            element.appendChild(child);
        }

        @Override
        public void addIterable(String name, Accumulation accumulation) {
            Element child = nodeCreator.elementNamed(name);
            accumulation.accumulateIn(new DomXmlAccumulator(child, nodeCreator));
            element.appendChild(child);
        }

        @Override
        public void addRekord(String name, String rekordName, Accumulation accumulation) {
            Element child = nodeCreator.elementNamed(name);
            accumulation.accumulateIn(new DomXmlAccumulator(child, nodeCreator));
            element.appendChild(child);
        }

        @Override
        public Element result() {
            return element;
        }
    }

    public static final class NodeCreator {
        private final Document document;

        public NodeCreator(Document document) {
            this.document = document;
        }

        public Element elementNamed(String name) {
            return document.createElement(slugify(name));
        }

        public Node textNodeContaining(String contents) {
            return document.createTextNode(contents);
        }

        private static String slugify(String name) {
            name = WhiteSpace.matcher(name).replaceAll("-");
            name = InvalidXmlNameChars.matcher(name).replaceAll("_");
            if (InvalidXmlNameStartChars.matcher(name.substring(0, 1)).matches()) {
                name = "_" + name;
            }
            return name.toLowerCase();
        }

        private static String join(String... strings) {
            StringBuilder builder = new StringBuilder();
            for (String string : strings) {
                builder.append(string);
            }
            return builder.toString();
        }

        private static final Pattern WhiteSpace = Pattern.compile(" ");
        private static final String XmlNameStartCharsString = join(
                ":", "_",
                "A-Z", "a-z",
                "\u00c0-\u00d6", "\u00d8-\u00f6", "\u00f8-\u02ff", "\u0370-\u037D",
                "\u037F-\u1FFF", "\u200C-\u200D", "\u2070-\u218F", "\u2C00-\u2FEF",
                "\u3001-\uD7FF", "\uF900-\uFDCF", "\uFDF0-\uFFFD"
        );
        private static final String XmlNameCharsString = join(
                XmlNameStartCharsString,
                "\\-", "\\.", "0-9", "\u00b7", "\u0300-\u036f", "\u203f-\u2040"
        );

        private static final Pattern InvalidXmlNameChars = Pattern.compile("[^" + XmlNameCharsString + "]");
        private static final Pattern InvalidXmlNameStartChars = Pattern.compile("[^" + XmlNameStartCharsString + "]");

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
}
