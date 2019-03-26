package com.example.networking;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceiveService extends Service {
    private String TAG = "ReceiveService";
    private Socket socket;
    BufferedReader in;

    boolean isRunning = false;

    private Runnable reader = new Runnable() {
        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                String msg;
                String[] splitmsg;
                while (isRunning) {
                    msg = in.readLine();
                    Log.d(TAG, "Received message: " + msg);
                    splitmsg = msg.split(">");
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    IBinder mBinder = new SocketBinder();

    class SocketBinder extends Binder {
        ReceiveService getService() {
            return ReceiveService.this;
        }
    }

    public ReceiveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            socket = new Socket(MainActivity.ip, 9502);
            isRunning = true;
        } catch(Exception e) { e.printStackTrace(); }
        new Thread(reader).start();
    }

    @Override
    public void onDestroy() {
        try{
            isRunning = false;

            in.close();
            socket.close();
        } catch(Exception e) {

        }
        super.onDestroy();
    }
}
