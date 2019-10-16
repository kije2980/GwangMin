package com.example.smarthotelservice;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.net.Socket;


/*하나의 소켓을 액티비티에서 공유하기 위한 클래스  싱글톤 패턴 */
public class SocketApplication extends Application {
    private static final SocketApplication instance = new SocketApplication();
    private static final String ip = "27.117.199.42";
    private static final int port = 14334;
    private IScoketService pService=null;
    private Intent intent;
    private Socket socket;
    private ServiceConnection connection = new ServiceConnection() {
        //bindService()가 실행될때 진입
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pService = IScoketService.Stub.asInterface(service);
            instance.setpService(pService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            pService = null;
        }
    };

    public SocketApplication(){
        //don't use initialized
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent("com.example.smarthotelservice.ACTION_PLAY");
        intent.setPackage("com.example.smarthotelservice");
        intent.putExtra("ip",ip);
        intent.putExtra("port",port);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }
    public static SocketApplication getinstance(){
        return instance;
    }


    public  void setpService(IScoketService pService)
    {
        this.pService = pService;
    }

    public IScoketService getpService()
    {
        return pService;
    }

    public void Connect()
    {
        try {
            pService.Connect();
        } catch (RemoteException e) {
            Log.d("SocAPP con Err",e.toString());
        }
    }
    public void Send(String msg) throws RemoteException {
        pService.Send(msg);
    }

    public boolean isConnect() throws RemoteException {

        return pService.isConnect();
    }

    public void Recv() throws RemoteException {
        pService.Recv();
    }
}
