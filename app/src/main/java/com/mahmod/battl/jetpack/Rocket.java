package com.mahmod.battl.jetpack;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Rocket extends GameObject {
    private int score;
    private int speed;
    private Random rand = new Random();
    private FramesAnimation framesAnimation = new FramesAnimation();
    private Bitmap bitmap;

    public Rocket(Bitmap img, int x, int y, int w, int h, int s, int numberOfFrames) {
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;
        //with better score the game becomes harder
        speed = 11 + (int) (rand.nextDouble()*score / 30);

        //cap asteroid speed
        if(speed > 18) {
            speed = 18;
        }

        Bitmap[] image = new Bitmap[numberOfFrames];
        bitmap = img;

        for(int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(bitmap, 0, i*height, width, height*2);
        }

        framesAnimation.setFrames(image);
        framesAnimation.setDelay(10000-speed);
    }

    public void update() {
        x -= speed;
        framesAnimation.update();
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(framesAnimation.getImage(), x, y, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        //offset slightly more, for realistic cllision detection
        return (width - 10);
    }
}
