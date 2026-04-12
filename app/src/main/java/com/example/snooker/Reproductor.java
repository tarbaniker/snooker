package com.example.snooker;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class Reproductor {
    public void reproduir(Context context, int raw, int duracio) {
        LogIt.i("MediaPlayer", "Creació");
        MediaPlayer mediaPlayer = MediaPlayer.create(context, raw);

        LogIt.i("MediaPlayer", "Start");
        mediaPlayer.start(); // no need to call prepare(); create() does that for you

        LogIt.i("MediaPlayer", "sleep while playing");
        try {
            Thread.sleep(duracio);
        } catch (Exception e) {
            Log.e("MediaPlayer", "error sleep", e);
        }

        LogIt.i("MediaPlayer", "Release");
        mediaPlayer.release();
        LogIt.i("MediaPlayer", "després del Release");

    }
}
