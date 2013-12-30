package com.noodlesandwich.rekord.extra;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Kollector;

final class DomXmlAccumulator implements Kollector.Accumulator {
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

    public Document document() {
        return document;
    }
}
