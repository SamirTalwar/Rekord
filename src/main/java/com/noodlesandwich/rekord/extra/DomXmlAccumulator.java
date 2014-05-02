package com.noodlesandwich.rekord.extra;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Kollector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class DomXmlAccumulator implements Kollector.Accumulator<Document> {
    private final Element root;
    private final Document document;

    public DomXmlAccumulator(Element root, Document document) {
        this.root = root;
        this.document = document;
    }

    @Override
    public <V> void accumulate(Key<?, V> key, V value) {
        Element element = document.createElement(DomXmlKollector.slugify(key.toString()));
        element.appendChild(document.createTextNode(value.toString()));
        root.appendChild(element);
    }

    @Override
    public void accumulateRekord(Key<?, ?> key, Document kollectedRekord) {
        Element adoptedElement = (Element) document.adoptNode(kollectedRekord.getDocumentElement());
        Element element = document.createElement(DomXmlKollector.slugify(key.toString()));
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
