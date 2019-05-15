package com.example.gerrymanderdemo.JacksonSerializer;

import com.example.gerrymanderdemo.model.Precinct;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class PrecinctBoundarySerializer extends StdSerializer<Precinct> {

    public PrecinctBoundarySerializer() {
        this(null);
    }

    private PrecinctBoundarySerializer(Class<Precinct> t) {
        super(t);
    }

    @Override
    public void serialize(Precinct precinct, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("boundary", precinct.getData().getBoundary().getGeoJSON());
        jsonGenerator.writeEndObject();
    }
}
