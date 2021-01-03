package com.example.mylabyrinth2.Objects;

import android.graphics.Canvas;
import android.graphics.Color;

import com.example.mylabyrinth2.Constants;
import com.example.mylabyrinth2.Objects.Labyrinth;
import com.example.mylabyrinth2.Objects.RectPlayer;

import java.util.ArrayList;

public class ObstacleManager {
    private ArrayList<Labyrinth> labyrinthLines;
    private Labyrinth wonRect;
    private int color;
/*    private static final int COL = 10, ROW = 10;
    private static final int cellSize = (Constants.SCREEN_HEIGHT - 20)/10;*/

    private long startTime;


    public ObstacleManager(int color){
        this.color = color;
        labyrinthLines = new ArrayList<>();
        populateLabyrinth();

        startTime = System.currentTimeMillis();
    }

    public boolean playerCollide(RectPlayer player){
        for(Labyrinth lab: labyrinthLines){
            if(lab.playerCollide(player)){
                return true;
            }
        }
        return false;
    }

    public boolean playerWon(RectPlayer player){
        if(wonRect.playerCollide(player)){
            return true;
        }
        return false;
    }

    //This is where you make the labyrinth, premade
    private void populateLabyrinth(){
        labyrinthLines.add(new Labyrinth(color,10,10,30, Constants.SCREEN_HEIGHT - 10));
        labyrinthLines.add(new Labyrinth(color,10,10,Constants.SCREEN_WIDTH - 10, 30));
        labyrinthLines.add(new Labyrinth(color, Constants.SCREEN_WIDTH - 30, 10, Constants.SCREEN_WIDTH -10, Constants.SCREEN_HEIGHT -10));
        labyrinthLines.add(new Labyrinth(color,10,Constants.SCREEN_HEIGHT - 30, Constants.SCREEN_WIDTH - 10, Constants.SCREEN_HEIGHT - 10));

        //This is if you want to make a premade map without "CELLS"
        labyrinthLines.add(new Labyrinth(color, 200,300,220,900));
        labyrinthLines.add(new Labyrinth(color, 200,580,400,600));
        labyrinthLines.add(new Labyrinth(color, 380,10,400,600));
        labyrinthLines.add(new Labyrinth(color, 10,1180,400,1200));
        labyrinthLines.add(new Labyrinth(color, 380,900,400,1200));
        labyrinthLines.add(new Labyrinth(color, 380,900,600,880));
        labyrinthLines.add(new Labyrinth(color, 580,300,600,880));
        labyrinthLines.add(new Labyrinth(color, 600,300,880,320));
        labyrinthLines.add(new Labyrinth(color, 860,300,880,1200));
        labyrinthLines.add(new Labyrinth(color, 200,1380,Constants.SCREEN_WIDTH - 20,1400));
        labyrinthLines.add(new Labyrinth(color, 660,1060,680,1400));
        labyrinthLines.add(new Labyrinth(color, 200,1380,220,1600));
        labyrinthLines.add(new Labyrinth(color, 200,1580,600,1600));
        labyrinthLines.add(new Labyrinth(color, 580,1380,600,1600));
        labyrinthLines.add(new Labyrinth(color, 740,1530,760,Constants.SCREEN_HEIGHT - 20));
        labyrinthLines.add(new Labyrinth(color, 740,1530,910,1550));
        labyrinthLines.add(new Labyrinth(color, 890,1530,910,1650));

        wonRect = new Labyrinth(Color.BLUE,765,1555,885,1645);
    }

    public void update(){
        if(startTime < Constants.INIT_TIME){
            startTime = Constants.INIT_TIME;
        }
    }

    public void draw(Canvas canvas){
        for(Labyrinth lab: labyrinthLines){
            lab.draw(canvas);
        }
        wonRect.draw(canvas);
    }

}
