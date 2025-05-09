package com.example.yp01;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class onBoardingScreen_2_withoutInternet extends AppCompatActivity {

    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboardingscreen_2_withoutinternet);
        gestureDetector = new GestureDetector(this, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        private static final int SWIPE_THRESHOLD = 50;
        private static final int SWIPE_VELOCITY_THRECHOLD = 50;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();
            if(Math.abs(diffX) > Math.abs(diffY)){
                if(Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(diffY) > SWIPE_VELOCITY_THRECHOLD){
                    if(diffX < 0){
                        return false;
                    }
                    else{
                        startActivity(new Intent(onBoardingScreen_2_withoutInternet.this, onBoardingScreen_1.class));
                        finish();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public void OnClickBoardingScreen_1(View view){
        Intent intent = new Intent(this, onBoardingScreen_1.class);
        startActivity(intent);
    }

    public void onMainScreen(View view){
        Intent intent = new Intent(this, mainscreen.class);
        startActivity(intent);
    }
}
