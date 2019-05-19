package com.example.gerrymanderdemo.JacksonSerializer;

import com.example.gerrymanderdemo.model.District;
import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class DistrictDataSerializer extends StdSerializer<District> {
    public DistrictDataSerializer(){
        this(null);
    }

    protected DistrictDataSerializer(Class<District> t) {
        super(t);
    }

    @Override
    public void serialize(District district, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", district.getId());
        jsonGenerator.writeNumberField("republican", district.getData().getVoteData().getVote(Party.REPUBLICAN));
        jsonGenerator.writeNumberField("democratic", district.getData().getVoteData().getVote(Party.DEMOCRATIC));
        jsonGenerator.writeNumberField("other_parties", district.getData().getVoteData().getVote(Party.OTHERS));
        jsonGenerator.writeNumberField("all", district.getData().getDemographic().getPopulation(RaceType.ALL));
        jsonGenerator.writeNumberField("caucasian", district.getData().getDemographic().getPopulation(RaceType.CAUCASIAN));
        jsonGenerator.writeNumberField("african_american", district.getData().getDemographic().getPopulation(RaceType.AFRICAN_AMERICAN));
        jsonGenerator.writeNumberField("asian", district.getData().getDemographic().getPopulation(RaceType.ASIAN_PACIFIC_AMERICAN));
        jsonGenerator.writeNumberField("native", district.getData().getDemographic().getPopulation(RaceType.NATIVE_AMERICAN));
        jsonGenerator.writeNumberField("hispanic", district.getData().getDemographic().getPopulation(RaceType.HISPANIC_LATINO_AMERICAN));
        jsonGenerator.writeNumberField("other_race", district.getData().getDemographic().getPopulation(RaceType.OTHERS));
        jsonGenerator.writeEndObject();
    }
}
