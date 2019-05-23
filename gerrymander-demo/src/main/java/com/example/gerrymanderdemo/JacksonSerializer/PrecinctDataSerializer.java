package com.example.gerrymanderdemo.JacksonSerializer;

import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Precinct;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class PrecinctDataSerializer extends StdSerializer<Precinct> {

    public PrecinctDataSerializer() {
        this(null);
    }

    private PrecinctDataSerializer(Class<Precinct> t) {
        super(t);
    }

    @Override
    public void serialize(Precinct precinct, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", precinct.getId());
        jsonGenerator.writeStringField("name", precinct.getName());
        jsonGenerator.writeStringField("county", precinct.getCounty());
        jsonGenerator.writeNumberField("republican", precinct.getData().getVoteData().getVote(Party.REPUBLICAN));
        jsonGenerator.writeNumberField("democratic", precinct.getData().getVoteData().getVote(Party.DEMOCRATIC));
        jsonGenerator.writeNumberField("other_parties", precinct.getData().getVoteData().getVote(Party.OTHERS));
        jsonGenerator.writeNumberField("all", precinct.getData().getDemographic().getPopulation(RaceType.ALL));
        jsonGenerator.writeNumberField("caucasian", precinct.getData().getDemographic().getPopulation(RaceType.CAUCASIAN));
        jsonGenerator.writeNumberField("african_american", precinct.getData().getDemographic().getPopulation(RaceType.AFRICAN_AMERICAN));
        jsonGenerator.writeNumberField("asian", precinct.getData().getDemographic().getPopulation(RaceType.ASIAN_PACIFIC_AMERICAN));
        jsonGenerator.writeNumberField("native", precinct.getData().getDemographic().getPopulation(RaceType.NATIVE_AMERICAN));
        jsonGenerator.writeNumberField("hispanic", precinct.getData().getDemographic().getPopulation(RaceType.HISPANIC_LATINO_AMERICAN));
        jsonGenerator.writeNumberField("other_race", precinct.getData().getDemographic().getPopulation(RaceType.OTHERS));
        if(precinct.getDistrict() != null)
            jsonGenerator.writeNumberField("district_id", precinct.getDistrict().getId());
        if(precinct.isBorder())
            jsonGenerator.writeStringField("is_border", "yes");
        jsonGenerator.writeEndObject();
    }
}
