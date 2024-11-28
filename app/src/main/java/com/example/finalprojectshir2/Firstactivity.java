package com.example.finalprojectshir2;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Firstactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_firstactivity);
        Thread t=new Thread() {
            public void run() {

                try {
//sleep thread for 10 seconds, time in milliseconds
                    sleep(3500);

//start new activity
                    Intent i=new Intent(Firstactivity.this,MainActivity.class);
                    startActivity(i);

//destroying Splash activity
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

//start thread
        t.start();
    }

}