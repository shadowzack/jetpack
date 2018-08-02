package com.mahmod.battl.jetpack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;



public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;

    private Player player;
    private Point playerPoint;
    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread= new MainThread(getHolder(),this);
        player=  new Player(new Rect(100,100,200,200), Color.BLUE);
        playerPoint=new Point(150,150);

        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread=new MainThread(getHolder(),this);
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry =true;
        while (true){
            try {
                thread.setRunning(false);
                thread.join();
            }catch(Exception e){
                e.printStackTrace();
            }
            retry=false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                playerPoint.set((int) event.getX(),(int) event.getY());
        }
        return true;
    }
    public void update(){
       player.update(playerPoint);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        player.draw(canvas);
    }
}
