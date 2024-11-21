package com.example.finalprojectshir2.Manager.ManagerRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectshir2.Manager.ManagerHome.ManagerHomeActivity;
import com.example.finalprojectshir2.R;

public class ManagerRegisterActivity extends AppCompatActivity implements View.OnClickListener{

    Button signUpButtonm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manager_register);

        signUpButtonm = findViewById(R.id.signUpButtonm);
        signUpButtonm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == signUpButtonm) {
            Intent i = new Intent(this,  ManagerHomeActivity.class);
            startActivity(i);
        }
    }
}