package com.example.gerrymanderdemo;

import com.example.gerrymanderdemo.model.Enum.JoinabilityMeasureType;

public class Properties {
    public static Properties properties;
    private static int[] joinabilityMeasureRatio = new int[JoinabilityMeasureType.values().length];

    private Properties() {
        for(int i = 0; i < joinabilityMeasureRatio.length; i++){
            joinabilityMeasureRatio[i] = 1;
        }

    }

    public static int getJoinabilityMeasureRatio (JoinabilityMeasureType measureType) {
        if (properties == null) {
            properties = new Properties();
        }
        return joinabilityMeasureRatio[measureType.ordinal()];
    }
}
