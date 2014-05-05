package com.noodlesandwich.rekord.serialization;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static com.noodlesandwich.rekord.serialization.Serializer.Accumulator;
import static com.noodlesandwich.rekord.serialization.Serializer.SerializedProperty;

public final class DomXmlAccumulator implements Accumulator<Element> {
    private final Document document;
    private final Element root;

    public DomXmlAccumulator(Document document, Element root) {
        this.document = document;
        this.root = root;
    }

    @Override
    public SerializedProperty<Element> single(String name, Object value) {
        return new SingleElement(name, value);
    }

    @Override
    public Accumulator<Element> nest(String name) {
        Element element = elementNamed(name);
        return new DomXmlAccumulator(document, element);
    }

    @Override
    public void accumulate(String name, SerializedProperty<Element> property) {
        root.appendChild(property.serialized());
    }

    @Override
    public Element serialized() {
        return root;
    }

    public Document document() {
        return document;
    }

    private Element elementNamed(String name) {
        return document.createElement(DomXmlSerializer.slugify(name));
    }

    private final class SingleElement implements SerializedProperty<Element> {
        private final String name;
        private final Object value;

        public SingleElement(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public Element serialized() {
            Element element = elementNamed(name);
            element.appendChild(document.createTextNode(value.toString()));
            return element;
        }
    }
}
