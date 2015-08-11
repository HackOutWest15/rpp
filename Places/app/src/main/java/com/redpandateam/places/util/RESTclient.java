package com.redpandateam.places.util;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.redpandateam.places.model.Coordinates;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bjornlexell on 11/08/15.
 */
public class RESTclient {

    String url;
    RequestQueue queue;

    ArrayList<Coordinates> coordinates;

    public RESTclient(Context ctx){

        queue = Volley.newRequestQueue(ctx);
        this.url = "http://localhost:9999";
        coordinates = new ArrayList<Coordinates>();
    }

    public ArrayList<Coordinates> getSongPlaces() {

        JsonArrayRequest arrayRequest = new JsonArrayRequest(url + "/songplaces", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                JSONObject jo;

                for(int i = 0; i < response.length(); i++){

                    try {
                        jo = (JSONObject)response.get(i);

                        for(int j = 0; j < coordinates.size(); j++){

                            Coordinates c = coordinates.get(j);
                            double lat = (double)jo.getDouble("PlaceLat");
                            double lon = (double)jo.getDouble("PlaceLong");

                            String id = jo.getString("SpotifyID");
                            //om koordinaterna redan finns, lägg till låt-id i lista
                            if((lat == c.getLat() && lon == c.getLon())){

                                c.getSongs().add(id);

                            //annars skapa ny koordinater och lägg till i listan, plus låt
                            }else{
                                Coordinates newC = new Coordinates(lat, lon);
                                newC.getSongs().add(id);
                                coordinates.add(newC);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

        }, null);

        queue.add(arrayRequest);

        return coordinates;
    }


    public void addSongPlace(String user, String songID, double lon, double lat){

        JSONObject jo = new JSONObject();
        try {
            jo.put("UserID", user);
            jo.put("SpotifyID", songID);
            jo.put("lat", lat);
            jo.put("long", lon);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jObjR = new JsonObjectRequest(Request.Method.POST, url + "/songplace",
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response){

                    }
                }, null);

        queue.add(jObjR);

    }


    
}

