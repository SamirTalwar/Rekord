package com.noodlesandwich.rekord.extra;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Kollector;

public final class DomXmlKollector implements Kollector<Document> {
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
    public Accumulator<Document> accumulator() {
        return new XmlAccumulator(name, documentBuilder.newDocument());
    }

    private static final class XmlAccumulator implements Accumulator<Document> {
        private final Document document;
        private final Element root;

        public XmlAccumulator(String name, Document document) {
            this.document = document;
            this.root = document.createElement(name);
            document.appendChild(root);
        }

        @Override
        public <V> void accumulate(Key<?, V> key, V value) {
            Element element = document.createElement(key.toString());
            element.appendChild(document.createTextNode(value.toString()));
            root.appendChild(element);
        }

        @Override
        public Document finish() {
            return document;
        }
    }
}
