package com.noodlesandwich.rekord.serialization;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static com.noodlesandwich.rekord.serialization.Serializer.Accumulator;
import static com.noodlesandwich.rekord.serialization.Serializer.SerializedProperty;

public final class DomXmlAccumulator implements Accumulator<Element> {
    private final Document document;
    private final Element element;

    public DomXmlAccumulator(Document document, Element element) {
        this.document = document;
        this.element = element;
    }

    @Override
    public SerializedProperty<Element> single(String name, Object value) {
        return new SingleElement(document, name, value);
    }

    @Override
    public Accumulator<Element> collection(String name) {
        Element child = elementNamed(name, document);
        return new DomXmlCollectionAccumulator(document, child);
    }

    @Override
    public Accumulator<Element> nest(String name) {
        Element child = elementNamed(name, document);
        return new DomXmlAccumulator(document, child);
    }

    @Override
    public void accumulate(String name, SerializedProperty<Element> property) {
        element.appendChild(property.serialized());
    }

    @Override
    public Element serialized() {
        return element;
    }

    public Document document() {
        return document;
    }

    private static final class SingleElement implements SerializedProperty<Element> {
        private final Document document;
        private final String name;
        private final Object value;

        public SingleElement(Document document, String name, Object value) {
            this.document = document;
            this.name = name;
            this.value = value;
        }

        @Override
        public Element serialized() {
            Element element = elementNamed(name, document);
            element.appendChild(document.createTextNode(value.toString()));
            return element;
        }
    }

    private static final class DomXmlCollectionAccumulator implements Accumulator<Element> {
        private final Document document;
        private final Element element;

        public DomXmlCollectionAccumulator(Document document, Element element) {
            this.document = document;
            this.element = element;
        }

        @Override
        public SerializedProperty<Element> single(String name, Object value) {
            return new SingleElement(document, name, value);
        }

        @Override
        public Accumulator<Element> collection(String name) {
            Element child = elementNamed(name, document);
            return new DomXmlCollectionAccumulator(document, child);
        }

        @Override
        public Accumulator<Element> nest(String name) {
            Element child = elementNamed(name, document);
            return new DomXmlAccumulator(document, child);
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

    private static Element elementNamed(String name, Document document) {
        return document.createElement(DomXmlSerializer.slugify(name));
    }
}
