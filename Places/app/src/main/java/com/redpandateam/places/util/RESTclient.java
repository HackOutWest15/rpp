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
import com.redpandateam.places.model.SongPlace;

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

    ArrayList<SongPlace> songPlaces;

    public RESTclient(Context ctx){

        queue = Volley.newRequestQueue(ctx);
        this.url = "http://localhost:9999";
        songPlaces = new ArrayList<SongPlace>();
    }

    public ArrayList<SongPlace> getSongPlaces() {

        JsonArrayRequest arrayRequest = new JsonArrayRequest(url + "/songplaces", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                JSONObject jo;
                songPlaces.clear();

                for(int i = 0; i < response.length(); i++){

                    try {
                        jo = (JSONObject)response.get(i);
                        double lat = (double)jo.getDouble("PlaceLat");
                        double lon = (double)jo.getDouble("PlaceLong");
                        int likes = (int)jo.getInt("Likes");
                        String songID = jo.getString("SpotifyID");
                        String album = jo.getString("Album");
                        String title = jo.getString("Title");
                        String artist = jo.getString("Artist");

                        SongPlace sp = new SongPlace(lat, lon, songID, title, artist, album, likes);
                        songPlaces.add(sp);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

        }, null);

        queue.add(arrayRequest);

        return songPlaces;
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

        JsonObjectRequest jObjR = new JsonObjectRequest(Request.Method.POST, url + "/songplace", jo,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response){

                    }
                }, null);

        queue.add(jObjR);

    }

    public void likeSong(String user, String songID, double lon, double lat){
        JSONObject jo = new JSONObject();
        try {
            jo.put("UserID", user);
            jo.put("SpotifyID", songID);
            jo.put("lat", lat);
            jo.put("long", lon);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jObjR = new JsonObjectRequest(Request.Method.POST, url + "/songplace/like", jo,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response){

                    }
                }, null);

        queue.add(jObjR);
    }



}

