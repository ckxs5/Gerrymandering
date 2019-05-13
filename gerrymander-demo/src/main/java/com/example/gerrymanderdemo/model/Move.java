package com.example.gerrymanderdemo.model;

public class Move {
    private District to;
    private District from;
    private Precinct precinct;

    public Move(District to, District from, Precinct precinct){
        this.to = to;
        this.from = from;
        this.precinct = precinct;
    }

    public void execute(){
        from.removePrecinct(precinct);
        to.addPrecinct(precinct);
    }

    public void undo(){
        to.removePrecinct(precinct);
        from.addPrecinct(precinct);
    }

    public String toString(){
        String toID = to!=null?to.getId():"NULL";
        String fromID = from!=null?from.getId():"NULL";
        String precinctID = precinct!=null?precinct.getId():"NULL";
        return "{"+"\"to\":\""+toID+"\",\"from\":\""+fromID+"\",\"precinct\":\""+precinctID+"\"}";
    }

    public District getTo(){
        return to;
    }

    public District getFrom(){
        return from;
    }

    public Precinct getPrecinct(){
        return precinct;
    }
}
