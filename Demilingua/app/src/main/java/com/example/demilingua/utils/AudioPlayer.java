package com.example.demilingua.utils;

import android.content.Context;
import android.media.MediaPlayer;
import javax.inject.Inject;
import javax.inject.Singleton;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class AudioPlayer {
    private final Context context;
    private MediaPlayer mediaPlayer;

    @Inject
    public AudioPlayer(@ApplicationContext Context context) {
        this.context = context;
    }

    public void playFromRaw(String fileName) {
        stop(); // Detener reproducción previa si existe

        int resId = context.getResources().getIdentifier(fileName, "raw", context.getPackageName());
        if (resId != 0) {
            mediaPlayer = MediaPlayer.create(context, resId);
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(mp -> stop());
                mediaPlayer.start();
            }
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}