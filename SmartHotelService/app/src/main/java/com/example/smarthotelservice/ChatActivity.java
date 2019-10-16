package com.example.smarthotelservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private ListView chatlist;
    private Button btn_send, btn_close;
    private EditText chat_edit;
    private ArrayList<String> item;
    private ArrayAdapter<String> frontAdapter;
    private ArrayAdapter<String> userAdapter;
    private TextView user_text, front_text;
    private SocketApplication instance;
    private String message = null;
    private Intent intent;
    private final String BROADCAST_MSG = "com.example.smarthotelservice.RECV";
    private BroadcastReceiver broadcastReceiver;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        intent = getIntent();
        item = new ArrayList<>();
        frontAdapter = new ArrayAdapter<>(this, R.layout.chat_item, R.id.text_front, item);
        userAdapter = new ArrayAdapter<>(this, R.layout.chat_item, R.id.text_user, item);
        chatlist = findViewById(R.id.chat_list);
        chat_edit = findViewById(R.id.chat_edit);
        btn_send = findViewById(R.id.btn_send);
        btn_close = findViewById(R.id.btn_chat_close);
        instance = SocketApplication.getinstance();

        try {
            instance.Recv();
        } catch (RemoteException e) {
            Log.d("리시브 에러", e+"");
        }

        handler = new Handler(){

            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(chatlist.getAdapter()!=frontAdapter)
                    chatlist.setAdapter(frontAdapter);
                item.add(message);
                frontAdapter.notifyDataSetChanged();
            }
        };

        btn_send.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = chat_edit.getText().toString();
                if (text.length() != 0) {
                    if(chatlist.getAdapter()!=userAdapter)
                        chatlist.setAdapter(userAdapter);
                    item.add(text);
                    try {
                        instance.Send("CHAT\n"+text);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    chat_edit.setText("");
                    userAdapter.notifyDataSetChanged();
                }
            }
        });

        btn_close.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver();
    }

    private void registerReceiver(){
        if(broadcastReceiver!=null)
            return;
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_MSG);

        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(BROADCAST_MSG)){
                    message = intent.getStringExtra("msg");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = handler.obtainMessage();
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        };
        this.registerReceiver(this.broadcastReceiver, intentFilter);
    }

    private void unregisterReceiver(){
        if(broadcastReceiver!=null)
            this.unregisterReceiver(broadcastReceiver);
        broadcastReceiver=null;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}