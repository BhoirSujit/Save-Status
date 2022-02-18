package com.gameside.savestatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MediaPlayerActivity extends AppCompatActivity {
    private final String TAG = "MPATAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        Intent intent = getIntent();
        String filepath = intent.getStringExtra("filepath");

        Log.d(TAG, "filepath is " +filepath);

    }
}