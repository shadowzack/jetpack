package com.mahmod.battl.jetpack;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Player extends GameCoordinates implements GameObject {
    private Bitmap bitmap;
    private int score;
    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;


    private Rect rect;
    private int color;

    public Rect getRectangle(){
        return rect;
    }

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
            playerImage[i] = Bitmap.createBitmap(bitmap, i*width, 0, width, height);
        }

        animation.setFrames(playerImage);
        animation.setDelay(10);
        startTime = System.nanoTime();
    }

    //setters
    public void setUp(boolean up) {
        this.up = up;
    }
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void resetDY() {
        dy = 0;
    }
    public void resetScore() {
        this.score = 0;
    }
    //getters
    public int getScore() {
        return (this.score * 3);
    }
    public void setScore(int score) {
        this.score = score;
    }
    public boolean getPlaying() {
        return this.playing;
    }


    @Override
    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > 100) {
            score++;
            startTime = System.nanoTime();
        }

        animation.update();

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

    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(animation.getImage(), x, y, null);
    }



   /* public void update(Point point){
        rect.set(point.x - rect.width()/2,point.y - rect.height()/2,point.x + rect.width()/2,point.y + rect.height()/2);
    }*/
}
