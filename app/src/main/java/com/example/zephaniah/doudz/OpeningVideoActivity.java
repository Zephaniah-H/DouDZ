package com.example.zephaniah.doudz;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import java.util.Map;

public class OpeningVideoActivity extends AppCompatActivity{
    public VideoPlayView videoView;
    public static SoundPool gamingSoundpool;
    public static Map<String,Integer> poolMap;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.opening_vedio);

        videoView = (VideoPlayView) findViewById(R.id.videoview);
        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.cardchessgame));
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent intent = new Intent(OpeningVideoActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        gamingSoundpool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        poolMap = GameSound.getPoolMap(this,gamingSoundpool);
        gamingSoundpool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                System.out.println("音乐加载完了！");
            }
        });
    }

}
