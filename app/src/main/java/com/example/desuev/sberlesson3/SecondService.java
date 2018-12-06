package com.example.desuev.sberlesson3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SecondService extends Service {

    private IBinder binder = new LocalBinder();
    private List<Observer> observers;


    public SecondService() {
        observers = new ArrayList<>();
    }

    public class LocalBinder extends Binder {
        SecondService getService(){
            return SecondService.this;
        }
    }

    @Override
    public void onCreate(){
        updateValue();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void registerListener(Observer obs) {
        observers.add(obs);
    }

    public void unregisterListener(Observer obs) {
        observers.remove(obs);
    }

    public void updateValue(){
        new Thread(() -> {
            while(true) {
                for (Observer e : observers) {
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    e.update(dateFormat.format(date));
                }
            }
        }).start();
    }


    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, SecondService.class);
        return intent;
    }

}
