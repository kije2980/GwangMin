package com.example.todaysmenutest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kakao.auth.Session;

public class SplashActivity extends AppCompatActivity {
    static CurrentLocation currentLocation;

    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();

        // postDelayed는 뒤에 초값을 취서 일정시간 이후에 처리하는 명령어
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Session.getCurrentSession().isOpened()) { //로그인이 되어있는 경우 바로 메인으로 intent
                    Intent intent = (new Intent(SplashActivity.this, MainActivity.class));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else{ //로그인이 되어 있지 않은 경우 로그인 액티비티로 intent
                    Intent intent = (new Intent(SplashActivity.this, LoginActivity.class));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }
}
