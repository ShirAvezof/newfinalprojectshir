package com.example.finalprojectshir2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btsigninp;
    Button btsigninm;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btsigninp=findViewById(R.id.btsigninp);
        btsigninp.setOnClickListener(V -> {
             intent= new Intent(this,LoginActivity.class);
            startActivity(intent);
        });

        btsigninm=findViewById(R.id.btsigninm);
        btsigninm.setOnClickListener(V -> {
            intent=new Intent(this, ManagerLoginActivity.class);
            startActivity(intent);
        });

    }
}