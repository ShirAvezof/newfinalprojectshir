package com.example.finalprojectshir2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManagerLoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView gotoSignUp;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_login);

        gotoSignUp = findViewById(R.id.gotoSignUp);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(this);
        gotoSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == gotoSignUp) {
            Intent i = new Intent(this, ManagerRegisterActivity.class);
            startActivity(i);
        }
        if(v == loginButton) {
            Intent i = new Intent(this, ManagerHomeActivity.class);
            startActivity(i);
        }
    }
}