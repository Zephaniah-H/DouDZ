package com.example.zephaniah.doudz;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class VideoPlayView extends VideoView {

    public VideoPlayView(Context context) {
        super(context);
    }

    public VideoPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //重新计算宽高度
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

}