package com.mahmod.battl.jetpack;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.firebase.crash.FirebaseCrash;

public class TopBorder extends GameObject {
    private Bitmap image;

    public TopBorder(Bitmap img, int x, int y, int h) {
        height = h;
        width = 20;

        this.x = x;
        this.y = y;

        //Borders will move at the same speed as the background.
        dx = GamePanel.MOVESPEED;
        image = Bitmap.createBitmap(img, 0, 0, width, height);
    }

    public void update() {
        x += dx;
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(image, x, y, null);
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.log("Caught an exception:TopBorder drawBitMap ");
            FirebaseCrash.report(e);
        }
    }
}
