package com.mahmod.battl.jetpack;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Random;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;
    private long missileStartTime;
    private GameThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Rocket> rockets;
    private Random rand = new Random();
    private int maxBorderHeight;
    private int minBorderHeight;
    private boolean newGameCreated;
    private int progress = 20; //progression
    private int best;
    private SharedPreferencesManager prefManager;


    public GamePanel(Context context)
    {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        this.prefManager = new SharedPreferencesManager(getContext());
        this.best = prefManager.get(SharedPreferencesManager.PREF_BEST_SCORE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.jet), 99, 66, 3);
        rockets = new ArrayList<Rocket>();
        missileStartTime = System.nanoTime();
        thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
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
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
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
        }
        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            player.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update()

    {
        if(player.getPlaying()) {

            bg.update();
            player.update();

            //add missiles on timer
            long missileElapsed = (System.nanoTime()-missileStartTime)/1000000;
            if(missileElapsed >(2000 - player.getScore()/4)){

                //first missile always goes down the middle
                if(rockets.size()==0)
                {
                    rockets.add(new Rocket(BitmapFactory.decodeResource(getResources(),R.drawable.
                            missile),WIDTH + 10, HEIGHT/2, 45, 15, player.getScore(), 13));
                }
                else
                {

                    rockets.add(new Rocket(BitmapFactory.decodeResource(getResources(),R.drawable.missile),
                            WIDTH+10, (int)(rand.nextDouble()*(HEIGHT - (maxBorderHeight * 2))+maxBorderHeight),45,15, player.getScore(),13));
                }

                //reset timer
                missileStartTime = System.nanoTime();
            }

            if(player.getY() <= -60 || player.getY() >= 500) {
                System.out.println("The player crossed the borders" + player.getY());
                try {
                    Thread.sleep(750);
                }

                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finally {
                    player.setPlaying(false);
                }
            }

            //loop through every missile and check collision and remove
            for(int i = 0; i< rockets.size();i++)
            {
                //update missile
                rockets.get(i).update();

                if(collision(rockets.get(i),player))
                {
                    try {
                        Thread.sleep(750);
                    }

                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    finally {
                        rockets.remove(i);
                        player.setPlaying(false);
                    }
//                    break;
                }
                //remove missile if it is way off the screen
                if(rockets.get(i).getX()<-100)
                {
                    rockets.remove(i);
                    break;
                }
            }
        }
        else{
            newGameCreated = false;
            if(!newGameCreated) {
                newGame();
            }
        }
    }
    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }

        return false;
    }
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            player.draw(canvas);

            //draw missiles
            for(Rocket m: rockets)
            {
                m.draw(canvas);
            }

            if(player.getScore() > best) {
                final int currentScore = player.getScore();
                final int oldBest = (best + 100);
                if(currentScore < oldBest && best != 0)
                    drawMessage(canvas, "New HeightScore!");
            }

            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public void newGame()
    {
        if(player.getScore() > best) {
            try {
                best = player.getScore();
                prefManager.add(SharedPreferencesManager.PREF_BEST_SCORE, best);
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        }

        rockets.clear();

        minBorderHeight = 5;
        maxBorderHeight = 30;

        player.resetDYA();
        player.resetScore();
        player.setY(HEIGHT/2);

        newGameCreated = true;
    }

    public void drawText(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        canvas.drawText("DISTANCE: " + player.getScore(), 10, HEIGHT - 10, paint);
        canvas.drawText("BEST: " + best, WIDTH - 215, HEIGHT - 10, paint);

        if(!player.getPlaying())
        {
            Paint paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("JETPACK", WIDTH / 2 - 50, HEIGHT / 2, paint1);

            Paint paint2 = new Paint();
            paint2.setColor(Color.WHITE);
            paint2.setTextSize(20);
            paint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH/2-50, HEIGHT/2 + 30, paint2);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2-50, HEIGHT/2 + 60, paint2);
        }
    }

    public void drawMessage(Canvas canvas, String message) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        canvas.drawText(message, (WIDTH / 2) - 120, (HEIGHT / 2) - 207, paint);
    }
}
