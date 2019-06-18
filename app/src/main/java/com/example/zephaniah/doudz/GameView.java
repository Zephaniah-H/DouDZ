package com.example.zephaniah.doudz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, OnTouchListener {
    GameActivity gameActivity;
    boolean threadFlag=true;
    Desk desk;
    SurfaceHolder holder;
    Canvas canvas;

    Bitmap gameBackGround;
    Thread gameThread = new Thread() {
        @Override
        @SuppressLint("WrongCall")
        public void run() {
//            int time = 0;
            holder=getHolder();
            synchronized (this) {
                while (threadFlag) {
                    desk.gameLogic();
                    try {
                        canvas = holder.lockCanvas();
                        onDraw(canvas);
                    } finally {
                        holder.unlockCanvasAndPost(canvas);
                    }
                    try {
                        Thread.sleep(100);
//                    time += 100;
//                    System.out.println("gameview,,"+time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    };

    public GameView(Context context,GameActivity gameActivity) {
        super(context);
        this.gameActivity = gameActivity;
        desk=new Desk(gameActivity);
        gameBackGround=Tools.scaleImageByScreen(BitmapFactory.decodeResource(getResources(), R.drawable.gamebg),MainActivity.ScreenWidth,MainActivity.ScreenHeight);
        this.getHolder().addCallback(this);
        this.setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(gameBackGround, 0, 0, null);
        desk.paint(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        threadFlag=true;
        gameThread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this) {
            threadFlag = false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            desk.onTuch(v, event);
        }
        return true;
    }

}
