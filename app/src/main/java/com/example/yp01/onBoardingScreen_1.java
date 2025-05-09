package com.example.yp01;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class onBoardingScreen_1 extends AppCompatActivity {

    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboardingscreen_1);
        gestureDetector = new GestureDetector(this, new GestureListener());
    }

    public boolean isInernetAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
                    if(diffX > 0){
                        return false;
                    }
                    else{
                        if(isInernetAvailable(onBoardingScreen_1.this) == true){
                            startActivity(new Intent(onBoardingScreen_1.this, onBoardingScreen_2.class));
                        }
                        else{
                            startActivity(new Intent(onBoardingScreen_1.this, onBoardingScreen_2_withoutInternet.class));
                        }
                        finish();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public void OnClickBoardingScreen_2(View view){
        if(isInernetAvailable(onBoardingScreen_1.this) == true){
            Intent intent = new Intent(this, onBoardingScreen_2.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, onBoardingScreen_2_withoutInternet.class);
            startActivity(intent);
        }
    }
}
