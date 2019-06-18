package com.example.zephaniah.doudz;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity{
    public static MediaPlayer gamingMusic1;
    public static MediaPlayer gamingMusic2;
    public static MediaPlayer gamingMusic3;
    public static MediaPlayer gamingMusic4;
    public static MediaPlayer gamingMusic5;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        GameView gameView = new GameView(this,this);
        setContentView(gameView);
        gamingMusic1 = MediaPlayer.create(this,R.raw.normal);
        gamingMusic2 = MediaPlayer.create(this,R.raw.normal2);
        gamingMusic3 = MediaPlayer.create(this,R.raw.exciting);
        gamingMusic4 = MediaPlayer.create(this,R.raw.win);
        gamingMusic5 = MediaPlayer.create(this,R.raw.lose);

        gamingMusic1.start();
        gamingMusic1.setLooping(true);
//        gamingMusic1.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
//            @Override
//            public void onCompletion(MediaPlayer mp){
//                if (!gamingMusic1.isPlaying()){
//                    System.out.println("normal播完了");
//                    gamingMusic2.start();
//                    gamingMusic2.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
//                        @Override
//                        public void onCompletion(MediaPlayer mp){
//                            if(!gamingMusic2.isPlaying()){
//                                System.out.println("normal2播完了");
//                                gamingMusic1.start();
//                            }
//                        }
//                    });
//                }
//            }
//        });

    }
    @Override
    protected void onDestroy(){
        if (gamingMusic1 != null) {
            gamingMusic1.stop();
            gamingMusic1.release();
        }
        if (gamingMusic2 != null) {
            gamingMusic2.stop();
            gamingMusic2.release();
        }
        if (gamingMusic3 != null) {
            gamingMusic3.stop();
            gamingMusic3.release();
        }
        if (gamingMusic4 != null) {
            gamingMusic4.stop();
            gamingMusic4.release();
        }
        if (gamingMusic5 != null) {
            gamingMusic5.stop();
            gamingMusic5.release();
        }
        if (OpeningVideoActivity.gamingSoundpool != null){
            OpeningVideoActivity.gamingSoundpool.release();
    }
        super.onDestroy();
    }
}
