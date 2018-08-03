package com.mahmod.battl.jetpack;

import android.graphics.Bitmap;

public class Animation {
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;

    //setters
    public void setFrames(Bitmap[] frames) {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }
    public void setDelay(long delay) {
        this.delay = delay;
    }
    public void setFrame(int i) {
        currentFrame = i;
    }
    //getters
    public Bitmap getImage() {
        return frames[currentFrame];
    }
    public int getFrame() {
        return currentFrame;
    }
    public boolean hasPlayed() {
        return playedOnce;
    }


    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;

        if(elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }

        if(currentFrame == frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }
    }


}
