package com.mahmod.battl.jetpack;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle implements GameObject {
    private Rect rect;
    private Rect rect2;
    private int color;

    public Rect getRectangle() {
        return rect;
    }

    public boolean playerCollide(Player player){


        //return Rect.intersects(rect,player.getRectangle()) ||
              //  Rect.intersects(rect2,player.getRectangle());

         if(rect.contains(player.getRectangle().left,player.getRectangle().top)
                 ||rect.contains(player.getRectangle().right,player.getRectangle().top)
                 ||rect.contains(player.getRectangle().left,player.getRectangle().bottom)
                 ||rect.contains(player.getRectangle().right,player.getRectangle().bottom))
             return true;
         return false;

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rect,paint);
        canvas.drawRect(rect2,paint);
    }

    @Override
    public void update() {

    }
}
