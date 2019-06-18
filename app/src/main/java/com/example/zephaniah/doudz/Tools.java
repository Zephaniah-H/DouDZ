package com.example.zephaniah.doudz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Tools {

    public static Bitmap scaleImageByScreen (Bitmap bitmap, float w, float h)//w,h是想要设置的图片长宽
    {
        int width = bitmap.getWidth ();
        int height = bitmap.getHeight ();
        Matrix matrix = new Matrix ();
        float scaleWidth = w / (float) width;
        float scaleHeight = h / (float) height;

        matrix.preScale (scaleWidth, scaleHeight);
        return Bitmap.createBitmap (bitmap, 0, 0, width, height, matrix, true);
    }

    public static Matrix imageScaleTran(float sx, float sy, int wherex, int wherey){//将图片放缩再平移
        Matrix matrix = new Matrix();
        matrix.setScale(sx,sy);
        matrix.postTranslate(wherex,wherey);
        return matrix;
    }

    public static List<Bitmap> split(Bitmap bitmap, int xPiece, int yPiece){//切割图片
        List<Bitmap> allPieces = new ArrayList<Bitmap>(xPiece * yPiece);
        int pieceWidth = bitmap.getWidth() / xPiece;
        int pieceHeight = bitmap.getHeight() / yPiece;
        for (int i = 0 ;i < yPiece ;i++){
            for (int j = 0 ;j < xPiece ;j++){
                int x = j * pieceWidth;
                int y = i * pieceHeight;
                allPieces.add(Bitmap.createBitmap(bitmap,x,y,pieceWidth,pieceHeight));//xy是从哪里开始截的坐标，pw，ph是截的长宽
            }
        }
        return allPieces;
    }

    public static Bitmap Rotate180(Bitmap bm){//将图片旋转180°
        Matrix matrix = new Matrix();
        matrix.setRotate(180,bm.getWidth()/2,bm.getHeight()/2);
        Bitmap rotateBm = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,false);
        return rotateBm;
    }

    public static List<Rect> cutANDplace(Bitmap bitmap ,int x,int y,float left ,float top ){
        Rect src = new Rect();
        Rect dst = new Rect();
        List<Rect> twoRect = new ArrayList<Rect>(x * y * 2);
        int xpieceWidth = bitmap.getWidth()/x;
        int ypieceHeight = bitmap.getHeight()/y;
        for (int i = 0;i < y;i++){
            for (int j = 0;j < x;j++) {
                src.set(xpieceWidth * j, ypieceHeight * i, xpieceWidth * (j+1), ypieceHeight * (i+1));
                dst.set((int)left,(int)top,(int)(left+xpieceWidth),(int)(top+ypieceHeight));
                twoRect.add(src);
                twoRect.add(dst);
            }
        }
        return twoRect;

    }

    public static void playMusic(MediaPlayer mediaPlayer,Context context) throws IOException {

        mediaPlayer.prepare();
        if (mediaPlayer == null) {//如果当前播放音乐则停止播放，因为多个线程在运行，所以没有这个会有很多音乐重叠
            mediaPlayer.start();
            mediaPlayer.setLooping(true);

//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            public void onCompletion(MediaPlayer mp) {
//                //播完了接着播或者关闭mMediaPlayer
//                mp.start();
//            }
//        });
        }
    }
}


