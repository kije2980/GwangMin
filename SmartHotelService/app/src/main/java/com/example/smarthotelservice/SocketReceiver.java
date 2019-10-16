package com.example.smarthotelservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SocketReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("msg"), Toast.LENGTH_SHORT).show();
        Intent chatintent = new Intent(context, ChatActivity.class);
        chatintent.putExtra("msg",intent.getStringExtra("msg"));
    }
}
