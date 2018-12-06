package com.example.desuev.sberlesson3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private Button btnFirst;
    private Button btnSecond;
    private Switch servicePatternSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        initListeners();
    }

    private void initComponent(){
        btnFirst = findViewById(R.id.btnFirst);
        btnSecond = findViewById(R.id.btnSecond);
        servicePatternSwitch = findViewById(R.id.servicePatternSwitch);
    }

    private void initListeners(){
        btnFirst.setOnClickListener(l -> {
            if(servicePatternSwitch.isChecked()) {
                startService(MyService.newIntent(MainActivity.this));
            }else{
                startService(SecondService.newIntent(MainActivity.this));
            }
        });

        btnSecond.setOnClickListener(l -> {
            if(servicePatternSwitch.isChecked()) {
                startActivity(ActivityForService.newIntent(MainActivity.this));
            } else{
                startActivity(SecondActivityForService.newIntent(MainActivity.this));
            }
        });
    }


}
