package com.noodlesandwich.rekord.extra;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.serialization.Serialization;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class JacksonSerializer implements Serializer<Void, IOException> {
    private final Writer writer;

    private JacksonSerializer(Writer writer) {
        this.writer = writer;
    }

    public static Serializer<Void, IOException> serializingToWriter(Writer writer) {
        return new JacksonSerializer(writer);
    }

    public static Serializer<String, IOException> serializingToString() {
        return new Serializer<String, IOException>() {
            @Override
            public <T> String serialize(FixedRekord<T> rekord) throws IOException {
                StringWriter writer = new StringWriter();
                serializingToWriter(writer).serialize(rekord);
                return writer.toString();
            }
        };
    }

    @Override
    public <T> Void serialize(FixedRekord<T> rekord) throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = factory.createGenerator(writer);
        generator.writeStartObject();
        Serialization.serialize(rekord).into(new JacksonRekordAccumulator(generator));
        generator.writeEndObject();
        generator.close();
        return null;
    }

    private static final class JacksonRekordAccumulator implements Accumulator<Void, IOException> {
        private final JsonGenerator generator;

        public JacksonRekordAccumulator(JsonGenerator generator) {
            this.generator = generator;
        }

        @Override
        public <V> void addValue(String name, V value) throws IOException {
            generator.writeStringField(name, value.toString());
        }

        @Override
        public void addIterable(String name, Accumulation accumulation) throws IOException {
            generator.writeFieldName(name);
            generator.writeStartArray();
            accumulation.accumulateIn(new JacksonIterableAccumulator(generator));
            generator.writeEndArray();
        }

        @Override
        public void addRekord(String name, String rekordName, Accumulation accumulation) throws IOException {
            generator.writeFieldName(name);
            generator.writeStartObject();
            accumulation.accumulateIn(this);
            generator.writeEndObject();
        }

        @Override
        public Void result() {
            return null;
        }
    }

    private static final class JacksonIterableAccumulator implements Accumulator<Void, IOException> {
        private final JsonGenerator generator;

        public JacksonIterableAccumulator(JsonGenerator generator) {
            this.generator = generator;
        }

        @Override
        public <V> void addValue(String name, V value) throws IOException {
            generator.writeString(value.toString());
        }

        @Override
        public void addIterable(String name, Accumulation accumulation) throws IOException {
            generator.writeStartArray();
            accumulation.accumulateIn(this);
            generator.writeEndArray();
        }

        @Override
        public void addRekord(String name, String rekordName, Accumulation accumulation) throws IOException {
            generator.writeStartObject();
            accumulation.accumulateIn(new JacksonRekordAccumulator(generator));
            generator.writeEndObject();
        }

        @Override
        public Void result() {
            return null;
        }
    }
}
