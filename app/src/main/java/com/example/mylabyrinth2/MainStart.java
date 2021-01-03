package com.example.mylabyrinth2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainStart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);
    }

    public void buttonPushed(View view) {
        startActivity(new Intent(MainStart.this, MainActivity.class));
        finish();
    }
}