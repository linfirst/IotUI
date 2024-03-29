package com.linfirst.temperatureview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class BrokenLineView extends View {
    private static final String TAG = "TemperatureView";

    private int minValue;
    private int maxValue;
    private int currentValue;
    private int lastValue;
    private int nextValue;
    private Paint mPaint;
    private int viewHeight;
    private int viewWidth;
    private int pointX;
    private int pointY;
    private boolean isDrawLeftLine;
    private boolean isDrawRightLine;
    private int pointTopY = (int) (40 * Util.getDensity(getContext()));
    private int pointBottomY = (int) (200 * Util.getDensity(getContext()));
    private int mMiddleValue;

    public BrokenLineView(Context context) {
        super(context);
    }

    public BrokenLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BrokenLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //设置最小值
    public void setMinValue(int minValue){
        this.minValue = minValue;
    }

    //设置最大值
    public void setMaxValue(int maxValue){
        this.maxValue = maxValue;
    }

    //设置目前的值
    public void setCurrentValue(int currentValue){
        this.currentValue = currentValue;
    }

    //设置是否画左边线段(只有第一个View是false)
    public void setDrawLeftLine(boolean isDrawLeftLine){
        this.isDrawLeftLine = isDrawLeftLine;
    }

    //设置是否画右边线段(只有最后一个View是false)
    public void setDrawRightLine(boolean isDrawRightLine){
        this.isDrawRightLine = isDrawRightLine;
    }

    //设置之前温度点的值
    public void setLastValue(int lastValue){
        this.lastValue = lastValue;
    }

    //设置下一个温度点的值
    public void setNextValue(int nextValue){
        this.nextValue = nextValue;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        //获取模式，一共有三种模式
//        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
//
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        //获取模式
//        int heightModel = MeasureSpec.getMode(heightMeasureSpec);
//
//        int size = 0;
//        //如果是 UNSPECIFIED 模式
//        //UNSPECIFIED：将视图按照自己的意愿设置成任意的大小，没有任何限制。
//        if (widthModel == MeasureSpec.UNSPECIFIED) {
//            size = heightSize;
//        } else if (heightModel == MeasureSpec.UNSPECIFIED) {
//            size = widthSize;
//        } else {
//            size = Math.min(widthSize, heightSize);
//        }
//        //告诉父布局在三种模式下需要的尺寸
//        setMeasuredDimension(size, size);
        //给一个初始长、宽
        int mDefaultWidth = 200;
        int mDefaultHeight = (int) (220 * Util.getDensity(getContext()));
        setMeasuredDimension(resolveSize(mDefaultWidth,widthMeasureSpec),resolveSize(mDefaultHeight,heightMeasureSpec));
        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();
        pointX = viewWidth / 2;
        Log.d(TAG, "onMeasure: " + resolveSize(mDefaultHeight,heightMeasureSpec));
    }



    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mMiddleValue = (pointTopY + pointBottomY) / 2;
        pointY = mMiddleValue + (int) ((pointBottomY-pointTopY) * 1f / (maxValue - minValue) * ((maxValue + minValue) / 2 - currentValue));

        Log.d(TAG, "onDraw: " + pointY);
        mPaint = new Paint();

        drawGraph(canvas);
        drawValue(canvas);
        drawPoint(canvas);
    }

    //绘制数值
    private void drawValue(Canvas canvas){
        mPaint.setTextSize(70);
        setTextColor();
        mPaint.setStrokeWidth(12);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(currentValue+"",pointX , pointY - 20, mPaint);
    }

    //设置字体颜色
    public void setTextColor(){
        if(currentValue <= 10 && currentValue >= 0){
            mPaint.setColor(Color.BLUE);
        }else if(currentValue <= 20 && currentValue > 10){
            mPaint.setColor(Color.GREEN);
        }else if(currentValue <= 30 && currentValue > 20){
            mPaint.setColor(0xFFFF8000);
        }else if(currentValue <= 40 && currentValue > 30){
            mPaint.setColor(Color.RED);
        }
    }

    //绘制温度点

    public void drawPoint(Canvas canvas){
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(pointX, pointY, 6, mPaint);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(pointX, pointY, 6, mPaint);
    }

    //绘制线段（线段组成折线）
    public void drawGraph(Canvas canvas){
//        mPaint.setColor(0xFF24C3F1);
        mPaint.setColor(0xffffffff);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(9);
        mPaint.setDither(true);   //设置防抖动
        mPaint.setStyle(Paint.Style.STROKE);   //设置 View 的样式
        mPaint.setAntiAlias(true);    //设置抗锯齿

        //判断是否画左线段（第一个View不用，其他全要）
        if(isDrawLeftLine){
            float middleValue = currentValue - (currentValue - lastValue) / 2f;

            float middleY = mMiddleValue + (int) ((pointBottomY-pointTopY) * 1f / (maxValue - minValue) * ((maxValue + minValue) / 2 - middleValue));
            canvas.drawLine(0-4, middleY-(4*(pointY-middleY)/pointX), pointX, pointY, mPaint);
        }

        //判断是否画右线段（最后View不用，其他全要）
        if(isDrawRightLine){
            float middleValue = currentValue - (currentValue - nextValue) / 2f;
            float middleY = mMiddleValue + (int) ((pointBottomY-pointTopY) * 1f / (maxValue - minValue) * ((maxValue + minValue) / 2 - middleValue));
            canvas.drawLine(pointX, pointY, viewWidth+4, middleY+(4*(middleY-pointY)/(viewWidth-pointX)), mPaint);
        }
    }
}
