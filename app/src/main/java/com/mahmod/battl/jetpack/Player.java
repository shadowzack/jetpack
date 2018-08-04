package com.mahmod.battl.jetpack;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player extends  GameObject {
    private Bitmap bitmap;
    private int score;
    private boolean up;
    private boolean playing;
    private FramesAnimation framesAnimation = new FramesAnimation();
    private long startTime;

    public Player(Bitmap image, int w, int h, int numberOfFrames) {
        x = 100;
        y = GamePanel.HEIGHT/2;
        dy = 0;
        score = 0;
        this.height = h;
        this.width = w;

        Bitmap[] playerImage = new Bitmap[numberOfFrames];
        bitmap = image;

        for(int i = 0; i < playerImage.length; i++) {
            playerImage[i] = Bitmap.createBitmap(bitmap, 200, 0, width, height);
        }

        framesAnimation.setFrames(playerImage);
        framesAnimation.setDelay(100000);
        startTime = System.nanoTime();
    }
    //getters
    public int getScore() {
        return (this.score * 3);
    }
    public boolean getPlaying() {
        return this.playing;
    }

    //setters
    public void setScore(int score) {
        this.score = score;
    }
    public void setUp(boolean up) {
        this.up = up;
    }
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }


    public void resetDYA() {
        dy = 0;
    }
    public void resetScore() {
        this.score = 0;
    }


    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > 100) {
            score++;
            startTime = System.nanoTime();
        }

        framesAnimation.update();

        if(up) {
            dy -= 1;
        } else {
            dy += 1;
        }

        if(dy > 14) {
            dy = 14;
        }

        if(dy < -14) {
            dy = -14;
        }

        y += dy*0.4;
    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(framesAnimation.getImage(), x, y, null);
    }

}
