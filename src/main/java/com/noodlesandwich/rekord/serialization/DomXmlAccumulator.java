package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.Key;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class DomXmlAccumulator implements Serializer.Accumulator<Document> {
    private final Element root;
    private final Document document;

    public DomXmlAccumulator(Element root, Document document) {
        this.root = root;
        this.document = document;
    }

    @Override
    public <V> void accumulate(Key<?, V> key, V value) {
        Element element = document.createElement(DomXmlSerializer.slugify(key.toString()));
        element.appendChild(document.createTextNode(value.toString()));
        root.appendChild(element);
    }

    @Override
    public void accumulateRekord(Key<?, ?> key, Document serializedRekord) {
        Element adoptedElement = (Element) document.adoptNode(serializedRekord.getDocumentElement());
        Element element = document.createElement(DomXmlSerializer.slugify(key.toString()));
        while (adoptedElement.hasChildNodes()) {
            Node child = adoptedElement.removeChild(adoptedElement.getFirstChild());
            element.appendChild(child);
        }
        root.appendChild(element);
    }

    @Override
    public Document finish() {
        return document;
    }
}
