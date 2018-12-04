package com.example.desuev.sberlesson3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btnFirst;
    private Button btnSecond;
    private TextView serviceStatusText;

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
        serviceStatusText = findViewById(R.id.serviceStatusText);
    }

    private void initListeners(){
        btnFirst.setOnClickListener(new ButtonFirstClick());
        btnSecond.setOnClickListener(new ButtonSecondClick());
    }

    private class ButtonFirstClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            startService(MyService.newIntent(MainActivity.this));
            serviceStatusText.setText("Service: On");
        }
    }

    private class ButtonSecondClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            startActivity(ActivityForService.newIntent(MainActivity.this));
        }
    }

}
