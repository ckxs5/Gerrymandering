package com.example.gerrymanderdemo.JacksonSerializer;

import com.example.gerrymanderdemo.model.Cluster;
import com.example.gerrymanderdemo.model.District;
import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ClusterDataSerializer extends StdSerializer<Cluster> {
    public ClusterDataSerializer(){
        this(null);
    }

    protected ClusterDataSerializer(Class<Cluster> t) {
        super(t);
    }

    @Override
    public void serialize(Cluster cluster, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
//        jsonGenerator.writeNumberField("id", cluster.getId());
        jsonGenerator.writeNumberField("republican", cluster.getData().getVoteData().getVote(Party.REPUBLICAN));
        jsonGenerator.writeNumberField("democratic", cluster.getData().getVoteData().getVote(Party.DEMOCRATIC));
        jsonGenerator.writeNumberField("other_parties", cluster.getData().getVoteData().getVote(Party.OTHERS));
        jsonGenerator.writeNumberField("all", cluster.getData().getDemographic().getPopulation(RaceType.ALL));
        jsonGenerator.writeNumberField("caucasian", cluster.getData().getDemographic().getPopulation(RaceType.CAUCASIAN));
        jsonGenerator.writeNumberField("african_american", cluster.getData().getDemographic().getPopulation(RaceType.AFRICAN_AMERICAN));
        jsonGenerator.writeNumberField("asian", cluster.getData().getDemographic().getPopulation(RaceType.ASIAN_PACIFIC_AMERICAN));
        jsonGenerator.writeNumberField("native", cluster.getData().getDemographic().getPopulation(RaceType.NATIVE_AMERICAN));
        jsonGenerator.writeNumberField("hispanic", cluster.getData().getDemographic().getPopulation(RaceType.HISPANIC_LATINO_AMERICAN));
        jsonGenerator.writeNumberField("other_race", cluster.getData().getDemographic().getPopulation(RaceType.OTHERS));
        jsonGenerator.writeEndObject();
    }
}