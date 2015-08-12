package com.redpandateam.places.model;

import java.util.ArrayList;

/**
 * Created by bjornlexell on 12/08/15.
 */
public class SongPlacesContainer {

    private static SongPlacesContainer instance = null;
    private ArrayList<SongPlace> sps;

    private SongPlacesContainer(){
        sps = new ArrayList<SongPlace>();
    }

    public static SongPlacesContainer getInstance(){
        if(instance == null){
            instance = new SongPlacesContainer();
        }

        return instance;
    }

    public ArrayList<SongPlace> getSongPlacesArray(){
        return sps;
    }
}
