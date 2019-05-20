package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

import javax.persistence.*;

@Entity
public class Boundary implements ResponseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    @OrderColumn
    private double[][] geoJSON;

    @Transient
    double area;

    public Boundary() {
    }

    public Boundary(Boundary boundary) {
        geoJSON = boundary.getGeoJSON();
    }

    public Boundary(double[][] geoJSON) {
        this.geoJSON = geoJSON;
    }

    public Boundary(Boundary b1, Boundary b2) {
        //TODO
        area = b1.getArea()+b2.getArea();
    }


    public double getArea(){ return area; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double[][] getGeoJSON() {
        return geoJSON;
    }

    public void setGeoJSON(double[][] geoJSON) {
        this.geoJSON = geoJSON;
        setArea();
    }

    public void setArea(){
        Coordinate[] coArr = new Coordinate[geoJSON.length];
        for(int i=0;i<coArr.length;i++){
            Coordinate co = new Coordinate(geoJSON[i][0],geoJSON[i][1]);
            coArr[i] = co;
        }
        GeometryFactory gf = new GeometryFactory();
        Polygon p = gf.createPolygon(coArr);
        area = p.getArea();
    }

    public void add(Boundary other) {
        //TODO
        area += other.getArea();
    }

    public Boundary remove(Boundary other) {
        //TODO
        area-=other.getArea();
        return other;

    }

    @Override
    public String toString() {
        return geoJSON.toString();
    }

    @Override
    public JSONObject toJSONObject() {
        //try {
            return null;
//        }catch (JSONException ex) {
//            System.out.printf("Exception catched when returning a geoJson: %s \n", ex.getMessage());
//            return null;
//        }
    }
}
