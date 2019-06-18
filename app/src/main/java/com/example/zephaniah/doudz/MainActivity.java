package com.example.zephaniah.doudz;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.util.Map;

public class MainActivity extends Activity {

    public static int ScreenWidth;
    public static int ScreenHeight;
    public static MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ScreenWidth = displayMetrics.widthPixels;
        ScreenHeight = displayMetrics.heightPixels;

        Intent intent = new Intent(MainActivity.this,GameActivity.class);
        MenuView menuView = new MenuView(this,intent,this);
        setContentView(menuView);
        mediaPlayer = MediaPlayer.create(this,R.raw.welcome);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

    }

}