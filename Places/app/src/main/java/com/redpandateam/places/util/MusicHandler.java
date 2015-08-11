package com.redpandateam.places.util;

import com.spotify.sdk.android.player.Player;

public class MusicHandler {

    private static MyBroadcastReceiver receiver = new MyBroadcastReceiver();
    private Player myPlayer;
    public void playSong(){
        myPlayer.play(receiver.getSongId());
    }

}
