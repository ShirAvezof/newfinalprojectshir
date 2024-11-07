package com.example.finalprojectshir2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    Button signUpButtonp;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        signUpButtonp = findViewById(R.id.signUpButtonp);
        signUpButtonp.setOnClickListener(V -> {
            intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

    }

}