package com.example.desuev.sberlesson3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivityForService extends AppCompatActivity implements Observer {

    private TextView msgFromService;
    private Button stopServiceBtn;
    private SecondService currentDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_for_service);

        initComponents();
        initListeners();
        bindService();

        Log.i("INFO:", "SECOND ACTIVITY STARTED");

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService();
    }

    private void initComponents(){
        msgFromService = findViewById(R.id.secondTextFromService);
        stopServiceBtn = findViewById(R.id.secondStopBtn);
    }

    private void initListeners(){
        stopServiceBtn.setOnClickListener( l -> unbindService());
    }

    private void bindService(){
        bindService(SecondService.newIntent(SecondActivityForService.this), sv, Context.BIND_AUTO_CREATE);
    }

    private void unbindService(){
        currentDataService.unregisterListener(SecondActivityForService.this);
        unbindService(sv);
    }


    private ServiceConnection sv = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            currentDataService = ((SecondService.LocalBinder)service).getService();
            currentDataService.registerListener(SecondActivityForService.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            currentDataService = null;
        }
    };

    @Override
    public void update(String value) {
        this.runOnUiThread(() -> msgFromService.setText("Data From Service: " + value));
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, SecondActivityForService.class);
        return intent;
    }

}
