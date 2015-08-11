package com.redpandateam.places.model;

import java.util.ArrayList;

/**
 * Created by bjornlexell on 11/08/15.
 */
public class Coordinates {

    private double lat, lon;
    private ArrayList<String> songs;

    public Coordinates(double lat, double lon){
        this.lat = lat;
        this.lon = lon;
        this.songs = new ArrayList<String>();

    }

    public ArrayList<String> getSongs(){
        return this.songs;
    }

    public void addSong(String id){
        songs.add(id);
    }

    public double getLat(){
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }
}
