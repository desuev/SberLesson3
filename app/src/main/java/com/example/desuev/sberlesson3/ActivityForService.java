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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityForService extends AppCompatActivity {

    private Button stopButton;
    private TextView textFromService;
    private Messenger myService;
    final Messenger  mMessenger = new Messenger(new IncomingHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_service);

        initComponents();
        initListeners();
        bindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService();
    }

    @Override
    protected void onPause(){
        super.onPause();
        unbindService();
    }

    private void initComponents(){
        stopButton = findViewById(R.id.stopBtn);
        textFromService = findViewById(R.id.textFromService);
    }

    private void initListeners(){
        stopButton.setOnClickListener(new ButtonStopClick());
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
            textFromService.setText("Data From Service: " + String.valueOf(msg.obj));
        }
    }


    private void bindService(){
        bindService(MyService.newIntent(ActivityForService.this), myServiceConnection, Context.BIND_AUTO_CREATE);
}

    private void unbindService(){
        Message msg = Message.obtain(null, MyService.MSG_UNREGISTER_CLIENT);
        msg.replyTo = mMessenger;
        try {
            myService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(myServiceConnection);
    }

    private class ButtonStopClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            unbindService();
        }
    }

    public static final Intent newIntent(Context context){
        Intent intent = new Intent(context, ActivityForService.class);
        return intent;
    }
}
