package com.example.zephaniah.doudz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class MenuView extends SurfaceView implements SurfaceHolder.Callback, OnTouchListener {


    SurfaceHolder holder;
    Canvas canvas;
    boolean threadFlag = true;
    Bitmap background;
    Bitmap startButton;
    Intent intent;
    MainActivity main;

    public MenuView(Context context, Intent intent, MainActivity main) {
        super(context);
        holder = getHolder();
        this.intent = intent;
        this.main = main;
        background = Tools.scaleImageByScreen(BitmapFactory.decodeResource(getResources(),R.drawable.bg),MainActivity.ScreenWidth,MainActivity.ScreenHeight);
        startButton = Tools.scaleImageByScreen(BitmapFactory.decodeResource(getResources(),R.drawable.startgameup),MainActivity.ScreenWidth/5,MainActivity.ScreenHeight/10);
        this.getHolder().addCallback(this);
        this.setOnTouchListener(this);
    }

    Thread menuThread = new Thread() {
        @Override
        @SuppressLint("WrongCall")
        public void run() {

            while (threadFlag) {
                try {
                    canvas = holder.lockCanvas();
                        onDraw(canvas);
                } catch (Exception e){
                } finally
                 {
                    holder.unlockCanvasAndPost(canvas);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    };

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        canvas.drawBitmap(background, 0, 0, paint);
        Matrix matrix = new Matrix();
        matrix.setTranslate((MainActivity.ScreenWidth-startButton.getWidth())/2,(MainActivity.ScreenHeight-startButton.getHeight())/2);
        canvas.drawBitmap(startButton,matrix,paint);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        threadFlag = true;
        menuThread.start();
        System.out.println("surfaceCreated");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
            threadFlag = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        System.out.println(event.getRawX() + "," + event.getRawY());
        int myAction = event.getAction();
        if (myAction == MotionEvent.ACTION_DOWN){
            int myX = (int)event.getRawX();
            int myY = (int)event.getRawY();
            if (myX >= (MainActivity.ScreenWidth-startButton.getWidth())/2 && myX <= (MainActivity.ScreenWidth-startButton.getWidth())/2+startButton.getWidth()
                   && myY >= (MainActivity.ScreenHeight-startButton.getHeight())/2 && myY <= (MainActivity.ScreenHeight-startButton.getHeight())/2+startButton.getHeight()){

                startButton = Tools.scaleImageByScreen(BitmapFactory.decodeResource(getResources(),R.drawable.startgameup),MainActivity.ScreenWidth*9/50,MainActivity.ScreenHeight*9/100);
            }
        }
        if (myAction == MotionEvent.ACTION_UP){
            int myX = (int)event.getRawX();
            int myY = (int)event.getRawY();
            if (myX >= (MainActivity.ScreenWidth-startButton.getWidth())/2 && myX <= (MainActivity.ScreenWidth-startButton.getWidth())/2+startButton.getWidth()
                    && myY >= (MainActivity.ScreenHeight-startButton.getHeight())/2 && myY <= (MainActivity.ScreenHeight-startButton.getHeight())/2+startButton.getHeight()){

                main.startActivity(intent);
                MainActivity.mediaPlayer.stop();
                MainActivity.mediaPlayer.release();
            }
            startButton = Tools.scaleImageByScreen(BitmapFactory.decodeResource(getResources(),R.drawable.startgameup),MainActivity.ScreenWidth/5,MainActivity.ScreenHeight/10);
        }
        return true;
    }

}
