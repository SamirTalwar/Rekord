package com.noodlesandwich.rekord.serialization;

import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import com.google.common.base.Joiner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class DomXmlSerializer implements RekordSerializer<Element, Document> {
    private final DocumentBuilder documentBuilder;

    public DomXmlSerializer() {
        try {
            this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Serializer<Element> start(String name) {
        NodeCreator nodeCreator = new NodeCreator(documentBuilder.newDocument());
        Element root = nodeCreator.elementNamed(name);
        return RekordSerializers.serializer(new DomXmlBuilder(nodeCreator), new DomXmlAccumulator(root));
    }

    @Override
    public Document finish(Serializer<Element> serializer) {
        Document document = documentBuilder.newDocument();
        Element root = serializer.serialized();
        document.adoptNode(root);
        document.appendChild(root);
        return document;
    }

    public static final class DomXmlBuilder implements Builder<Element> {
        private final NodeCreator nodeCreator;

        public DomXmlBuilder(NodeCreator nodeCreator) {
            this.nodeCreator = nodeCreator;
        }

        @Override
        public SerializedProperty<Element> single(String name, Object value) {
            return new SingleElement(nodeCreator, name, value);
        }

        @Override
        public Serializer<Element> collection(String name) {
            Element child = nodeCreator.elementNamed(name);
            return RekordSerializers.serializer(this, new DomXmlCollectionAccumulator(child));
        }

        @Override
        public Serializer<Element> nest(String name) {
            Element child = nodeCreator.elementNamed(name);
            return RekordSerializers.serializer(this, new DomXmlAccumulator(child));
        }
    }

    public static final class SingleElement implements SerializedProperty<Element> {
        private final NodeCreator nodeCreator;
        private final String name;
        private final Object value;

        public SingleElement(NodeCreator nodeCreator, String name, Object value) {
            this.nodeCreator = nodeCreator;
            this.name = name;
            this.value = value;
        }

        @Override
        public Element serialized() {
            Element element = nodeCreator.elementNamed(name);
            element.appendChild(nodeCreator.textNodeContaining(value.toString()));
            return element;
        }
    }

    public static final class DomXmlCollectionAccumulator implements Accumulator<Element> {
        private final Element element;

        public DomXmlCollectionAccumulator(Element element) {
            this.element = element;
        }

        @Override
        public void accumulate(String name, SerializedProperty<Element> property) {
            element.appendChild(property.serialized());
        }

        @Override
        public Element serialized() {
            return element;
        }
    }

    public static final class DomXmlAccumulator implements Accumulator<Element> {
        private final Element element;

        public DomXmlAccumulator(Element element) {
            this.element = element;
        }

        @Override
        public void accumulate(String name, SerializedProperty<Element> property) {
            element.appendChild(property.serialized());
        }

        @Override
        public Element serialized() {
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

        private static final Pattern WhiteSpace = Pattern.compile(" ");
        private static final String XmlNameStartCharsString = Joiner.on("").join(
                ":", "_",
                "A-Z", "a-z",
                "\u00c0-\u00d6", "\u00d8-\u00f6", "\u00f8-\u02ff", "\u0370-\u037D",
                "\u037F-\u1FFF", "\u200C-\u200D", "\u2070-\u218F", "\u2C00-\u2FEF",
                "\u3001-\uD7FF", "\uF900-\uFDCF", "\uFDF0-\uFFFD"
        );
        private static final String XmlNameCharsString = Joiner.on("").join(
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
