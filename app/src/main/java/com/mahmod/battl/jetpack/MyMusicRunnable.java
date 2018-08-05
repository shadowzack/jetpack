package com.mahmod.battl.jetpack;


import android.content.Context;
import android.media.MediaPlayer;

import com.google.firebase.crash.FirebaseCrash;

import java.io.IOException;

public class MyMusicRunnable implements Runnable, MediaPlayer.OnCompletionListener {
    Context appContext;
    MediaPlayer mPlayer;
    float volume = 100;
    boolean musicIsPlaying = false;

    public MyMusicRunnable(Context c,int volume) {
        // be careful not to leak the activity context.
        // can keep the app context instead.
        this.volume = volume;
        appContext = c.getApplicationContext();
    }


    public boolean isMusicIsPlaying() {
        return musicIsPlaying;
    }

    /**
     * MediaPlayer.OnCompletionListener callback
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        // loop back - play again
        if (musicIsPlaying && mPlayer != null) {
            mPlayer.start();
        }
    }

    /**
     * toggles the music player state
     * called asynchronously every time the play/pause button is pressed
     */
    @Override
    public void run() {

        if (musicIsPlaying) {
            mPlayer.stop();
            musicIsPlaying = false;
        } else {
            if (mPlayer == null) {
                mPlayer = MediaPlayer.create(appContext, R.raw.menu);
                setVolume(this.volume);
                mPlayer.start();
                mPlayer.setOnCompletionListener(this); // MediaPlayer.OnCompletionListener
            } else {
                try {
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    FirebaseCrash.log("Caught an exception:music runnable prepare and start ");
                    FirebaseCrash.report(e);
                }
            }
            musicIsPlaying = true;
        }

    }

    public void setVolume(float value){
        this.volume = value;
        mPlayer.setVolume(value/100,value/100);
    }


}
