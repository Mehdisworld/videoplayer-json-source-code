package com.mezdev.player.video_player.activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.mezdev.player.video_player.AdsManager.AdsController;
import com.mezdev.player.video_player.R;


public class splash_scr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_scr);


        new Handler().postDelayed(() -> AdsController.GetAdsUnit(splash_scr.this),3000);

    }


    @Override
    protected void onPause() {

        super.onPause();
        finish();

    }


}