package com.example.a4k8kwallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000; // 3 seconds
    TextView idDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        idDot = findViewById(R.id.idDot);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            // Set the status bar color
//            window.setStatusBarColor(getResources().getColor(R.color.colortransparent));
//        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//  Remove Status bar


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);  // hide mobile key button

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start MainActivity after the splash screen duration
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    private final Handler handler = new Handler();
    private int dotCount = 0;
    private final Runnable dotAnimation = new Runnable() {
        @Override
        public void run() {
            StringBuilder loadingText = new StringBuilder();
            for (int i = 0; i < dotCount; i++) {
                loadingText.append(".");
            }
            idDot.setText(loadingText.toString());

            dotCount = (dotCount + 1) % 7;
            handler.postDelayed(this, 500);
        }
    };


    private void startLoadingAnimation() {
        dotAnimation.run();
    }
    @Override
    protected void onStart() {
        super.onStart();
        startLoadingAnimation();
    }
    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(dotAnimation);
    }

}