package com.noodlesandwich.rekord.serialization;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class DomXmlAccumulator implements Serializer.Accumulator<Element, Document> {
    private final Document document;
    private final Element root;

    public DomXmlAccumulator(Document document, Element root) {
        this.document = document;
        this.root = root;
    }

    @Override
    public void accumulate(String name, Object value) {
        Element element = elementNamed(name);
        element.appendChild(document.createTextNode(value.toString()));
        root.appendChild(element);
    }

    @Override
    public void accumulateNested(String name, Serializer.Accumulator<Element, Document> accumulator) {
        root.appendChild(accumulator.value());
    }

    @Override
    public Serializer.Accumulator<Element, Document> nest(String name) {
        Element element = elementNamed(name);
        root.appendChild(element);
        return new DomXmlAccumulator(document, element);
    }

    @Override
    public Element value() {
        return root;
    }

    @Override
    public Document finish() {
        return document;
    }

    private Element elementNamed(String name) {
        return document.createElement(DomXmlSerializer.slugify(name));
    }
}
