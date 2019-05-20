package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Arrays;

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
//        area = b1.getArea()+b2.getArea();
//        int newLength = b1.geoJSON.length + b2.geoJSON.length;
//        geoJSON = Arrays.copyOf(b1.getGeoJSON(), newLength);
//        if (newLength - b1.getGeoJSON().length >= 0)
//            System.arraycopy(b2.geoJSON, b1.getGeoJSON().length, geoJSON, b1.getGeoJSON().length, newLength - b1.getGeoJSON().length);
//        System.out.println("old length: " + b1.getGeoJSON().length);
//        System.out.println("old length2: "+ b2.getGeoJSON().length);
//        double[][] combinedGeojson = new double[b1.getGeoJSON().length + b2.getGeoJSON().length][];
//        System.arraycopy(b1.getGeoJSON(), 0, combinedGeojson, 0, b1.getGeoJSON().length);
//        System.arraycopy(b2.getGeoJSON(), 0, combinedGeojson, b1.getGeoJSON().length, b2.getGeoJSON().length);
//
//        System.out.println("new array: " + combinedGeojson);
//        System.out.println("new array length: " + combinedGeojson.length);
//        System.out.println("new array2: " + Arrays.deepToString(combinedGeojson));

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
//        setArea();
    }

//    public void setArea(){
//        Coordinate[] coArr = new Coordinate[geoJSON.length];
//        for(int i=0;i<coArr.length;i++){
//            Coordinate co = new Coordinate(geoJSON[i][0],geoJSON[i][1]);
//            coArr[i] = co;
//        }
//        GeometryFactory gf = new GeometryFactory();
//        Polygon p = gf.createPolygon(coArr);
//        area = p.getArea();
//    }

    public void add(Boundary other) {
        //TODO
//        System.out.println("old length: " + geoJSON.length);
//        System.out.println("old length2: "+ other.getGeoJSON().length);
//        double[][] combinedGeojson = new double[geoJSON.length + other.getGeoJSON().length][];
//        System.arraycopy(geoJSON, 0, combinedGeojson, 0, geoJSON.length);
//        System.arraycopy(other.getGeoJSON(), 0, combinedGeojson, geoJSON.length, other.getGeoJSON().length);
//
//        System.out.println("new array: " + combinedGeojson);
//        System.out.println("new array length: " + combinedGeojson.length);
    }

    public Boundary remove(Boundary other) {
        //TODO
//        int biglength = geoJSON.length;
//        int smalllength=other.getGeoJSON().length;
//        for (int i =0; i<=smalllength; i++){
//            for (int j =0; j <=biglength; j++){
//                if(other.getGeoJSON()[j].equals(geoJSON[i])){
//                    geoJSON[i] = null;
//                }
//
//            }
//        }
//        area-=other.getArea();
        return other;

    }

    @Override
    public String toString() {
        return id == null? id.toString() : "";
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