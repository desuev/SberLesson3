package com.example.desuev.sberlesson3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class ActivityForService extends AppCompatActivity {

    private Button stopButton;
    private TextView textFromService;
    private Messenger myService;
    private final Messenger  mMessenger = new Messenger(new IncomingHandler());
    private boolean isUnbind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_service);

        initComponents();
        initListeners();
        bindService();

        Log.i("INFO:", "FIRST ACTIVITY STARTED");
    }

    @Override
    protected void onResume() {
        super.onResume();
        isUnbind = false;
        bindService();
    }

    @Override
    protected void onPause(){
        super.onPause();
        unBindService();
    }

    private void initComponents(){
        stopButton = findViewById(R.id.stopBtn);
        textFromService = findViewById(R.id.textFromService);
    }

    private void initListeners(){
        stopButton.setOnClickListener(l -> unBindService());
    }

    private ServiceConnection myServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = new Messenger(service);
            Message msg = Message.obtain(null, MyService.MSG_REGISTER_CLIENT);
            msg.replyTo = mMessenger;
            try {
                myService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.v("ActivityForResult", "connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v("ActivityForResult", "disconnected");
        }
    };

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == MyService.EXIT_CODE){
                isUnbind = true;
                return;
            }
            textFromService.setText("Data From Service: " + String.valueOf(msg.obj));
        }
    }

    private void bindService(){
        bindService(MyService.newIntent(ActivityForService.this), myServiceConnection, Context.BIND_AUTO_CREATE);
        isUnbind = false;
    }

    private void unBindService() {
        Message msg = Message.obtain(null, MyService.MSG_UNREGISTER_CLIENT);
        msg.replyTo = mMessenger;
        try {
            myService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(isUnbind) {
            unbindService(myServiceConnection);
            isUnbind = true;
        }
    }

    public static final Intent newIntent(Context context){
        Intent intent = new Intent(context, ActivityForService.class);
        return intent;
    }
}
