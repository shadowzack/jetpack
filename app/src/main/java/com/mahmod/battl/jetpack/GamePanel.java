package com.mahmod.battl.jetpack;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.Random;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;
    private BackGround backGround;
    private Player player;
    private GameThread thread;
    private Point playerPoint;
    private Random rand = new Random();
    private boolean newGameCreated;

    //TODO:make progeress add best score
    //this will help use multiple difficulties
    private int progress = 20;
    private int best; //best score


    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread= new GameThread(getHolder(),this);
       //player=  new Player(new Rect(100,100,200,200), Color.BLUE);
        playerPoint=new Point(150,150);

        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        backGround = new BackGround(BitmapFactory.decodeResource(getResources(), R.drawable.bg));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.jet), 99, 66, 3);
        thread=new GameThread(getHolder(),this);
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;

            }catch(InterruptedException e){e.printStackTrace();}

        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!player.getPlaying())
                {
                    player.setPlaying(true);
                    player.setUp(true);
                }
                else
                {
                    player.setUp(true);
                }
                return true;
            case MotionEvent.ACTION_UP:
                player.setUp(false);
                return true;
                //playerPoint.set((int) event.getX(),(int) event.getY());
        default:
            return super.onTouchEvent(event);
        }
    }
    public void update(){
       //player.update(playerPoint);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        player.draw(canvas);
    }
}
