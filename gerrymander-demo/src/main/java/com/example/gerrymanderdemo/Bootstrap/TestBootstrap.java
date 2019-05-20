package com.example.gerrymanderdemo.Bootstrap;

import com.example.gerrymanderdemo.Service.PrecinctService;
import com.example.gerrymanderdemo.model.Precinct;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
public class TestBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    PrecinctService precinctService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Precinct precinct1 =  precinctService.findById(new Long(270170110)).get();
        Precinct precinct2 =  precinctService.findById(new Long(270170115)).get();
        double[][] boundary1 = precinct1.getBoundary();
        double[][] boundary2 = precinct2.getBoundary();


        double[] xPoints = {-94.81201171875,-95.11962890625,-94.658203125,-94.1528320312,-94.02099609375,-94.81201171875};
        double[] yPoints = {45.67548217560647,45.38301927899065,45.10454630976873,45.22848059584359,45.5679096098613,45.67548217560647};
        double[] rec_xPoints = {-94.50439453125,-93.07617187499999,-93.07617187499999,-94.50439453125,-94.50439453125};
        double[] rec_yPoints = {44.99588261816546,44.99588261816546,45.81348649679973,45.81348649679973,44.99588261816546};

        Coordinate[] carr = new  Coordinate[6];
        for(int i=0; i<6; i++) {
            Coordinate co = new Coordinate(xPoints[i], yPoints[i]);
            carr[i] = co;
        }
        Coordinate[] rec_carr = new  Coordinate[5];
        for(int i=0; i<5; i++) {
            Coordinate co = new Coordinate(rec_xPoints[i], rec_yPoints[i]);
            rec_carr[i] = co;
        }

        GeometryFactory gf = new GeometryFactory();
        Polygon p = gf.createPolygon(carr);

        GeometryFactory rec_gf = new GeometryFactory();
        Polygon rec_p = rec_gf.createPolygon(rec_carr);

//        //calculate area
//        double area = p.getArea();
//        System.out.println(area);
//
//
//        //calculate boundary coordinate
//        Geometry g = p.getBoundary();
//        Coordinate[] new_co = g.getCoordinates();
//        for(Coordinate c: new_co){
//            System.out.println("("+c.x+","+c.y+")");
//        }
//
//        //calculate perimeter
//        double perimeter = p.getLength();
//        System.out.println(perimeter);


        Geometry g = p.getBoundary();
        Geometry rec_g = rec_p.getBoundary();
        Geometry union = g.union(rec_g);
        //Geometry ch = union.convexHull();
        Coordinate[] new_co = union.getCoordinates();
        for(Coordinate c: new_co){
            System.out.println("("+c.x+","+c.y+")");
        }

//        double[] rec_xPoints = {-94.50439453125,-93.07617187499999,-93.07617187499999,-94.50439453125,-94.50439453125};
//        double[] rec_yPoints = {44.99588261816546,44.99588261816546,45.81348649679973,45.81348649679973,44.99588261816546};
//
//        Coordinate[] rec_carr = new  Coordinate[5];
//        for(int i=0; i<5; i++) {
//            Coordinate co = new Coordinate(rec_xPoints[i], rec_yPoints[i]);
//            rec_carr[i] = co;
//        }
//
//        GeometryFactory rec_gf = new GeometryFactory();
//        Polygon rec_p = rec_gf.createPolygon(rec_carr);
//
//        Geometry rec_g = rec_p.getBoundary();
//        Geometry ch = rec_g.convexHull();
//        Coordinate[] coor = ch.getCoordinates();
//        for(Coordinate c: coor){
//            System.out.println("("+c.x+","+c.y+")");
//        }

    }

}
