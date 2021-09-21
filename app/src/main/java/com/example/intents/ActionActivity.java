package com.example.intents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.intents.databinding.ActivityActionBinding;

public class ActionActivity extends AppCompatActivity {

    ActivityActionBinding activityActionBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityActionBinding = ActivityActionBinding.inflate(getLayoutInflater());
        setContentView(activityActionBinding.getRoot());

        activityActionBinding.mainTb.appTb.setTitle(ActionActivity.this.getClass().getSimpleName());
        activityActionBinding.mainTb.appTb.setSubtitle(getIntent().getAction());
        setSupportActionBar(activityActionBinding.mainTb.appTb);
        activityActionBinding.parameterTv.setText(getIntent().getStringExtra(Intent.EXTRA_TEXT));
    }
}