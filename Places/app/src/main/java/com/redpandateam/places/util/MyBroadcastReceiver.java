package com.redpandateam.places.util;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.redpandateam.places.MapsActivity;
import com.redpandateam.places.model.SongPlace;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private String trackId;
    static final class BroadcastTypes {
        static final String SPOTIFY_PACKAGE = "com.spotify.music";
        static final String PLAYBACK_STATE_CHANGED = SPOTIFY_PACKAGE + ".playbackstatechanged";
        static final String QUEUE_CHANGED = SPOTIFY_PACKAGE + ".queuechanged";
        static final String METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged";
    }
    public SongPlace songPlace = new SongPlace(0,0,null,null,null,null,0);
    @Override
    public void onReceive(Context context, Intent intent) {
        // This is sent with all broadcasts, regardless of type. The value is taken from
        // System.currentTimeMillis(), which you can compare to in order to determine how
        // old the event is.
        long timeSentInMs = intent.getLongExtra("timeSent", 0L);

        String action = intent.getAction();

        if (action.equals(BroadcastTypes.METADATA_CHANGED)) {
            String id = intent.getStringExtra("id");
            System.out.println(id);
            setSongId(id);

            String artistName = intent.getStringExtra("artist");
            String albumName = intent.getStringExtra("album");
            String trackName = intent.getStringExtra("track");
            //updateCurrentTrack(id, artistName, albumName,trackName);

            RESTclient.getInstance().setTrack(trackName);
            int trackLengthInSec = intent.getIntExtra("length", 0);
            // Do something with extracted information...
        } else if (action.equals(BroadcastTypes.PLAYBACK_STATE_CHANGED)) {
            boolean playing = intent.getBooleanExtra("playing", false);
            int positionInMs = intent.getIntExtra("playbackPosition", 0);
            // Do something with extracted information
        } else if (action.equals(BroadcastTypes.QUEUE_CHANGED)) {
            // Sent only as a notification, your app may want to respond accordingly.
        }
    }

    public void setSongId(String id){
        trackId = id;
    }

    public String getSongId(){
        try{
            return trackId;
        }catch (NullPointerException e ){

        }
        return "Hejsan";
    }
}