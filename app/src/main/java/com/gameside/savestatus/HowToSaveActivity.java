package com.gameside.savestatus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import java.util.Objects;

public class HowToSaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_save);
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.dimAmount = 0;
        wlp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        getWindow().setAttributes(wlp);
    }
}