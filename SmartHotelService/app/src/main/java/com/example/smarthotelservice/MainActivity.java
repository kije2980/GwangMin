package com.example.smarthotelservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private Button btn_switch, btn_room_services, btn_chat, btn_setting;
    private TextView text_temp, text_humi, text_rnum;
    private SocketApplication instance;
    private BroadcastReceiver myreceiver;
    private String BROADCAST_MESSAGE = "com.example.smarthotelservice.start_service";
    private Handler handler;
    private IScoketService pService = null;
    private long lastTimeBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_switch = findViewById(R.id.btn_switch);
        btn_chat = findViewById(R.id.btn_chat);
        btn_room_services = findViewById(R.id.btn_room_services);

        instance = SocketApplication.getinstance();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pService == null) {
                    pService = SocketApplication.getinstance().getpService();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                SocketApplication.getinstance().Connect();
                /*try {
                    SocketApplication.getinstance().Recv();
                } catch (RemoteException e) {
                    Log.d("리시브에러",e+"");
                }*/
            }
        }).start();

        btn_switch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SwitchActivity.class);
                startActivity(intent);
            }
        });
        btn_chat.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    instance.Send("APP54321");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
        btn_room_services.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RoomServiceActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 2000)
        {
            ActivityCompat.finishAffinity(this);
        }
        Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
