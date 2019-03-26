package com.example.networking;

import com.example.networking.ReceiveService.SocketBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    ReceiveService socketsvc;

    public static String ip = "192.168.0.1";
    public Button connectBtn;
    public Button disconnectBtn;


    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SocketBinder binder = (SocketBinder) service;
            socketsvc = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent(this, ReceiveService.class);
        startService(intent);

        connectBtn = findViewById(R.id.ConnectButton);
        connectBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                bindService(intent, conn, Context.BIND_AUTO_CREATE);
            }
        });

        disconnectBtn = findViewById(R.id.DisconnectButton);


    }

    @Override
    protected void onDestroy(){
        Intent intent = new Intent(this, ReceiveService.class);
        stopService(intent);
        super.onDestroy();
    }
}
