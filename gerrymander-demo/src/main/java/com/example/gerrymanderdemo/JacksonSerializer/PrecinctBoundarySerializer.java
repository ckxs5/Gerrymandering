package com.example.gerrymanderdemo.JacksonSerializer;

import com.example.gerrymanderdemo.model.Precinct;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrecinctBoundarySerializer extends StdSerializer<ArrayList<Precinct>> {
    public PrecinctBoundarySerializer() {
        this(null);
    }

    private PrecinctBoundarySerializer(Class<ArrayList<Precinct>> t) {
        super(t);
    }

    @Override
    public void serialize(ArrayList<Precinct> precincts, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", "FeatureCollection");
        jsonGenerator.writeStringField("name", "precincts");
        jsonGenerator.writeStringField("description", "Minnesota Voting Precincts");
        jsonGenerator.writeArrayFieldStart("features");
        for (Precinct precinct : precincts) {
            jsonGenerator.writeStringField("type", "Feature");
            jsonGenerator.writeObjectFieldStart("properties");
            jsonGenerator.writeStringField("Precinct", precinct.getName());
            jsonGenerator.writeNumberField("PrecinctID", precinct.getId());
            jsonGenerator.writeStringField("County", precinct.getCounty());
            jsonGenerator.writeStringField("State", precinct.getState().toString());
            jsonGenerator.writeEndObject();
            jsonGenerator.writeObjectFieldStart("geometry");
            jsonGenerator.writeStringField("type", "Polygon");
            jsonGenerator.writeObjectField("coordinates", precinct.getData().getBoundary().getGeoJSON());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
