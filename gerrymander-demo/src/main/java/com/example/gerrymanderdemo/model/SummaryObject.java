package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;


public class SummaryObject implements ResponseObject {
    private Long stateId;
    private int[] seats = new int[Party.values().length];
    private double objFuncVal;
    private int numMajMinDistricts;

    public SummaryObject(State state, double objFuncVal, int numMajMinDistricts) {
        this.stateId = state.getId();
        for (District d : state.getDistricts()) {
            seats[d.getData().getVoteData().getResult().ordinal()]++;
        }
        this.objFuncVal = objFuncVal;
        this.numMajMinDistricts = numMajMinDistricts;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject res = new JSONObject();
        try {
            res.put("STATE-ID", stateId);
            res.put(Party.DEMOCRATIC.toString(), seats[Party.DEMOCRATIC.ordinal()]);
            res.put(Party.REPUBLICAN.toString(), seats[Party.REPUBLICAN.ordinal()]);
            res.put("OBJECTIVE-FUNCTION-VALUE", objFuncVal);
            res.put("NUMBER-OF-MAJORITYMINORITY-DISTRICT", numMajMinDistricts);
            return res;
        } catch (JSONException ex) {
            System.out.println("Error when getting SummeryObject");
            return null;
        }
    }
}
