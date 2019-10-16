package com.example.smarthotelservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketService extends Service {
    private Socket socket = null;
    private String ip;
    private int port;
    private BufferedWriter writer;
    private BufferedReader reader;
    private SocketAddress socketAddress;
    private Context context = this;
    private String msg, line;


    public SocketService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        this.ip = intent.getStringExtra("ip");
        this.port = intent.getIntExtra("port", 0);
        return new IScoketService.Stub() {
            @Override
            public void Connect() throws RemoteException {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (socket == null) {
                                socketAddress = new InetSocketAddress(ip, port);
                                socket = new Socket();
                                socket.connect(socketAddress, 5000);
                                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                writer.flush();
                            }
                        } catch (Exception e) {
                            Log.d("소켓 연결 에러 : ", e.toString());
                        }
                    }
                }).start();
            }

            @Override
            public void disConnect() throws RemoteException {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Send(final String msg) throws RemoteException {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (socket != null && writer != null) {
                            try {
                                writer.write(msg, 0, msg.length());
                                writer.flush();
                            } catch (IOException e) {
                                Log.d("Send_Error1", e.toString());
                            }
                        } else {
                            Log.d("Send_Error2", "서버에 연결 되지 않았습니다");
                        }
                    }
                }).start();

            }

            public void Recv() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                if (socket == null || reader == null)
                                    break;
                                msg = reader.readLine();
                                if (msg != null) {
                                    try{
                                    Intent intent = new Intent("com.example.smarthotelservice.RECV");
                                    intent.putExtra("msg", msg);
                                    sendBroadcast(intent);}
                                    catch (Exception e){
                                        Log.d("브로드캐스트", e+"");
                                    }
                                }
                            } catch (Exception e) {
                                Log.d("Recv Err", e.toString());
                            }
                        }
                    }
                }).start();
            }

            @Override
            public boolean isConnect() {
                boolean connect;
                if (socket != null) {
                    if (socket.isConnected())
                        connect = true;
                    else
                        connect = false;
                } else
                    connect = false;

                return connect;
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        };
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void startForegroundService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_main);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "foreground_service";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Start Foreground Service", NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        } else {
            builder = new NotificationCompat.Builder(this);
        }
        builder.setContent(remoteViews)
                .setContentIntent(pendingIntent);

        startForeground(1, builder.build());
    }

}
