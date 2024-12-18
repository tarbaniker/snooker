package com.example.snooker;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class Reproductor {
    public void reproduir(Context context, int raw, int duracio) {
        Log.i("MediaPlayer", "Creació");
        MediaPlayer mediaPlayer = MediaPlayer.create(context, raw);

        Log.i("MediaPlayer", "Start");
        mediaPlayer.start(); // no need to call prepare(); create() does that for you

        Log.i("MediaPlayer", "sleep while playing");
        try {
            Thread.sleep(duracio);
        } catch (Exception e) {
            Log.e("MediaPlayer", "error sleep", e);
        };

        Log.i("MediaPlayer", "Release");
        mediaPlayer.release();
        Log.i("MediaPlayer", "després del Release");

    }
}
