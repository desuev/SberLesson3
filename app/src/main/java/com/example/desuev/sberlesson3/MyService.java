package com.example.desuev.sberlesson3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {


    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 0;

    private List<Messenger> mClients = new ArrayList<>();
    private Messenger mMessenger = new Messenger(new IncomingHandler());

    public MyService() {
    }

    @Override
    public void onCreate(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    Message msg = new Message();
                    msg.obj = System.currentTimeMillis();
                    for(Messenger e: mClients){
                        try {
                            e.send(msg);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                default:
                        break;
            }
        }
    }

    public static final Intent newIntent(Context context){
        Intent intent = new Intent(context, MyService.class);
        return intent;
    }
}
