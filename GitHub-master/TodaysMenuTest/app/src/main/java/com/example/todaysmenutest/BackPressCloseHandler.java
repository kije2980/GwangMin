package com.example.todaysmenutest;

import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHandler {

    private long backPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context){
        this.activity = context;
    }

    public void onBackPressed(){ //뒤로가기 버튼을 눌렀을때
        if(System.currentTimeMillis() > backPressedTime + 2000){ //한 번 눌렀을때 or
                                                                // 한번 누르고 2초후에 눌렀을때 ->토스트 메시지
            backPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if(System.currentTimeMillis() <=backPressedTime + 2000){ //한 번 누르고 2초전에 눌렀을때 ->앱종료
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide(){ //토스트 메시지
        toast = Toast.makeText(activity,"\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

}
