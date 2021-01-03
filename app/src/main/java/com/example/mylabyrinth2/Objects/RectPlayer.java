package com.example.mylabyrinth2.Objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.mylabyrinth2.Animation;
import com.example.mylabyrinth2.Constants;
import com.example.mylabyrinth2.R;

public class RectPlayer implements GameObject {

    private Rect rectangle;
    private int color;

    private Animation animation;

    public RectPlayer(Rect rectangle,int color){
        this.rectangle = rectangle;
        this.color = color;

        BitmapFactory bf = new BitmapFactory();
        Bitmap idleImg = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.slimeblock);

        animation = new Animation(new Bitmap[]{idleImg},2);
    }

    @Override
    public void draw(Canvas canvas) {

        animation.draw(canvas,rectangle);
    }

    @Override
    public void update() {
        animation.update();
    }


    public void update(Point point){
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);
        animation.play();
        animation.update();
    }

    public Rect getRectangle() {
        return rectangle;
    }
}
