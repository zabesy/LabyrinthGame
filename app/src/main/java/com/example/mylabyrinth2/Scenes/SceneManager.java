package com.example.mylabyrinth2.Scenes;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.mylabyrinth2.Scenes.GameplayScene;
import com.example.mylabyrinth2.Scenes.HomeMenuScene;
import com.example.mylabyrinth2.Scenes.Scene;

import java.util.ArrayList;

public class SceneManager {
    private ArrayList<Scene> scenes = new ArrayList<>();
    public static int ACTIVE_SCENE = 0;


    public SceneManager(){
        scenes.add(new HomeMenuScene());
        scenes.add(new GameplayScene());
    }

    public void receiveTouch(MotionEvent event){
        scenes.get(ACTIVE_SCENE).receiveTouch(event);
    }

    public void update(){
        scenes.get(ACTIVE_SCENE).update();
    }

    public void draw(Canvas canvas){
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }


}
