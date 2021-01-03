package com.example.mylabyrinth2.Objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Labyrinth implements GameObject {
    private Rect rectangle;
    private int color;


    public Labyrinth(int color,int left, int top, int right, int bottom) {
        this.color = color;
        this.rectangle = new Rect(left,top,right,bottom);
    }

    public Rect getRectangle(){
        return rectangle;
    }

    public boolean playerCollide(RectPlayer player){
        return Rect.intersects(rectangle, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle,paint);
    }

    @Override
    public void update() {

    }
}
