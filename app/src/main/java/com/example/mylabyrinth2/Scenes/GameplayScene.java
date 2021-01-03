package com.example.mylabyrinth2.Scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.example.mylabyrinth2.Constants;
import com.example.mylabyrinth2.Objects.ObstacleManager;
import com.example.mylabyrinth2.OrientationData;
import com.example.mylabyrinth2.Objects.RectPlayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class GameplayScene implements Scene {

    private Rect r = new Rect();

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;

    private boolean gameOver = false;
    private boolean gameWon = false;
    private boolean alreadyDoneOnce = false;

    private static ArrayList<Double> theTimes = new ArrayList<>();

    private long gameStopTime;
    private long startTimer = 0;

    private OrientationData orientationData;
    private long frameTime;

    public GameplayScene(){
        player = new RectPlayer(new Rect(100,100,200,200), Color.rgb(255,0,0));

        playerPoint = new Point(300,500);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(Color.BLACK);

        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();


        //Database
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("highscores");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                theTimes.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    //theTimes[i] = Double.parseDouble(snapshot.getValue().toString());
                    theTimes.add(Double.parseDouble(snapshot.getValue().toString()));
                    System.out.println(theTimes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

        Collections.sort(theTimes);

    }

    public void reset(){
        playerPoint = new Point(300,500);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(Color.BLACK);
        movingPlayer = false;
        startTimer = System.currentTimeMillis();
    }

    @Override
    public void update() {
        //If gameWon or gameOver
        if(!gameOver && !gameWon) {
            if(frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if(orientationData.getOrientation() != null && orientationData.getStartOrientation() != null){
                //pitch is the y-axis, roll is the x-axis
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

                //this is where you input the acceleration of the roll/pitch
                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH/1000f;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT/1000f;

                playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
                playerPoint.y -= Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;
            }

            //this is because we dont want the player to be moving off the screen in either the x or y axis
            // Not really needed in this case though
            if(playerPoint.x < 0){
                playerPoint.x = 0;
            }
            else if(playerPoint.x > Constants.SCREEN_WIDTH){
                playerPoint.x = Constants.SCREEN_WIDTH;
            }
            if(playerPoint.y < 0){
                playerPoint.y = 0;
            }
            else if(playerPoint.y > Constants.SCREEN_HEIGHT){
                playerPoint.y = Constants.SCREEN_HEIGHT;
            }


            player.update(playerPoint);
            obstacleManager.update();

            if(obstacleManager.playerCollide(player)){
                gameOver = true;
                gameWon = false;
                gameStopTime = System.currentTimeMillis();
            }else if(obstacleManager.playerWon(player)){
                gameOver = false;
                gameWon = true;
                gameStopTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(startTimer == 0) startTimer = System.currentTimeMillis();
                if((!gameOver || !gameWon) && player.getRectangle().contains((int)event.getX(),(int)event.getY())){
                    movingPlayer = true;
                }
                //Reset game after 2 seconds and the user clicks on it

                if((gameOver || gameWon) && System.currentTimeMillis() - gameStopTime >= 2000){
                    reset();
                    gameOver = false;
                    gameWon = false;
                    alreadyDoneOnce = false;
                    orientationData.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //will set where the finger is on the screen
                if((!gameOver || !gameWon) && movingPlayer){
                    playerPoint.set((int) event.getX(),(int) event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        player.draw(canvas);
        obstacleManager.draw(canvas);

        //If gameWon så sparar jag tiden och displayar highscoren, annars om gameOver så sparas inget och bara texten syns.

        double timeSeconds = (gameStopTime - startTimer)/1000.0;

        if(gameOver){

            drawEndText(canvas,"Game Lost!",timeSeconds);

        }else if(gameWon){

            //compare the first 3 numbers

            boolean changedArray = false;
            //Sorting the values
            if(!alreadyDoneOnce) {
                alreadyDoneOnce = true;
                if (Double.valueOf(timeSeconds) < theTimes.get(0)) {
                    changedArray = true;
                    //theTimes[0]>tmeSeconds
                    theTimes.add(0, timeSeconds);

                } else if (timeSeconds < theTimes.get(1)) {
                    changedArray = true;

                    // theTimes[1]>timeSeconds>theTimes[0]

                    theTimes.add(1, timeSeconds);

                } else if (timeSeconds < theTimes.get(2)) {
                    changedArray = true;

                    //theTimes[2]>timeSeconds>theTimes[1]

                    theTimes.add(2, timeSeconds);
                }

            }

            Collections.sort(theTimes);

            //Updating the database if array is changed
            if(changedArray){

                for(int i=0;i<3;i++){
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("highscores");
                    reference.child("" + (i+1)).setValue(theTimes.get(i));
                }

            }

            drawEndText(canvas,"Game Won!",timeSeconds);

        }



    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }


    private void drawText(Canvas canvas, Paint paint, String text, float yDivision, float xDivision){
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text,0,text.length(),r);
        float x = cWidth / xDivision - r.width() / xDivision - r.left;
        float y = cHeight / yDivision + r.height() / yDivision - r.bottom;
        canvas.drawText(text, x , y, paint);
    }


    private void drawEndText(Canvas canvas, String endText, double timeSeconds){

        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.RED);

        drawText(canvas,paint,endText,3.5f,2f);
        paint.setTextSize(80);
        drawText(canvas,paint,"Your Time: "+ timeSeconds,2.5f, 2f);

        paint.setColor(Color.BLUE);
        paint.setTextSize(80);
        drawText(canvas,paint,"Highscores:", 1.5f,2f);

        paint.setTextSize(60);

        drawText(canvas,paint,"RANK",1.3f,4.0f);
        drawText(canvas,paint,"USER",1.3f,2.0f);
        drawText(canvas,paint,"TIME",1.3f,1.3f);

        paint.setColor(Color.RED);
        paint.setTextSize(50);

        drawText(canvas,paint,"1.",1.25f,3.7f);
        drawText(canvas,paint,"2.",1.2f,3.7f);
        drawText(canvas,paint,"3.",1.15f,3.7f);

        drawText(canvas,paint,"User1",1.25f,2.0f);
        drawText(canvas,paint,"User2",1.2f,2.0f);
        drawText(canvas,paint,"User3",1.15f,2.0f);

        drawText(canvas,paint,theTimes.get(0).toString(),1.25f,1.3f);
        drawText(canvas,paint,theTimes.get(1).toString(),1.2f,1.3f);
        drawText(canvas,paint,theTimes.get(2).toString(),1.15f,1.3f);
    }
}
