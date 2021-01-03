package com.example.mylabyrinth2.Scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class HomeMenuScene implements Scene {

    private Rect r = new Rect();


    @Override
    public void update() {

    }

    @Override
    public void terminate() {

    }

    @Override
    public void receiveTouch(MotionEvent event) {
        //Depending on which scene is displayed
        if(event.getAction() == MotionEvent.ACTION_UP){
            SceneManager.ACTIVE_SCENE = 1;
        }

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        paint.setColor(Color.WHITE);
        paint.setTextSize(120);
        drawText(canvas,paint,"LABYRINTH",3.3f,2f);
        drawText(canvas,paint,"THE GAME",2.75f,2f);

        paint.setTextSize(60);
        paint.setColor(Color.WHITE);
        drawText(canvas,paint,"Click anywhere to start!",1.8f,2f);
    }

    private void drawText(Canvas canvas, Paint paint, String text, float yDivision, float xDivision){
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text,0,text.length(),r);
        float x = cWidth / xDivision - r.width() / xDivision - r.left;
        float y = cHeight / yDivision + r.height() / 2.5f - r.bottom;
        canvas.drawText(text, x , y, paint);
    }

}
