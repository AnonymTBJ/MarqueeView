package com.anonymtbj.tools.marqueeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Anonym on 2018/2/6.
 */

public class MarqueeView extends View{

    private Paint paint;
    private boolean isInit,isMobile,isBlink,isBitmapDisplay;
    private int width ,height ,position;
    private int textSize ,textColor ,backdropColor ,speed;
    private String text[];
    private MarqueeDirection direction;
    private MarqueeAlign align;
    private Bitmap bitmap;
    private Thread thread;

    public enum MarqueeDirection { LEFT, RIGHT }
    public enum MarqueeAlign { LEFT, RIGHT ,CENTER}

    public MarqueeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.text = new String[]{"MarqueeView","MarqueeView"};
        this.textSize = 100;
        this.textColor = Color.BLACK;
        this.backdropColor = Color.WHITE;
        this.speed = 1;
        this.direction = MarqueeDirection.LEFT;
        this.isMobile = true;
        this.isInit = false;
        this.isBlink = false;
        this.isBitmapDisplay = false;
        this.bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        canvas.drawColor(backdropColor);
        if(!isInit){
            setAlign(MarqueeAlign.CENTER);
            isInit = true;
        }
        canvas.translate(position,height / 2);
        if(isBitmapDisplay){
            int residual = (int) ((paint.measureText(text[0])/2)-(bitmap.getWidth()/2));
            if(!text[1].equals("")){
                canvas.drawBitmap(bitmap,residual,-(bitmap.getHeight()/2),null);
            }
            canvas.drawText(text[1],0,paint.getTextSize()/3f,paint);
            onMobile((int) paint.measureText(text[0]),bitmap.getWidth(),residual);
        }else{
            canvas.drawText(text[1],0,paint.getTextSize()/3f,paint);
            onMobile((int) paint.measureText(text[0]),0,0);
        }
    }

    private void onMobile(int textLength,int bitmapSize,int residual){
        if(isMobile){
            switch (direction){
                case LEFT:
                    position += speed;
                    if(textLength > bitmapSize){
                        if(position > width){
                            position = -textLength;
                        }
                    }else{
                        if(position > (width + (0-residual))){
                            position = -(textLength + (0-residual));
                        }
                    }
                    break;
                case RIGHT:
                    position -= speed;
                    if(textLength > bitmapSize){
                        if(position < -textLength){
                            position = width;
                        }
                    }else{
                        if(position < -(textLength + (0-residual))){
                            position = (width + (0-residual));
                        }
                    }
                    break;
            }
        }
        invalidate();
    }

    public void setTextSize(int textSize){
        this.textSize = textSize;
    }

    public int getTextSize(){
        return this.textSize;
    }

    public void setTextColor(int textColor){
        this.textColor = textColor;
    }

    public int getTextColor(){
        return this.textColor;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public int getSpeed(){
        return this.speed;
    }

    public void setText(String text){
        this.text[0] = text;
        this.text[1] = text;
    }

    public String getText(){
        return this.text[0];
    }

    public void setBackdropColor(int backdropColor){
        this.backdropColor = backdropColor;
    }

    public int getBackdropColor(){
        return this.backdropColor;
    }

    public void setDirection (MarqueeDirection direction){
        this.direction = direction;
    }

    public MarqueeDirection getDirection(){
        return this.direction;
    }

    public void setMobile(boolean isMobile){
        this.isMobile = isMobile;
    }

    public boolean isMobile(){
        return this.isMobile;
    }

    public boolean isBlink(){
        return this.isBlink;
    }

    public void startBlink(int blinkSpeed){
        if(thread != null){
            thread.interrupt();
            thread = null;
        }
        blinkRun(blinkSpeed);
        isBlink = true;
    }

    public void stopBlink(){
        if(isBlink){
            if(thread != null){
                thread.interrupt();
                thread = null;
            }
            text[1] = text[0];
            isBlink = false;
        }
    }

    private void blinkRun(final int blinkSpeed){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (true){
                        if(text[1].equals("")){
                            text[1] = text[0];
                        }else{
                            text[1] = "";
                        }
                        Thread.sleep(blinkSpeed);
                    }
                }catch (Exception e){}
            }
        });
        thread.start();
    }

    public void setAlign(MarqueeAlign align){
        int number = (int) paint.measureText(text[0]);
        this.align = align;
        switch (align){
            case RIGHT:
                position = (width-number);
                break;
            case LEFT:
                position = 0;
                break;
            case CENTER:
                position = ((width/2) - (number/2));
                break;
        }
    }

    public MarqueeAlign getAlign(){
        return this.align;
    }

    public void setBitmap (Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap(){
        return this.bitmap;
    }

    public boolean isBitmapDisplay(){
        return this.isBitmapDisplay;
    }

    public void setBitmapDisplay(boolean isBitmapDisplay){
        this.isBitmapDisplay = isBitmapDisplay;
    }

}
