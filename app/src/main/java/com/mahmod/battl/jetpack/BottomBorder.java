package com.mahmod.battl.jetpack;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BottomBorder extends GameObject {
    private Bitmap image;

    public BottomBorder(Bitmap img, int x, int y) {
        height = 200;
        width = 20;

        this.x = x;
        this.y = y;

        //borders will move at the same speed as background
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
        }
    }
}
