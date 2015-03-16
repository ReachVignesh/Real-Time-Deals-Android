package com.example.conor.senan.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A model used to hold a list of ads that are nearby.
 */
public class NearbyModel extends SimpleObservable<NearbyModel>{

    private List<SellerAdModel> points;

    public NearbyModel(){

        points = new ArrayList<SellerAdModel>();

    }

    public List<SellerAdModel> getPoints(){
        return points;
    }


    public void setPoints(List<SellerAdModel> points){


        while (this.points.size() > 0) {
            this.points.remove(0);
        }
        for (SellerAdModel point : points) {
            this.points.add(point);
        }

        notifyListeners(this);


    }




}
